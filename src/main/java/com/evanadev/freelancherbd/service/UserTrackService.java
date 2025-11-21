package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.TrafficType;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.model.UserTrac;
import com.evanadev.freelancherbd.repository.UserTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserTrackService {

    @Autowired
    UserTrackRepository userTrackRepository;


    public List<UserTrac> findSavedFreelancer(TrafficType trafficType, Long userId){

        return userTrackRepository.findByLoggedId(userId, trafficType);
    }

}
