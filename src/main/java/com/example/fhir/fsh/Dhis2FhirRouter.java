package com.example.fhir.fsh;

import com.example.fhir.fsh.domain.dhis2.OptionSets;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@AllArgsConstructor
public class Dhis2FhirRouter extends RouteBuilder
{
    private final Dhis2Properties dhis2Properties;

    private final RestTemplate restTemplate;

    @Override
    public void configure() throws Exception
    {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .uri( URI.create( dhis2Properties.getBaseUrl() ) )
            .path( "/api/optionSets" )
            .queryParam( "paging", false )
            .queryParam( "fields", "id,code,name,description,options[id,code,name,description]" )
            .build()
            .encode();

        ResponseEntity<OptionSets> response = restTemplate.getForEntity( uriComponents.toUri(),
            OptionSets.class );

        from( "timer:foo?repeatCount=1" )
            .process( x -> x.getIn().setBody( response.getBody() ) )
            .process("dhis2Cleanup")
            .to( "mustache:fsh/fsh-optionSets.mustache" )
            .to( "file:output?fileName=optionSets.fsh" );
    }
}
