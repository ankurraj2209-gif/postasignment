package com.gaurav.postserviceassignment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long CommentId;

    private Long postId;

    private Long parentCommentId;

    private Long authorId;

    private String content;

    @Enumerated(EnumType.STRING)
    private AuthorType authorType;

    private Integer commentDepth;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
