package com.gaurav.postserviceassignment.dto;

import com.gaurav.postserviceassignment.entity.AuthorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentDto {

    private Long parentCommentId;
    private Long authorId;
    private String content;
    private AuthorType authorType;
}
