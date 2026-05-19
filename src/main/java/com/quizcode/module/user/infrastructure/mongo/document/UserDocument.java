package com.quizcode.module.user.infrastructure.mongo.document;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDocument {
    @Id
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String surname1;
    @NotNull
    private String surname2;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private Boolean active;
}
