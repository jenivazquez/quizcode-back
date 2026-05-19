package com.quizcode.module.user.infrastructure.mongo;

import com.quizcode.module.user.domain.UserRepository;
import com.quizcode.module.user.domain.entity.User;
import com.quizcode.module.user.infrastructure.mongo.document.UserDocument;
import com.quizcode.module.user.infrastructure.mongo.mapper.UserMongoMapper;
import com.quizcode.module.user.infrastructure.mongo.persistence.UserMongoRepository;
import com.quizcode.shared.Util;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserMongoRepository userMongoRepository;
    private final UserMongoMapper userMongoMapper;
    private final MongoTemplate mongoTemplate;

    public UserRepositoryImpl(UserMongoRepository userMongoRepository, UserMongoMapper userMongoMapper, MongoTemplate mongoTemplate) {
        this.userMongoRepository = userMongoRepository;
        this.userMongoMapper = userMongoMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void create(User user) {
        UserDocument userDoc = userMongoMapper.userToUserDocument(user);
        userMongoRepository.insert(userDoc);
    }

    @Override
    public Optional<User> findById(String id) {
        return this.userMongoRepository.findByIdWithoutPassword(id).map(userMongoMapper::userDocumentToUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userMongoRepository.findByEmail(email).map(userMongoMapper::userDocumentToUser);
    }

    @Override
    public void update(User user) {
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        Update update = new Update();
        update.set("name", user.getName());
        update.set("surname1", user.getSurname1());
        update.set("surname2", user.getSurname2());
        if (!Util.isNull(user.getPassword())) update.set("password", user.getPassword());
        mongoTemplate.updateFirst(query, update, UserDocument.class);
    }

    @Override
    public void updateStatus(String id, Boolean active) {
        this.userMongoRepository.updateStatus(id, active);
    }

}