package com.svincent7.sentra.policy.dto;

import lombok.Data;

import java.util.Map;

@Data
public class Condition {
    private Map<String, Map<String, Object>> conditionsMap;
}
