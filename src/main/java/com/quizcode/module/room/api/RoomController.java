package com.quizcode.module.room.api;

import com.quizcode.module.room.api.dto.CreateRoomRequest;
import com.quizcode.module.room.api.dto.IdRoomResponse;
import com.quizcode.module.room.api.dto.RoomResponse;
import com.quizcode.module.room.api.dto.UpdateRoomRequest;
import com.quizcode.module.room.api.dto.UpdateRoomStatusRequest;
import com.quizcode.module.room.api.mapper.RoomMapper;
import com.quizcode.module.room.domain.RoomService;
import com.quizcode.shared.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    public RoomController(RoomService roomService, RoomMapper roomMapper) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
    }

    @PostMapping(path = "/user/{ownerId}/quiz/{quizId}/room")
    @ResponseStatus(HttpStatus.CREATED)
    public IdRoomResponse create(@PathVariable String ownerId, @PathVariable String quizId, @RequestBody CreateRoomRequest request) {
        SecurityUtil.checkAuthorized(ownerId);
        String id = roomService.create(ownerId, roomMapper.createRoomRequestToRoom(quizId, request));
        return new IdRoomResponse(id);
    }

    @GetMapping(path = "/user/{ownerId}/quiz/{quizId}/room")
    @ResponseStatus(HttpStatus.OK)
    public List<RoomResponse> findByQuizId(@PathVariable String ownerId, @PathVariable String quizId) {
        SecurityUtil.checkAuthorized(ownerId);
        return roomService.findByQuizId(ownerId, quizId).stream()
                .map(roomMapper::roomToRoomResponse)
                .toList();
    }

    @GetMapping(path = "/user/{ownerId}/room")
    @ResponseStatus(HttpStatus.OK)
    public List<RoomResponse> findByOwnerId(@PathVariable String ownerId) {
        SecurityUtil.checkAuthorized(ownerId);
        return roomService.findByOwnerId(ownerId).stream()
                .map(roomMapper::roomToRoomResponse)
                .toList();
    }

    @GetMapping(path = "/user/{ownerId}/quiz/{quizId}/room/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoomResponse findById(@PathVariable String ownerId, @PathVariable String quizId, @PathVariable String id) {
        SecurityUtil.checkAuthorized(ownerId);
        return roomMapper.roomToRoomResponse(roomService.findById(id, ownerId, quizId));
    }

    @PatchMapping(path = "/user/{ownerId}/quiz/{quizId}/room/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String ownerId, @PathVariable String quizId, @PathVariable String id, @RequestBody UpdateRoomRequest request) {
        SecurityUtil.checkAuthorized(ownerId);
        roomService.update(ownerId, roomMapper.updateRoomRequestToRoom(id, quizId, request));
    }

    @PatchMapping(path = "/user/{ownerId}/quiz/{quizId}/room/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable String ownerId, @PathVariable String quizId, @PathVariable String id, @RequestBody UpdateRoomStatusRequest request) {
        SecurityUtil.checkAuthorized(ownerId);
        roomService.updateStatus(id, ownerId, quizId, request.getStatus());
    }

    @DeleteMapping(path = "/user/{ownerId}/quiz/{quizId}/room/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String ownerId, @PathVariable String quizId, @PathVariable String id) {
        SecurityUtil.checkAuthorized(ownerId);
        roomService.delete(id, ownerId, quizId);
    }

    @GetMapping(path = "/room/code/{code}")
    @ResponseStatus(HttpStatus.OK)
    public RoomResponse findByCode(@PathVariable String code) {
        return roomMapper.roomToRoomResponse(roomService.findByCode(code));
    }
}
