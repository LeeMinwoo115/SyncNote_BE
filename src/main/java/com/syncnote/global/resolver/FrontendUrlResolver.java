package com.syncnote.global.resolver;

import com.syncnote.global.properties.FrontendProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FrontendUrlResolver {
    private final FrontendProperties frontendProperties;
    private final HttpServletRequest request;

    public String resolveBaseUrl() {
        String origin = request.getHeader("Origin");

        if (origin != null && frontendProperties.getAllowedBaseUrls().contains(origin)) {
            return origin;
        }

        String referer = request.getHeader("Referer");
        if (referer != null) {
            for (String allowed : frontendProperties.getAllowedBaseUrls()) {
                if (referer.startsWith(allowed)) {
                    return allowed;
                }
            }
        }

        return frontendProperties.getDefaultBaseUrl();
    }
}
