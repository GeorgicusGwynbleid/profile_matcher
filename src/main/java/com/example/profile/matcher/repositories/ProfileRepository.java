package com.example.profile.matcher.repositories;

import com.example.profile.matcher.entities.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<Profile, String> {
    Profile findByPlayerId(String playerId);
}
