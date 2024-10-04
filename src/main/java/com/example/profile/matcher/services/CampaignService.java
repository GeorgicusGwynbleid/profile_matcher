package com.example.profile.matcher.services;

import com.example.profile.matcher.entities.Campaign;
import com.example.profile.matcher.entities.Matchers;
import com.example.profile.matcher.entities.Profile;
import com.example.profile.matcher.repositories.CampaignRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CampaignService {
    private static final Log LOG = LogFactory.getLog(CampaignService.class);

    @Autowired
    private CampaignRepository campaignRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();


    @PostConstruct
    public void init(){
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    public Campaign getCampaignById(String id) {
        return campaignRepository.findById(id).orElse(null);
    }

    public boolean doesProfileMatchCampaign(Profile userProfile, Campaign campaign) {
        Matchers campaignMatchers = campaign.getMatchers();
        if (userProfile.getLevel() >= campaignMatchers.getLevel().getMin() && userProfile.getLevel() <= campaignMatchers.getLevel().getMax()){
            LOG.info(String.format("Player level %s matches campaign ",userProfile.getLevel()));
        } else {
            return false;
        }
        Map<String,Object> campaignHasFields = campaignMatchers.getHas();
        for (Map.Entry<String,Object> campaignField: campaignHasFields.entrySet()){
            LOG.info(String.format("Checking player profile for %s for with value in [%s] ",campaignField.getKey(),campaignField.getValue()));
            if (!checkField(userProfile,campaignField.getKey(),campaignField.getValue())){
                return false;
            }
        }
        Map<String,Object> campaignDoesNotHaveFields = campaignMatchers.getDoesNotHave();
        for (Map.Entry<String,Object> campaignField: campaignDoesNotHaveFields.entrySet()){
            LOG.info(String.format("Checking player profile for %s for with value not in [%s] ",campaignField.getKey(),campaignField.getValue()));
            if (checkField(userProfile,campaignField.getKey(),campaignField.getValue())){
                return false;
            }
        }
        return true;
    }

    private boolean checkField(Profile playerProfile, String fieldName, Object expectedValue){
        try {
            Field field;
            LOG.info(expectedValue + " vs "  + fieldName);

            if (fieldName.equals("items")){
                field = Profile.class.getDeclaredField("inventory");
                field.setAccessible(true);
                Map<String,Integer> fieldValue = (Map<String, Integer>) field.get(playerProfile);
                boolean result = false;
                if (expectedValue instanceof List){
                    for (String key : (List<String>)expectedValue){
                        if (fieldValue.containsKey(key)){
                            result = true;
                            break;
                        }
                    }
                } else {
                    result = fieldValue.containsKey(expectedValue);
                }
                return result;
            }
            else{
                field = Profile.class.getDeclaredField(fieldName);
            }
            field.setAccessible(true);
            Object fieldValue = field.get(playerProfile);
            LOG.info(expectedValue + " vs "  + fieldValue);
            if (expectedValue instanceof List){
                return ((List<?>) expectedValue).contains(fieldValue);
            } else {
                return expectedValue.equals(fieldValue);
            }
        }catch (NoSuchFieldException  | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean isCampaignStillActive(Campaign campaign){
        if (!campaign.isEnabled()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = campaign.getStartDate().toLocalDateTime();
        LocalDateTime endDate = campaign.getEndDate().toLocalDateTime();

        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    public boolean matchesCampaign(Profile profile, Campaign campaign) {
        JsonNode profileJsonNode = objectMapper.valueToTree(profile);
        JsonNode campaignJsonNode = objectMapper.valueToTree(campaign);

        JsonNode matchers = campaignJsonNode.get("matchers");

        for (Iterator<Map.Entry<String, JsonNode>> it = matchers.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            switch (key) {
                case "level":
                    if (!checkLevelMatcher(profileJsonNode.get("level").asInt(), value)) {
                        return false;
                    }
                    break;
                case "has":
                    if (!checkHasMatcher(profileJsonNode, value)) {
                        return false;
                    }
                    break;
                case "doesNotHave":
                    if (!checkDoesNotHaveMatcher(profileJsonNode, value)) {
                        return false;
                    }
                    break;
                default:
                    if (!checkCustomMatcher(profileJsonNode, key, value)) {
                        return false;
                    }
            }
        }
        return true;
    }

    private boolean checkLevelMatcher(int userLevel, JsonNode levelMatcher) {
        int min = levelMatcher.get("min").asInt();
        int max = levelMatcher.get("max").asInt();
        return userLevel >= min && userLevel <= max;
    }

    private boolean checkHasMatcher(JsonNode userProfile, JsonNode campaignHasMatcher) {
        for (Iterator<Map.Entry<String, JsonNode>> it = campaignHasMatcher.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            if (key.equals("items")) {
                if (!checkItems(userProfile.get("inventory"), value)) {
                    return false;
                }
            } else if (value.isArray()) {
                if (!value.toString().contains(userProfile.get(key).asText())) {
                    return false;
                }
            } else {
                if (!userProfile.has(key) || !userProfile.get(key).equals(value)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkDoesNotHaveMatcher(JsonNode userProfile, JsonNode campaignDoesNotHaveMatcher) {
        for (Iterator<Map.Entry<String, JsonNode>> it = campaignDoesNotHaveMatcher.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            if (key.equals("items")) {
                if (checkItems(userProfile.get("inventory"), value)) {
                    return false;
                }
            } else if (value.isArray()) {
                if (value.toString().contains(userProfile.get(key).asText())) {
                    return false;
                }
            } else {
                if (userProfile.has(key) && userProfile.get(key).equals(value)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkItems(JsonNode profileInventory, JsonNode itemsRequiredByCampaign) {
        for (JsonNode item : itemsRequiredByCampaign) {
            if (!profileInventory.has(item.asText()) || profileInventory.get(item.asText()).asInt() == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean checkCustomMatcher(JsonNode userProfile, String key, JsonNode value) {
        if (!userProfile.has(key)) {
            return false;
        }

        JsonNode userValue = userProfile.get(key);

        if (value.isObject()) {
            if (value.has("min") && value.has("max")) {
                if (userValue.isNumber()) {
                    double userNum = userValue.asDouble();
                    return userNum >= value.get("min").asDouble() && userNum <= value.get("max").asDouble();
                } else if (userValue.isTextual()) {
                    LocalDateTime userDate = LocalDateTime.parse(userValue.asText());
                    LocalDateTime minDate = LocalDateTime.parse(value.get("min").asText());
                    LocalDateTime maxDate = LocalDateTime.parse(value.get("max").asText());
                    return !userDate.isBefore(minDate) && !userDate.isAfter(maxDate);
                }
            }
        } else if (value.isArray()) {
            for (JsonNode arrayValue : value) {
                if (userValue.equals(arrayValue)) {
                    return true;
                }
            }
            return false;
        } else {
            return userValue.equals(value);
        }

        return false;
    }
}
