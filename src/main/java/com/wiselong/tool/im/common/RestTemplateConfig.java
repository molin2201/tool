package com.wiselong.tool.im.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
@Configuration
public class RestTemplateConfig {
    @Value("${httpTimeOut}")
    private Integer  httpTimeOut;
    @Value("${httpConnectTimeOut}")
    private Integer  httpConnectTimeOut;

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(httpConnectTimeOut);
        factory.setReadTimeout(httpTimeOut);
        return factory;
    }

    public Integer getHttpTimeOut() {
        return httpTimeOut;
    }

    public void setHttpTimeOut(Integer httpTimeOut) {
        this.httpTimeOut = httpTimeOut;
    }

    public Integer getHttpConnectTimeOut() {
        return httpConnectTimeOut;
    }

    public void setHttpConnectTimeOut(Integer httpConnectTimeOut) {
        this.httpConnectTimeOut = httpConnectTimeOut;
    }
}
