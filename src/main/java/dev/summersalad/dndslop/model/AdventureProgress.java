package dev.summersalad.dndslop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AdventureProgress {
    private String progression;
    private Stats stats;
    private List<String> choices;

    @Data
    public static class Stats {
        private int turn;
        private int level;
        private int xp;
        @JsonProperty("hp_current")
        private int hpCurrent;
        @JsonProperty("hp_max")
        private int hpMax;
        private int gold;
        private String sword;
        private String armor;
        private List<String> inventory;
        private List<String> effects;
    }
}