package com.example.profile.matcher.entities;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "profile")
public class Profile {
    @Field("player_id")
    private String playerId;

    private String credential;
    private ZonedDateTime created;
    private ZonedDateTime modified;
    @Field("last_session")
    private ZonedDateTime lastSession;

    private int totalSpent;
    private int totalRefund;
    private int totalTransactions;
    @Field("last_purchase")
    private ZonedDateTime lastPurchase;
    @Field("active_campaigns")
    private List<String> activeCampaigns;
    private List<Device> devices;
    private int level;
    private int xp;
    private int totalPlaytime;
    private String country;
    private String language;
    private ZonedDateTime birthdate;
    private String gender;
    private Map<String,Integer> inventory;
    private Clan clan;

    @Field("_customfield")
    private String customField;


    public static class Device {
        private int id;
        private String model;
        private String carrier;
        private String firmware;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getCarrier() {
            return carrier;
        }

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }

        public String getFirmware() {
            return firmware;
        }

        public void setFirmware(String firmware) {
            this.firmware = firmware;
        }
    }

    public static class Clan {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    public void setModified(ZonedDateTime modified) {
        this.modified = modified;
    }

    public ZonedDateTime getLastSession() {
        return lastSession;
    }

    public void setLastSession(ZonedDateTime lastSession) {
        this.lastSession = lastSession;
    }

    public int getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(int totalSpent) {
        this.totalSpent = totalSpent;
    }

    public int getTotalRefund() {
        return totalRefund;
    }

    public void setTotalRefund(int totalRefund) {
        this.totalRefund = totalRefund;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public ZonedDateTime getLastPurchase() {
        return lastPurchase;
    }

    public void setLastPurchase(ZonedDateTime lastPurchase) {
        this.lastPurchase = lastPurchase;
    }

    public List<String> getActiveCampaigns() {
        return activeCampaigns;
    }

    public void setActiveCampaigns(List<String> activeCampaigns) {
        this.activeCampaigns = activeCampaigns;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getTotalPlaytime() {
        return totalPlaytime;
    }

    public void setTotalPlaytime(int totalPlaytime) {
        this.totalPlaytime = totalPlaytime;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ZonedDateTime getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(ZonedDateTime birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    public Clan getClan() {
        return clan;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }

    public String getCustomField() {
        return customField;
    }

    public void setCustomField(String customField) {
        this.customField = customField;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "playerId='" + playerId + '\'' +
                ", credential='" + credential + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", lastSession=" + lastSession +
                ", totalSpent=" + totalSpent +
                ", totalRefund=" + totalRefund +
                ", totalTransactions=" + totalTransactions +
                ", lastPurchase=" + lastPurchase +
                ", devices=" + devices +
                ", level=" + level +
                ", xp=" + xp +
                ", totalPlaytime=" + totalPlaytime +
                ", country='" + country + '\'' +
                ", language='" + language + '\'' +
                ", birthdate=" + birthdate +
                ", gender='" + gender + '\'' +
                ", inventory=" + inventory +
                ", activeCampaigns=" + activeCampaigns +
                ", clan=" + clan +
                ", customField='" + customField + '\'' +
                '}';
    }
}
