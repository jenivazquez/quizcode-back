package com.quizcode.module.participation.infrastructure.mongo.document;

import com.quizcode.module.participation.domain.entity.status.ParticipationStatus;
import com.quizcode.module.participation.domain.entity.status.ReviewStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Document(collection = "participations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationDocument {
    @Id
    private String id;
    @NotNull
    private String roomId;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private ParticipationStatus status;
    @Field(write = Field.Write.ALWAYS)
    private ReviewStatus reviewStatus;
    @NotNull
    private Instant startedAt;
    @Field(write = Field.Write.ALWAYS)
    private Instant finishedAt;
    @Field(write = Field.Write.ALWAYS)
    private Integer totalScore;
    @Field(write = Field.Write.ALWAYS)
    private Long totalTime;
    @Field(write = Field.Write.ALWAYS)
    private List<AnswerDocument> answers;
}
