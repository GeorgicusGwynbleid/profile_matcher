package com.example.profile.matcher.controllers;

import com.example.profile.matcher.entities.Profile;
import com.example.profile.matcher.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ProfileController {

    @Autowired
    ProfileService profileService;
    @GetMapping("/get_client_config/{player_id}")
    public Profile getProfile(@PathVariable String player_id){
        Profile profile = profileService.getProfileById(player_id);
        return profile;
    }
}
