package com.quizcode.module.quiz.infrastructure.mongo.document;

import com.quizcode.module.quiz.domain.entity.QuizStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "quizzes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDocument {
    @Id
    private String id;
    @NotNull
    private String ownerId;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private Boolean hasLimit;
    @Field(write = Field.Write.ALWAYS)
    private Integer limitMinutes;
    @NotNull
    private QuizStatus status;
    @NotNull
    private Instant createdAt;
    @NotNull
    private Instant updatedAt;
}
