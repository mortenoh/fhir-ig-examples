package com.example.fhir.fsh;

import com.example.fhir.fsh.domain.dhis2.Option;
import com.example.fhir.fsh.domain.dhis2.OptionSets;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class Dhis2Cleanup implements Processor
{
    @Override
    public void process( Exchange exchange ) throws Exception
    {
        OptionSets optionSets = (OptionSets) exchange.getIn().getBody();

        Objects.requireNonNull( optionSets ).getOptionSets().forEach( os -> {
            Map<String, Option> options = new HashMap<>();

            for ( Option option : os.getOptions() )
            {
                if ( option == null ) continue;
                options.put( option.getId(), option );
            }

            os.setOptions( new ArrayList<>( options.values() ) );
        } );
    }
}
