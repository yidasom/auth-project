package com.demo.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * com.demo.config
 *
 * @author : idasom
 */
@Slf4j
@Getter
@Setter
@Component
@Configuration
//@ConfigurationProperties(prefix = "sns.login")
public class SnsConfig {
    private Map<String, SnsProperties> types = new HashMap<>(); // 내부 클래스 제거 후 설정용 클래스 사용

    @Getter
    @Setter
    public static class SnsProperties {
        private String id;
        private String secret;
        private String callback;
    }

    @PostConstruct
    public void init() {
        log.info("SnsConfig initialized with: {}", types);
    }

    public SnsProperties getType(SnsType type) {
        log.info("Fetching SnsProperties for type: {}", type.getSnsType().toLowerCase());
        return types.get(type.getSnsType().toLowerCase());
    }
}
