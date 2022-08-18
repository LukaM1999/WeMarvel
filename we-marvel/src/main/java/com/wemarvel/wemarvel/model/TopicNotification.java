package com.wemarvel.wemarvel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TopicNotification extends Notification {
    @Getter
    @Setter
    private Long boardId;
    @Getter
    @Setter
    private Long topicId;
    @Getter
    @Setter
    private String topicTitle;
    @Getter
    @Setter
    private String posterUsername;

    public TopicNotification(String type, String recipientUsername, LocalDateTime receivedAt,
                             Long boardId, Long topicId, String topicTitle, String posterUsername) {
        super(type, recipientUsername, receivedAt);
        this.boardId = boardId;
        this.topicId = topicId;
        this.topicTitle = topicTitle;
        this.posterUsername = posterUsername;
    }
}
