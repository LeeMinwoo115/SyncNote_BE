package com.syncnote.domain.room.room.controller;

import com.syncnote.domain.room.room.dto.request.CreateRoomRequest;
import com.syncnote.domain.room.room.dto.request.JoinRoomRequest;
import com.syncnote.domain.room.room.dto.response.CreateRoomResponse;
import com.syncnote.domain.room.room.dto.response.GetRoomResponse;
import com.syncnote.domain.room.room.dto.response.GetRoomSummary;
import com.syncnote.domain.room.room.service.RoomService;
import com.syncnote.global.http.ApiResponse;
import com.syncnote.global.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Validated
@Tag(name = "Room API", description = "방 생성,조회,삭제 API")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @Operation(summary = "방 생성")
    public ApiResponse<CreateRoomResponse> createRoom(
            @AuthenticationPrincipal SecurityUser user,
            @Validated @RequestBody CreateRoomRequest request
    ) {
        CreateRoomResponse response = roomService.createRoom(user.getId(), request);
        return ApiResponse.created("방 생성 성공", response);
    }

    @GetMapping
    @Operation(summary = "참여 중인 모든 방 조회")
    public ApiResponse<List<GetRoomSummary>> getRooms(@AuthenticationPrincipal SecurityUser user) {
        List<GetRoomSummary> response = roomService.getRooms(user.getId());
        return ApiResponse.ok(response);
    }

    @GetMapping("/{roomId}")
    @Operation(summary = "참여 방 상세 조회")
    public ApiResponse<GetRoomResponse> getRoom(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable("roomId") Long roomId
    ) {
        GetRoomResponse response = roomService.getRoom(user.getId(), roomId);
        return ApiResponse.ok("방 상세 조회를 성공했습니다.", response);
    }

    @PostMapping("/join")
    @Operation(summary = "초대코드로 방 참여")
    public ApiResponse<Void> joinRoom(
            @AuthenticationPrincipal SecurityUser user,
            @Validated @RequestBody JoinRoomRequest request
    ) {
        roomService.joinRoom(user.getId(), request.inviteCode());
        return ApiResponse.noContent("방 참여에 성공했습니다.");
    }
}
