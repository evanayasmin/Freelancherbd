package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.repository.UserTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTrackService {

    @Autowired
    UserTrackRepository userTrackRepository;

    public UserTrackService(UserTrackRepository userTrackRepository) {
        this.userTrackRepository = userTrackRepository;
    }


}
