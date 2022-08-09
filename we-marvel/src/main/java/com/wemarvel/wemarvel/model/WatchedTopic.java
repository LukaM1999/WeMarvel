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
public class WatchedTopic {

    @Id
    @GeneratedValue
    @Getter
    public Long id;

    @Getter
    public String username;

    @Getter
    public Long topicId;
}
