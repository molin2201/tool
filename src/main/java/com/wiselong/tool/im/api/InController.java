package com.wiselong.tool.im.api;


import com.wiselong.tool.im.bean.BlackReqAi;
import com.wiselong.tool.im.bean.BlackResp;
import com.wiselong.tool.im.service.BlackBiz;
import com.wiselong.tool.im.service.PhoneServiceImpl;
import com.wiselong.tool.im.bean.BlackReq;
import com.wiselong.tool.im.cache.RedissonCache;
import com.wiselong.tool.im.common.JsonUtil;
import com.wiselong.tool.im.common.RedisKey;
import com.wiselong.tool.im.common.SysConfig;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/zl/black/*")
public class InController {
    private static final Logger logger = LoggerFactory.getLogger(InController.class);
    @Autowired
    private PhoneServiceImpl phoneService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedissonCache redissonCache;

    @Autowired
    private SysConfig sysConfig;
    @Autowired
    private BlackBiz blackBiz;


    private String buildPriKey(String phone,String version){
        if(StringUtils.isEmpty(version)){
            version="1.0";
        }
        StringBuffer sb=new StringBuffer();
        sb.append(RedisKey.PRI_PHONEBLACK).append(".").append(version).append(".").append(phone);
        return sb.toString();
    }
    public boolean checkAllthird(BlackReq req){
        List<String> urlList =  sysConfig.getUrlList();
        if(urlList!=null && urlList.size()>0){
            for(String url:urlList){
               if(thirdCheck(url,req)){
                   return true;
               }
            }
        }
        return false;
    }
    private boolean thirdCheck(String url,BlackReq req){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        String content=JsonUtil.obj2json(req);
        HttpEntity<String> request = new HttpEntity<>(content, headers);
        try{
            ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
            if(response!=null && StringUtils.isNotEmpty(response.getBody())&&response.getBody().contains("forbid")){
                logger.info("黑名单查询第三方返回："+response.getBody());
                BlackResp resp=JsonUtil.objectMapper.readValue(response.getBody(),BlackResp.class);
               if(resp.getForbid()!=null){
                   redissonCache.setCacheMapValue(RedisKey.PUB_PHONEBLACK,req.getCallee(),"1");
                   phoneService.saveThirdBlack(req.getCallee());
                   return true;
               }
            }
        }catch(Exception ex){
            if(sysConfig.getOpenAsyn()!=null && "Y".equals(sysConfig.getOpenAsyn().toUpperCase())){
                blackBiz.publish(url,req);
            }
            logger.error("黑名单查询第三方异常"+url+","+ex.getMessage());
        }
        return false;
    }

    private  BlackReq checkPhone(BlackReq blackReq){
        if(blackReq!=null && blackReq.getCallee()!=null && blackReq.getCallee().length()>11){
            String phone=blackReq.getCallee();
            phone =phone.substring(phone.length()-11,phone.length());
            blackReq.setCallee(phone);
        }
        return blackReq;
    }

    /**
     * 保存黑名单
     * @param request
     * @return
     */
    @PostMapping("/blackInfo")
    @ResponseBody
    public String blackInfo( HttpServletRequest request) {
        BlackResp blackResp=new BlackResp();
        try {
            String req =  getRequestPostStr(request);
            logger.info("黑名单查询参数："+req);
            BlackReq blackReq=null;
            if(req.contains("&")){
                StringBuffer sb=new StringBuffer();
                req = req.replace("=","\":\"").replace("&","\",\"");
                sb.append("{\"").append(req).append("\"}");
                blackReq=JsonUtil.objectMapper.readValue(sb.toString(),BlackReq.class);
            }else{
             blackReq=JsonUtil.objectMapper.readValue(req,BlackReq.class);
                if(blackReq==null ||StringUtils.isEmpty(blackReq.getCallee())){
                    BlackReqAi blackReqAi=JsonUtil.objectMapper.readValue(req,BlackReqAi.class);
                    blackReq =blackReqAi.getRewtiteE164Req();
                }
            }
           // BlackReq  blackReq=blackReqAi.getRewtiteE164Req();
            blackResp.setCallId(blackReq.getCallId());
            blackReq= checkPhone(blackReq);
            String key=buildPriKey(blackReq.getCallee(),blackReq.getVersion());
            String phone =redissonCache.getCacheObject(key);
            if(StringUtils.isNotEmpty(phone)){
                blackResp.setForbid(1);//
                blackResp.setTransactionId(UUID.randomUUID().toString().replace("-",""));
            }else if (redissonCache.cacheMapExists(RedisKey.PUB_PHONEBLACK, blackReq.getCallee())) {
                blackResp.setForbid(1);//
                blackResp.setTransactionId(UUID.randomUUID().toString().replace("-",""));
            } else {
                if (checkAllthird(blackReq)) {
                    blackResp.setForbid(1);//
                    blackResp.setTransactionId(UUID.randomUUID().toString().replace("-",""));
                }else{
                    redissonCache.setCacheObject(key,"1",sysConfig.getExpireTimeMin(), TimeUnit.MINUTES);
                }
            }
        }catch(Exception ex){
            logger.error("黑名单查询异常",ex);
        }
        String ret = JsonUtil.obj2json(blackResp);
        logger.info("黑名单查询返回参数："+ret);
        return ret;
    }

    /**
     * 保存黑名单
     * @param blackReq
     * @return
     */
    @RequestMapping(value ="saveBlackInfo", method = RequestMethod.POST)
    public String saveBlackInfo(@RequestBody BlackReq blackReq, HttpServletResponse response) {
              BlackResp blackResp=new BlackResp();
            try {
                logger.info("黑名单查询参数："+JsonUtil.obj2json(blackResp));
                response.setContentType("text/html;charset=UTF-8");
                blackResp.setCallId(blackReq.getCallId());
                if (redissonCache.cacheMapExists(RedisKey.PUB_PHONEBLACK, blackReq.getCallee())) {
                    blackResp.setForbid(1);//
                    blackResp.setTransactionId(UUID.randomUUID().toString().replace("-",""));
                } else {
                    if (checkAllthird(blackReq)) {
                        blackResp.setForbid(1);//
                        blackResp.setTransactionId(UUID.randomUUID().toString().replace("-",""));
                    }else{
                        String key=buildPriKey(blackReq.getCallee(),blackReq.getVersion());
                        String phone =redissonCache.getCacheObject(key);
                        if(StringUtils.isNotEmpty(phone)){
                            blackResp.setForbid(1);//
                            blackResp.setTransactionId(UUID.randomUUID().toString().replace("-",""));
                        }else{
                            redissonCache.setCacheObject(key,"1",sysConfig.getExpireTimeMin(), TimeUnit.MINUTES);
                        }
                    }
                }
            }catch(Exception ex){
                logger.error("黑名单查询异常",ex);
            }
            String ret = JsonUtil.obj2json(blackResp);
             logger.info("黑名单查询返回参数："+ret);
            return  ret;
    }

    /***
     * Compatible with GET and POST
     *
     * @param request
     * @return : <code>byte[]</code>
     * @throws IOException
     */
    public static byte[] getRequestQuery(HttpServletRequest request)
            throws IOException {
        String submitMehtod = request.getMethod();
        String queryString = null;

        if (submitMehtod.equals("GET")) {// GET
            queryString = request.getQueryString();
            String charEncoding = request.getCharacterEncoding();// charset
            if (charEncoding == null) {
                charEncoding = "UTF-8";
            }
            return queryString.getBytes(charEncoding);
        } else {// POST
            return getRequestPostBytes(request);
        }
    }

    /***
     * Get request query string, form method : post
     *
     * @param request
     * @return byte[]
     * @throws IOException
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if(contentLength<0){
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength;) {

            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }
    /***
     * Get request query string, form method : post
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestPostStr(HttpServletRequest request)
            throws IOException {
        byte buffer[] = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }
}
