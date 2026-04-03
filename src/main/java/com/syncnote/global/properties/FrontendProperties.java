package com.syncnote.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.frontend")
public class FrontendProperties {
    private String defaultBaseUrl;
    private List<String> allowedBaseUrls = new ArrayList<>();
}
