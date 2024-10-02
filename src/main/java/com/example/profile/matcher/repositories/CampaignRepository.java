package com.example.profile.matcher.repositories;

import com.example.profile.matcher.entities.Campaign;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CampaignRepository  extends MongoRepository<Campaign, String> {
}
