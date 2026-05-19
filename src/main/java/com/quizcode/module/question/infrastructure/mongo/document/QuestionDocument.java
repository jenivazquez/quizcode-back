package com.quizcode.module.question.infrastructure.mongo.document;

import com.quizcode.module.question.domain.entity.type.QuestionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDocument {
    @Id
    private String id;
    @NotNull
    private String statement;
    @Field(write = Field.Write.ALWAYS)
    private String baseCode;
    @NotNull
    private QuestionType type;
    @NotNull
    private Integer order;
    @NotNull
    private Integer score;
    @NotNull
    private String quizId;
    @Field(write = Field.Write.ALWAYS)
    private List<OptionDocument> options;
}
