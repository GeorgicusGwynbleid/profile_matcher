package com.example.profile.matcher.entities;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matchers {

    private Level level;
    @Field("has")
    private Map<String,Object> has = new HashMap<>();

    @Field("does_not_have")
    private Map<String,Object> doesNotHave = new HashMap<>();

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Map<String,Object> getHas() {
        return has;
    }

    public void setHas(Map<String,Object> has) {
        this.has = has;
    }

    public Map<String,Object> getDoesNotHave() {
        return doesNotHave;
    }

    public void setDoesNotHave(Map<String,Object> doesNotHave) {
        this.doesNotHave = doesNotHave;
    }

    @Override
    public String toString() {
        return "Matchers{" +
                "level=" + level +
                ", has=" + has +
                ", doesNotHave=" + doesNotHave +
                '}';
    }
}
