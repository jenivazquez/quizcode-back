package com.quizcode.module.user.infrastructure.mongo.persistence;

import com.quizcode.module.user.infrastructure.mongo.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.Optional;

public interface UserMongoRepository extends MongoRepository<UserDocument, String> {

    Optional<UserDocument> findByEmail(String email);

    // value: busca por _id usando ?0 (primer parámetro del metodo) | fields: { 'password': 0 } excluye el campo password
    @Query(value = "{ '_id': ?0 }", fields = "{ 'password': 0 }")
    Optional<UserDocument> findByIdWithoutPassword(String id);

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'active': ?1 } }")
    void updateStatus(String id, Boolean active);
}

