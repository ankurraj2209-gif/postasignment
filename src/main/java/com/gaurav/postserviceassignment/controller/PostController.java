package com.gaurav.postserviceassignment.controller;

import com.gaurav.postserviceassignment.dto.CreateCommentDto;
import com.gaurav.postserviceassignment.dto.CreatePostRequestDto;
import com.gaurav.postserviceassignment.dto.LikePostDto;
import com.gaurav.postserviceassignment.entity.Comment;
import com.gaurav.postserviceassignment.entity.Post;
import com.gaurav.postserviceassignment.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody CreatePostRequestDto
                                                         createPostRequest) {

        return ResponseEntity.ok(postService.createPost(createPostRequest));
    }

   @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable("postId") Long postId,
                                           @RequestBody LikePostDto likePostDto) {
      postService.likePost(postId,likePostDto);
      return ResponseEntity.ok("Post liked successfully");
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<String> addComment(@PathVariable Long postId,
                                              @RequestBody CreateCommentDto createCommentDto) {
      postService.addComment(postId,createCommentDto);
       return ResponseEntity.ok("comment  added successfully");
    }

 }
