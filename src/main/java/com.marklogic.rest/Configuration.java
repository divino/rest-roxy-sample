package com.marklogic.rest.wrappers;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * References:
 *  https://hc.apache.org/httpcomponents-client-ga/quickstart.html
 *  https://hc.apache.org/httpcomponents-client-ga/examples.html
 *
 * Thank you!!!
 */
@org.springframework.context.annotation.Configuration
/* Note:
 *   reusing the local.properties to centralize configuration
 */
@PropertySource("file:deploy/local.properties")
public class Configuration {

    @Value("${admin-user}")
    private String user;

    @Value("${password}")
    private String password;

    @Value("${app-port}")
    private int appPort;

    @Value("${xcc-port}")
    private String xccPort;

    @Value("${local-server}")
    private String mlHostname;

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getAppPort() {
        return appPort;
    }

    public String getXccPort() {
        return xccPort;
    }

    public String getMlHostname() {
        return mlHostname;
    }

    @Bean
    public DatabaseClient getDBClient() {
        return DatabaseClientFactory.newClient(
                this.mlHostname,
                this.appPort,
                this.user,
                this.password,
                DatabaseClientFactory.Authentication.BASIC);
    }

    public CloseableHttpClient getHttpClient () {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(this.mlHostname, this.appPort),
                new UsernamePasswordCredentials(this.user, this.password));
        return HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", appPort=" + appPort +
                ", xccPort='" + xccPort + '\'' +
                ", mlHostname='" + mlHostname + '\'' +
                '}';
    }
}
