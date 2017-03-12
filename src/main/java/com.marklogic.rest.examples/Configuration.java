package com.marklogic.rest.examples;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by dbagayau on 12/03/2017.
 */
@org.springframework.context.annotation.Configuration
@PropertySource("file:deploy/local.properties")
public class Configuration {

    @Value("${user}")
    private String user;

    @Value("${password}")
    private String password;

    @Value("${app-port}")
    private int appPort;

    @Value("${xcc-port}")
    private String xccPort;

    @Value("${local-server}")
    private String mlHostname;

    @Bean
    public DatabaseClient getDBClient() {
        return DatabaseClientFactory.newClient(
                this.mlHostname,
                this.appPort,
                this.user,
                this.password,
                DatabaseClientFactory.Authentication.BASIC);
    }

}
