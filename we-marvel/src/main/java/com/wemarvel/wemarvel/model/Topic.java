package com.wemarvel.wemarvel.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Topic {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    private String ownerUsername;

    @Getter
    private Long firstPostId;

    @Getter
    private Long boardId;

    @Getter
    private Long marvelEntityId;

    @Getter
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @Column(nullable = false)
    private String title;

    @Getter
    @Setter
    @Column(columnDefinition = "Boolean default false")
    private boolean sticky;

    @Getter
    @Setter
    @Column(columnDefinition = "Boolean default false")
    private boolean locked;
}
