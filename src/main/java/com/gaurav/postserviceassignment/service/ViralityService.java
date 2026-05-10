package com.gaurav.postserviceassignment.service;

import com.gaurav.postserviceassignment.entity.AuthorType;

public interface ViralityService {
    public void updateViralityScore(Long postId, AuthorType authorType,InteractionType interactionType);
}
