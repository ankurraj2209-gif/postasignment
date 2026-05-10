package com.gaurav.postserviceassignment.service;

import com.gaurav.postserviceassignment.dto.CreateCommentDto;
import com.gaurav.postserviceassignment.dto.CreatePostRequestDto;
import com.gaurav.postserviceassignment.dto.LikePostDto;
import com.gaurav.postserviceassignment.entity.Comment;
import com.gaurav.postserviceassignment.entity.Post;

public interface PostService {
    public Post createPost(CreatePostRequestDto createPostRequest);

    public String likePost(Long postId,LikePostDto likePostDto);

    public void addComment (Long postId, CreateCommentDto createPostRequestDto);

}
