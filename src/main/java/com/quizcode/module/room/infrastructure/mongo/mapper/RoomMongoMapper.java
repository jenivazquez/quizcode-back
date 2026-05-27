package com.quizcode.module.room.infrastructure.mongo.mapper;

import com.quizcode.module.room.domain.entity.Room;
import com.quizcode.module.room.domain.entity.SavedRoom;
import com.quizcode.module.room.infrastructure.mongo.document.RoomDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMongoMapper {

    default Room roomDocumentToRoom(RoomDocument doc) {
        return new SavedRoom(doc.getId(), doc.getName(), doc.getDescription(), doc.getCode(), doc.getStatus(), doc.getQuizId(), doc.getCreatedAt(), doc.getStartedAt(), doc.getFinishedAt(), doc.getReviewed()).getRoom();
    }

    RoomDocument roomToRoomDocument(Room room);
}
