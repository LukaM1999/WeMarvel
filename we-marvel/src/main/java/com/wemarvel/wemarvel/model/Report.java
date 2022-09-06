package com.wemarvel.wemarvel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @SequenceGenerator(name = "reportIdGen", sequenceName = "reportIdSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reportIdGen")
    @Getter
    private Long id;

    @Getter
    @Setter
    private Long reportingUserId;

    @Getter
    @Setter
    private Long reportedUserId;

    @Getter
    @Setter
    private Long postId;

    @Getter
    @Setter
    private String explanation;

    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm", timezone = "Europe/Belgrade")
    @Getter
    @Setter
    private LocalDateTime sentAt;

    @Getter
    @Setter
    @Column(columnDefinition = "Boolean default false")
    private boolean resolved;
}
