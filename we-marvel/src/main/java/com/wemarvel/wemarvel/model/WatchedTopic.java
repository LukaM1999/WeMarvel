package com.wemarvel.wemarvel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WatchedTopic {

    @Id
    @SequenceGenerator(name = "watchedTopicIdGen", sequenceName = "watchedTopicIdSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "watchedTopicIdGen")
    @Getter
    public Long id;

    @Getter
    public Long userId;

    @Getter
    public Long topicId;
}
