package com.syncnote.domain.home.controller;

import com.syncnote.domain.home.dto.TestResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@Tag(name = "API Docs Home", description = "API 문서")
public class HomeController {

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String home() throws UnknownHostException {
        InetAddress localhost = InetAddress.getLocalHost();

        return """
			<h1>SyncNote API Docs</h1>
			<p>Server IP Address: %s</p>
			<p>Server Host Name: %s</p>
			<div>
				<a href="swagger-ui/index.html">API 문서로 이동</a>
			</div>
			""".formatted(localhost.getHostAddress(), localhost.getHostName());
    }

	@GetMapping("/api/v1/test")
	public ResponseEntity<TestResponseDto> testFetch() {
		return ResponseEntity.ok(new TestResponseDto("테스트 API 호출 성공"));
	}
}
