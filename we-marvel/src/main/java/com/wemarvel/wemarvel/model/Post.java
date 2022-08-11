package com.wemarvel.wemarvel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @SequenceGenerator(name = "postIdGen", sequenceName = "postIdSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postIdGen")
    @Getter
    private Long id;

    @Getter
    private String ownerUsername;

    @Getter
    private Long topicId;

    @Getter
    @Setter
    private Long quotedPostId;

    @Getter
    @Setter
    @Column(length = 20000)
    private String content;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Europe/Belgrade")
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
