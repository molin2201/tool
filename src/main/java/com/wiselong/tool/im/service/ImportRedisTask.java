package com.wiselong.tool.im.service;

import com.wiselong.tool.im.common.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ImportRedisTask {
    private static final Logger logger = LoggerFactory.getLogger(ImportRedisTask.class);


    @Scheduled(cron = "*/50 * * * * ?")
    public void execute() {
        try{
            SpringUtil.getBean(BlackBiz.class).initPubBlackPhone();
        }catch(Exception ex){
            logger.error("黑名单初始到redis异常",ex);
        }
    }
}
