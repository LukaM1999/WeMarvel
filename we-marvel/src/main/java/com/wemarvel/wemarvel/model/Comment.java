package com.wemarvel.wemarvel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    private Long userId;

    @Getter
    private Long postId;

    @Getter
    private Long quotedCommentId;

    @Getter
    private String comment;
}
