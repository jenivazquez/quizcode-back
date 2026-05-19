package com.quizcode.module.user.infrastructure.mongo.mapper;

import com.quizcode.module.user.domain.entity.SavedUser;
import com.quizcode.module.user.domain.entity.User;
import com.quizcode.module.user.infrastructure.mongo.document.UserDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMongoMapper {

    default User userDocumentToUser(UserDocument doc) {
        return new SavedUser(doc.getId(), doc.getEmail(), doc.getPassword(), doc.getName(), doc.getSurname1(), doc.getSurname2(), doc.getActive()).getUser();
    }

    UserDocument userToUserDocument(User user);
}
