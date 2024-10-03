package com.example.profile.matcher.repositories;

import com.example.profile.matcher.entities.Campaign;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CampaignRepository  extends MongoRepository<Campaign, String> {

    @Query("{ 'name' : ?0 }")
    List<Campaign> findCampaignsByName(String name);
}
