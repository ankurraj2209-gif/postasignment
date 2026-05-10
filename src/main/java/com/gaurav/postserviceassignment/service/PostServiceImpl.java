package com.gaurav.postserviceassignment.service;

import com.gaurav.postserviceassignment.dto.CreateCommentDto;
import com.gaurav.postserviceassignment.dto.CreatePostRequestDto;
import com.gaurav.postserviceassignment.dto.LikePostDto;
import com.gaurav.postserviceassignment.entity.AuthorType;
import com.gaurav.postserviceassignment.entity.Comment;
import com.gaurav.postserviceassignment.entity.Post;
import com.gaurav.postserviceassignment.repository.BotRepository;
import com.gaurav.postserviceassignment.repository.CommentRepository;
import com.gaurav.postserviceassignment.repository.PostRepository;
import com.gaurav.postserviceassignment.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional

public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final BotRepository botRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final GuardrailService guardrailService;
    private final NotificationService notificationService;
    private final ViralityService viralityService;



    @Override
    public Post createPost(CreatePostRequestDto createPostRequest) {
        validateAuthor(
                createPostRequest.getAuthorId(),
                createPostRequest.getAuthorType());
        Post post = new Post();
        post.setAuthorId(createPostRequest.getAuthorId());
        post.setAuthorType(createPostRequest.getAuthorType());
        post.setContent(createPostRequest.getContent());
        return postRepository.save(post);
    }
    @Override
    public String likePost(Long postId,LikePostDto likePostDto) {
        postRepository.findById(postId)
                .orElseThrow(()->new RuntimeException("post not found"));
        validateAuthor(likePostDto.getAuthorId(),likePostDto.getAuthorType());
        viralityService.updateViralityScore(postId,likePostDto.getAuthorType(),InteractionType.LIKE);
        return "Post liked successfully";
    }

    @Override
    public void addComment(Long postId, CreateCommentDto createCommentDto) {
       Post post= postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("post not found"));
        validateAuthor(createCommentDto.getAuthorId(), createCommentDto.getAuthorType());


        Comment comment = new Comment();
        comment.setAuthorId(createCommentDto.getAuthorId());
        comment.setAuthorType(createCommentDto.getAuthorType());
        comment.setContent(createCommentDto.getContent());
        comment.setPostId(postId);
        if (createCommentDto.getParentCommentId() == null) {
            if(createCommentDto.getAuthorType() ==AuthorType.BOT &&
                    post.getAuthorType()==AuthorType.USER){
                guardrailService.validateCoolDownCaplCap(createCommentDto.getAuthorId(),
                        post.getAuthorId());

            }
            comment.setCommentDepth(1);
        } else {
            Comment parent =
                    commentRepository.findById(
                                    createCommentDto.getParentCommentId())
                            .orElseThrow(() ->
                                    new RuntimeException("Parent comment not found"));


        comment.setParentCommentId(parent.getCommentId());
        comment.setCommentDepth(parent.getCommentDepth() + 1);
        guardrailService.validateDepth(parent.getCommentDepth());
    }
        if(createCommentDto.getAuthorType() ==AuthorType.BOT){
            guardrailService.validateHorizonalCap(postId);
            notificationService.handleBotInteraction(
                    createCommentDto.getContent(),post.getAuthorId());
        }

            Comment saved = commentRepository.save(comment);
        viralityService.updateViralityScore(postId,createCommentDto.getAuthorType(),InteractionType.COMMENT);


    }

    private void validateAuthor(Long authorId, AuthorType authorType) {
        if (authorId == null) {
            throw new IllegalArgumentException("Author Id cannot be null");
        }
        if (authorType == null) {
            throw new IllegalArgumentException("Author Type cannot be null");
        }
        /*if(authorType==AuthorType.BOT){
            botRepository.findById(authorId)
                    .orElseThrow(()->new RuntimeException("Bot NOT FOUND"));
        }
        else{
            userRepository.findById(authorId)
                    .orElseThrow(()->new RuntimeException("USER NOT FOUND"));
        }*/
    }

}
