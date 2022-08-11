package com.wemarvel.wemarvel.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Topic {

    @Id
    @SequenceGenerator(name = "topicIdGen", sequenceName = "topicIdSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topicIdGen")
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
