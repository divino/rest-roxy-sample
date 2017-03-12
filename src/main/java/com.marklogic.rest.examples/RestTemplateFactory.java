package com.marklogic.rest.examples;

import org.apache.http.HttpHost;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@PropertySource("file:deploy/local.properties")
public class RestTemplateFactory
        implements FactoryBean<RestTemplate>, InitializingBean {

    private static final String PROTOCOL = "http";

    @Value("${user}")
    private String user;

    @Value("${password}")
    private String password;

    @Value("${app-port}")
    private int appPort;

    @Value("${local-server}")
    private String mlHostname;

    private RestTemplate restTemplate;

    public RestTemplate getObject() {
        restTemplate.getInterceptors().add(
                new BasicAuthorizationInterceptor(this.user, this.password));
        restTemplate.getInterceptors().add(
                new LoggingRequestInterceptor());
        return restTemplate;
    }
    public Class<RestTemplate> getObjectType() {
        return RestTemplate.class;
    }
    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() {
        System.out.println("Dumaan po ako dito!!!!");
        HttpHost host = new HttpHost(this.mlHostname, this.appPort, PROTOCOL);
        restTemplate = new RestTemplate(
                new HttpComponentsClientHttpRequestFactoryBasicAuth(host));
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
    }
}