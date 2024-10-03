package com.example.profile.matcher.services;

import com.example.profile.matcher.entities.Campaign;
import com.example.profile.matcher.entities.Profile;
import com.example.profile.matcher.repositories.ProfileRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    private static final Log LOG = LogFactory.getLog(ProfileService.class);
    @Autowired
    private CampaignService campaignService;
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Profile getProfileById(String playerId){
        Optional<Profile> userProfileSearch = profileRepository.findByPlayerId(playerId);
        if (userProfileSearch.isEmpty()){
            LOG.info(String.format("User %s not found ! ", playerId));
            return new Profile();
        }
        Profile userProfile = userProfileSearch.get();
        List<Campaign> availableCampaigns = campaignService.getAllCampaigns();
        LOG.info(String.format("Found %d campaigns: ", availableCampaigns.size()));
        availableCampaigns.stream().forEach(campaign -> {
            if (campaignService.isCampaignStillActive(campaign) &&
//                    campaignService.doesProfileMatchCampaign(userProfile,campaign)) {
                    campaignService.matchesCampaign(userProfile,campaign)) {
                userProfile.getActiveCampaigns().add(campaign.getName());
                LOG.info("Updated user profile " + userProfile);
                updateProfileField(playerId, "active_campaigns", userProfile.getActiveCampaigns());
            }
        });

        return userProfile;
    }


    private void updateProfileField(String playerId, String fieldName,Object newValue){

        Query query = new Query(Criteria.where("player_id").is(playerId));
        Update update = new Update();
        update.set(fieldName, newValue);

        mongoTemplate.updateFirst(query, update, Profile.class);

    }
}
