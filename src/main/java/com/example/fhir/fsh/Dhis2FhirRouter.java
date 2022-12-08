/*
 * Copyright (c) 2004-2022, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.example.fhir.fsh;

import java.net.URI;

import lombok.AllArgsConstructor;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.fhir.fsh.domain.dhis2.OptionSets;

@Component
@AllArgsConstructor
public class Dhis2FhirRouter extends RouteBuilder
{
    private final Dhis2Properties dhis2Properties;

    private final RestTemplate restTemplate;

    @Override
    public void configure()
        throws Exception
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
            .process( "dhis2Cleanup" )
            .to( "mustache:fsh/fsh-optionSets.mustache" )
            .to( "file:output?fileName=optionSets.fsh" );
    }
}
