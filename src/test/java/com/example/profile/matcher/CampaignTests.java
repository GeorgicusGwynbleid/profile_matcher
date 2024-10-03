package com.example.profile.matcher;

import com.example.profile.matcher.entities.Campaign;
import com.example.profile.matcher.entities.Level;
import com.example.profile.matcher.entities.Matchers;
import com.example.profile.matcher.entities.Profile;
import com.example.profile.matcher.repositories.CampaignRepository;
import com.example.profile.matcher.services.CampaignService;
import com.example.profile.matcher.services.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CampaignTests {

    @Mock
    private ProfileService profileService;

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private CampaignService campaignService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoesUserMatchCampaign_WhenUserProfileMatches() {
        Profile userProfile = new Profile();
        userProfile.setLevel(5);
        userProfile.setPlayerId("Ulise");
        userProfile.setTotalPlaytime(200);
        userProfile.setLanguage("DE");
        List<Campaign> campaigns = getCampaigns();
        when(profileService.getProfileById("Ulise")).thenReturn(userProfile);
        when(campaignRepository.findCampaignsByName("MegaPromo")).thenReturn(campaigns);

        boolean result = campaignService.doesProfileMatchCampaign(userProfile, campaigns.get(0));
        assertTrue(result);
    }

    private static List<Campaign> getCampaigns() {
        Campaign campaign = new Campaign();
        campaign.setName("MegaPromo");
        Matchers matchers = new Matchers();
        Level campaignLevel = new Level();
        campaignLevel.setMax(8);
        campaignLevel.setMin(4);
        matchers.setLevel(campaignLevel);
        Map<String,Object> campaignHas = new HashMap<>();
        campaignHas.put("language","DE");
        matchers.setHas(campaignHas);
        campaign.setMatchers(matchers);
        // Mock repository responses
        List<Campaign> campaigns = new ArrayList<>();
        campaigns.add(campaign);
        return campaigns;
    }

    @Test
    void testDoesUserMatchCampaign_WhenUserProfileDoesNotMatch() {
        Profile userProfile = new Profile();
        //wrong level
        userProfile.setLevel(2);
        userProfile.setPlayerId("Ulise");
        userProfile.setTotalPlaytime(200);
        userProfile.setLanguage("DE");
        List<Campaign> campaigns = getCampaigns();
        when(profileService.getProfileById("Ulise")).thenReturn(userProfile);
        when(campaignRepository.findCampaignsByName("MegaPromo")).thenReturn(campaigns);

        boolean result = campaignService.doesProfileMatchCampaign(userProfile, campaigns.get(0));

        assertFalse(result);
    }
}

