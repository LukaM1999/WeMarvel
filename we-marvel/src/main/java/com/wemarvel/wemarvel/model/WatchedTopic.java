package com.wemarvel.wemarvel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"topic_id", "user_id"}))
public class WatchedTopic {

    @Id
    @SequenceGenerator(name = "watchedTopicIdGen", sequenceName = "watchedTopicIdSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "watchedTopicIdGen")
    @Getter
    public Long id;

    @Getter
    @Column(name = "user_id")
    public Long userId;

    @Getter
    @Column(name = "topic_id")
    public Long topicId;
}
