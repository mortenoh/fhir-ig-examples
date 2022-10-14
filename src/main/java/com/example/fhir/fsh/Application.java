package com.example.fhir.fsh;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.hisp.dhis.integration.sdk.Dhis2ClientBuilder;
import org.hisp.dhis.integration.sdk.api.Dhis2Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Servlet;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
@EnableConfigurationProperties( Dhis2Properties.class )
public class Application
{
    @Autowired
    private Dhis2Properties dhis2Properties;

    public static void main( String[] args )
    {
        SpringApplication.run( Application.class, args );
    }

    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplateBuilder().defaultMessageConverters()
            .defaultHeader( "Content-Type", MediaType.APPLICATION_JSON_VALUE )
            .defaultHeader( "Accept", MediaType.APPLICATION_JSON_VALUE )
            .basicAuthentication( dhis2Properties.getUsername(),
                dhis2Properties.getPassword(), StandardCharsets.UTF_8 )
            .build();
    }

    @Bean
    public Dhis2Client dhis2Client()
    {
        return Dhis2ClientBuilder.newClient( "https://play.dhis2.org/dev/api", "system", "System123" ).build();
    }
}
