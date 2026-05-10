package com.gaurav.postserviceassignment.dto;

import com.gaurav.postserviceassignment.entity.AuthorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequestDto {
    private Long authorId;
    private AuthorType authorType;
    private String content;
}
