package com.example.fhir.fsh;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@Valid
@ConfigurationProperties( prefix = "dhis2" )
public class Dhis2Properties
{
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String baseUrl;
}
