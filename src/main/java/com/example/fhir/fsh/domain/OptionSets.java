package com.example.fhir.fsh.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OptionSets
{
    private List<OptionSet> optionSets = new ArrayList<>();
}
