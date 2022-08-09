package com.wemarvel.wemarvel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    private String ownerUsername;

    @Getter
    private Long topicId;

    @Getter
    @Setter
    @Column(length = 20000)
    private String content;

    @Getter
    @Setter
    private LocalDateTime createdAt;

    @Getter
    @Setter
    private LocalDateTime modifiedAt;

    @Getter
    @Setter
    private String modifiedByUsername;

    @Getter
    @Setter
    @Column(columnDefinition = "BigInt default 0")
    private int modifications;

    @Getter
    @Setter
    @Column(columnDefinition = "Boolean default false")
    private boolean deleted;
}
