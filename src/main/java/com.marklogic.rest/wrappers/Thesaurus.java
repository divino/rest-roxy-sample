package com.marklogic.rest.wrappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklogic.rest.pojo.Synonyms;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
* References:
* http://www.baeldung.com/httpclient-post-http-request
*
* Thank you!!!
*/
@Component
public class Thesaurus {

    @Autowired
    private com.marklogic.rest.wrappers.Configuration configuration;

    private String getBaseUrl () {
        return "http://" + configuration.getMlHostname() + ":" + configuration.getAppPort();
    }

    private String getThesaurusUrl() {
        return getBaseUrl() + EndpointConstants.THESAURUS_URI;
    }

    public void load(String name, String filename) throws IOException {
        try (CloseableHttpClient httpClient = configuration.getHttpClient()) {
            HttpPost httpPost = new HttpPost( getThesaurusUrl() + "?rs:name=" + name);
            httpPost.setHeader("Content-Type", "application/xml");
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            StringEntity entity = new StringEntity(content);
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            System.out.println("status code=" + response.getStatusLine().getStatusCode());
            response.close();
        }
    }

    public Synonyms retrieve(String thesaurus, String word) throws IOException {
        Synonyms synonyms = null;
        try (CloseableHttpClient httpClient = configuration.getHttpClient()) {
            HttpGet httpGet = new HttpGet( getThesaurusUrl() + "?rs:thesaurus=" + thesaurus + "&rs:word=" + word);
            httpGet.addHeader("accept", "application/json");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            ObjectMapper objectMapper = new ObjectMapper();
            synonyms = objectMapper.readValue(response.getEntity().getContent(), Synonyms.class);
            response.close();
        }
        return synonyms;
    }
}
