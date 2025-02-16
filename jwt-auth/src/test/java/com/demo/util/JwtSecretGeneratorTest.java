package com.demo.util;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * util
 *
 * @author : idasom
 * @data : 2/13/25
 */
public class JwtSecretGeneratorTest {
    @Test
    void shouldGenerateBase64EncodedSecretKey() {
        // given
        byte[] key = new byte[32];

        // when
        new SecureRandom().nextBytes(key);
        String base64key = Base64.getEncoder().encodeToString(key);

        // then
        assertThat(base64key).isNotNull() // null X
                .isNotEmpty() // 빈값 X
                .doesNotContain(" "); // 공백 X

        System.out.println("jwt secret key : " + base64key);
    }

}
