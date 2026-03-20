package com.syncnote;

import com.syncnote.domain.auth.repository.RefreshTokenRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class SyncnoteApplicationTests {

    @MockitoBean
    private RefreshTokenRedisRepository refreshTokenRedisRepository;
    
    @Test
    void contextLoads() {
    }

}
