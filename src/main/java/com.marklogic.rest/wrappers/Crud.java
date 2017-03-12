package com.marklogic.rest.wrappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.marklogic.rest.wrappers.pojo.Person;

import java.io.IOException;

/**
 * Created by dbagayau on 12/03/2017.
 */
@Component
public class Crud {

    @Autowired
    private com.marklogic.rest.wrappers.Configuration configuration;

    private String getBaseUrl () {
        return "http://" + configuration.getMlHostname() + ":" + configuration.getAppPort();
    }

    private String getCrudUrl() {
        return getBaseUrl() + EndpointConstants.CRUD_URI;
    }

    public void insert(String docUri, Person person) throws IOException {
        try (CloseableHttpClient httpClient = configuration.getHttpClient()) {
            HttpPost httpPost = new HttpPost( getCrudUrl() + "?rs:uri=" + docUri);
            ObjectMapper objectMapper = new ObjectMapper();
            StringEntity entity = new StringEntity(objectMapper.writeValueAsString(person));
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = httpClient.execute(httpPost);
            response.close();
        }
    }

    public Person retrieve(String docUri) throws IOException {
        Person person = null;
        try (CloseableHttpClient httpClient = configuration.getHttpClient()) {
            HttpGet httpGet = new HttpGet( getCrudUrl() + "?rs:uri=" + docUri);
            httpGet.addHeader("accept", "application/json");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            ObjectMapper objectMapper = new ObjectMapper();
            person = objectMapper.readValue(response.getEntity().getContent(), Person.class);
            response.close();
        }
        return person;
    }

}
