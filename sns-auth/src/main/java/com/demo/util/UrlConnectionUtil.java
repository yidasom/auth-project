package com.demo.util;

import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * com.demo.config
 *
 * @author : idasom
 */
public final class UrlConnectionUtil {

    private UrlConnectionUtil() {
    }

    private static final Logger logger = LoggerFactory.getLogger(UrlConnectionUtil.class);

    public static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        if (con == null) {
            logger.debug("API 연결 실패.");
            return null;
        }
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            logger.debug("API 요청과 응답 실패. {}", e.getCause().getMessage());
            return null;
        } finally {
            con.disconnect();
        }
    }

    public static String post(String apiUrl, Map<String, String> requestHeaders, String params) {
        HttpURLConnection con = connect(apiUrl);
        if (con == null) {
            logger.debug("API 연결 실패.");
            return null;
        }
        try {
            con.setRequestMethod("POST");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            if (StringUtils.isNotEmpty(params)) {
                con.setDoOutput(true);
                byte[] data = params.getBytes(StandardCharsets.UTF_8);
                con.setRequestProperty("Content-Length", String.valueOf(data.length));
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(params);
                wr.flush();
                wr.close();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            logger.debug("API 요청과 응답 실패. {}", e.getCause().getMessage());
            return null;
        } finally {
            con.disconnect();
        }
    }

    public static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            logger.debug("API URL이 잘못되었습니다. {}", e.getCause().getMessage());
            return null;
        } catch (IOException e) {
            logger.debug("연결이 실패했습니다. {}", e.getCause().getMessage());
            return null;
        }
    }

    public static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            logger.debug("API 응답을 읽는데 실패했습니다. {}", e.getCause().getMessage());
            return null;
        }
    }

}
