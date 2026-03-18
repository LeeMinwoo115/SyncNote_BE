package com.syncnote.domain.room.room.controller;

import com.syncnote.domain.room.room.dto.request.CreateRoomRequest;
import com.syncnote.domain.room.room.dto.response.CreateRoomResponse;
import com.syncnote.domain.room.room.dto.response.GetRoomResponse;
import com.syncnote.domain.room.room.dto.response.GetRoomSummary;
import com.syncnote.domain.room.room.service.RoomService;
import com.syncnote.global.http.ApiResponse;
import com.syncnote.global.http.HttpRequestContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Validated
@Tag(name = "Room API", description = "방 생성,조회,삭제 API")
public class RoomController {

    private final HttpRequestContext httpRequestContext;
    private final RoomService roomService;

    @PostMapping
    @Operation(summary = "방 생성")
    public ApiResponse<CreateRoomResponse> createRoom(@Validated @RequestBody CreateRoomRequest request) {
        long userId = httpRequestContext.getUserId();
        CreateRoomResponse response = roomService.createRoom(userId, request);
        return ApiResponse.created("방 생성 성공", response);
    }

    @GetMapping
    @Operation(summary = "참여 중인 모든 방 조회")
    public ApiResponse<List<GetRoomSummary>> getRooms() {
        long userId = httpRequestContext.getUserId();
        List<GetRoomSummary> response = roomService.getRooms(userId);
        return ApiResponse.ok(response);
    }

    @GetMapping("/{roomId}")
    @Operation(summary = "참여 방 상세 조회")
    public ApiResponse<GetRoomResponse> getRoom(@PathVariable("roomId") Long roomId) {
        long userId = httpRequestContext.getUserId();
        GetRoomResponse response = roomService.getRoom(userId, roomId);
        return ApiResponse.ok("방 상세 조회를 성공했습니다.", response);
    }
}
