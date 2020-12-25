package com.wiselong.tool.im.service;

import com.wiselong.tool.im.bean.BlackReq;
import com.wiselong.tool.im.bean.BlackResp;
import com.wiselong.tool.im.bean.Msg;
import com.wiselong.tool.im.bean.PhoneEntity;
import com.wiselong.tool.im.cache.RedissonCache;
import com.wiselong.tool.im.common.JsonUtil;
import com.wiselong.tool.im.common.RedisKey;
import com.wiselong.tool.im.common.SysConfig;
import io.micrometer.core.instrument.util.StringUtils;
import org.redisson.api.RTopic;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class BlackBiz {
    private static final Logger logger = LoggerFactory.getLogger(BlackBiz.class);

    @Autowired
    public SysConfig sysConfig;

    @Autowired
    public PhoneServiceImpl phoneService;
    @Autowired
    private RedissonCache redissonCache;
    private RestTemplate getTemplate(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(10000);
        return new RestTemplate(factory);
    }

    // 订阅
    @PostConstruct
    public void subscribe(){
        RTopic rTopic = redissonCache.getRedissonClient().getTopic(RedisKey.TOPIC_ERROR);
        rTopic.addListener(Msg.class, new MessageListener<Msg>() {
            // 接受订阅的消息
            @Override
            public void onMessage(CharSequence charSequence, Msg msg) {
                logger.info("异步消息主{}，内容={}",charSequence,msg);
                BlackReq req=new BlackReq();
                req.setCallee(msg.getCallee());
                req.setCaller(msg.getCaller());
                req.setCallId(msg.getCallId());
                req.setExtraData(msg.getExtraData());
                req.setVersion(msg.getVersion());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_HTML);
                String content= JsonUtil.obj2json(req);
                HttpEntity<String> request = new HttpEntity<>(content, headers);
                try{
                    ResponseEntity<String> response = getTemplate().postForEntity( msg.getUrl(), request , String.class );
                    if(response!=null && StringUtils.isNotEmpty(response.getBody())){
                        logger.info("异步查询返回："+response.getBody());
                        BlackResp resp=JsonUtil.objectMapper.readValue(response.getBody(),BlackResp.class);
                        if(resp.getForbid()!=null){
                            redissonCache.setCacheMapValue(RedisKey.PUB_PHONEBLACK,req.getCallee(),"1");
                            phoneService.saveThirdBlack(req.getCallee());
                        }
                    }
                }catch(Exception ex){
                    logger.error("异步查询异常"+ex.getMessage());
                }
            }
        });
    }

    //发布
    public long publish(String url,BlackReq req){
        Msg msg=new Msg();
        msg.setCallee(req.getCallee());
        msg.setCaller(req.getCaller());
        msg.setExtraData(req.getExtraData());
        msg.setVersion(req.getVersion());
        msg.setUrl(url);
        RTopic rTopic = redissonCache.getRedissonClient().getTopic(RedisKey.TOPIC_ERROR);
        return rTopic.publish(msg);
    }



    public void initPubBlackPhone(){
             //redissonCache.clearCacheMap(RedisKey.PUB_PHONEBLACK);
        if(sysConfig.getPhoneList()!=null){
            for(String phone:sysConfig.getPhoneList()){
                redissonCache.setCacheMapValue(RedisKey.PUB_PHONEBLACK,phone,"1");
            }
        }
        boolean imPort=true;
        while(imPort){
            List<PhoneEntity> list = phoneService.queryBySize("N",sysConfig.getImportSize());
            if(list!=null&& list.size()>0){
                for(PhoneEntity phoneEntity:list){
                    redissonCache.setCacheMapValue(RedisKey.PUB_PHONEBLACK,phoneEntity.getPhone(),"1");
                    phoneEntity.setIsImport("Y");
                }
                phoneService.batchUpdateIsImport(list);
                logger.info("导入完成:"+list.size());
            }else{
                imPort = false;
            }
        }
    }
}
