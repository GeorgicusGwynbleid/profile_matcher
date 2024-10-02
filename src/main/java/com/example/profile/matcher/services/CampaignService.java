package com.example.profile.matcher.services;

import com.example.profile.matcher.entities.Campaign;
import com.example.profile.matcher.repositories.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignService {
    @Autowired
    private CampaignRepository campaignRepository;
    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    public Campaign getCampaignById(String id) {
        return campaignRepository.findById(id).orElse(null);
    }

    public Campaign createOrUpdateCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }
}
