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
    @Setter
    private Long ownerId;

    @Getter
    @Setter
    private Long firstPostId;

    @Getter
    @Setter
    private Long boardId;

    @Getter
    @Setter
    private Long marvelEntityId;

    @Getter
    @Setter
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
