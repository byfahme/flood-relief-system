package com.kmct.floodrelief.matching;

import java.util.Map;

/**
 * One camp's position for a matching run.
 * surplus: item name -> quantity the camp holds beyond its needs
 * shortage: item name -> quantity the camp still lacks
 */
public record CampStock(String campName, Map<String, Integer> surplus, Map<String, Integer> shortage) {
}