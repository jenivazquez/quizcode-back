package com.quizcode.module.room.infrastructure.mongo.document;

import com.quizcode.module.room.domain.entity.RoomStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDocument {
    @Id
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @Field(write = Field.Write.ALWAYS)
    private String code;
    @NotNull
    private RoomStatus status;
    @NotNull
    private String quizId;
    @NotNull
    private Instant createdAt;
    @Field(write = Field.Write.ALWAYS)
    private Instant startedAt;
    @Field(write = Field.Write.ALWAYS)
    private Instant finishedAt;
    @NotNull
    private Boolean reviewed;
}
