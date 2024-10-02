package com.example.profile.matcher.services;

import com.example.profile.matcher.entities.Campaign;
import com.example.profile.matcher.entities.Matchers;
import com.example.profile.matcher.entities.Profile;
import com.example.profile.matcher.repositories.ProfileRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class ProfileService {
    private static final Log LOG = LogFactory.getLog(ProfileService.class);
    @Autowired
    private CampaignService campaignService;
    @Autowired
    private ProfileRepository profileRepository;
    public Profile getProfileById(String playerId){
        Profile userProfile = profileRepository.findByPlayerId(playerId);
        List<Campaign> availableCampaigns = campaignService.getAllCampaigns();
        LOG.info(String.format("Found %d campaigns: ", availableCampaigns.size()));
        availableCampaigns.stream().forEach(campaign -> {
            Matchers campaignMatchers = campaign.getMatchers();
            if (userProfile.getLevel() >= campaignMatchers.getLevel().getMin() && userProfile.getLevel() <= campaignMatchers.getLevel().getMax()){
                LOG.info(String.format("Player level %s matches campaign ",userProfile.getLevel()));
            } else {
                return;
            }
            Map<String,Object> campaignHasFields = campaignMatchers.getHas();
            for (Map.Entry<String,Object> campaignField: campaignHasFields.entrySet()){
                LOG.info(String.format("Checking player profile for %s for with value in [%s] ",campaignField.getKey(),campaignField.getValue()));
                if (!checkField(userProfile,campaignField.getKey(),campaignField.getValue())){
                    return;
                }
            }
            Map<String,Object> campaignDoesNotHaveFields = campaignMatchers.getDoesNotHave();
            for (Map.Entry<String,Object> campaignField: campaignDoesNotHaveFields.entrySet()){
                LOG.info(String.format("Checking player profile for %s for with value not in [%s] ",campaignField.getKey(),campaignField.getValue()));
                if (checkField(userProfile,campaignField.getKey(),campaignField.getValue())){
                    return;
                }
            }
            // All conditions passed, therefore the campaign matches the player profile
            userProfile.getActiveCampaigns().add(campaign.getName());
            LOG.info("Updated user profile " + userProfile);
        });

        return userProfile;
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
}
