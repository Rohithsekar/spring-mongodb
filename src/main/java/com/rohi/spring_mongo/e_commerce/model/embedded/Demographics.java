package com.rohi.spring_mongo.e_commerce.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Demographics implements Serializable {
    private java.util.List<String> topCountries;
    private java.util.List<String> topStates;
    private java.util.Map<String, Integer> ageGroups;
    private java.util.Map<String, Integer> genderSplit;
}