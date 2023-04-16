package com.lypaka.betterlures.Lures;

import java.util.Map;

public class ComplicatedSpeciesMode {

    private final Map<String, Map<String, Map<String, Double>>> speciesMap;

    public ComplicatedSpeciesMode (Map<String, Map<String, Map<String, Double>>> speciesMap) {

        this.speciesMap = speciesMap;

    }

    public Map<String, Map<String, Map<String, Double>>> getSpeciesMap() {

        return this.speciesMap;

    }

}
