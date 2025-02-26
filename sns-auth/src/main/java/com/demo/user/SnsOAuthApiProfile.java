package com.demo.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * com.demo.user
 *
 * @author : idasom
 */
public class SnsOAuthApiProfile {
    // 카카오
    private String id = "";
    private Map<String, Object> kakaoAccount; // email
    private Map<String, Object> properties; // nickname
    private String code; // 카카오 error코드 확인
    private String msg; // 카카오 error메시지

    // 네이버
    private Map<String, String> response;
    /**
     * 결과 코드 00 정상 024 Authentication failed / 인증에 실패했습니다. 028 Authentication header
     * not exists / OAuth 인증 헤더(authorization header)가 없습니다. 403 Forbidden / 호출 권한이
     * 없습니다. 404 Not Found / 검색 결과가 없습니다. 500 Internal Server Error / 데이터베이스 오류입니다.
     */
    private String resultcode; // 네이버 error코드 확인
    private String message; // 네이버 error메시지

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getKakaoAccount() {
        if (this.kakaoAccount == null) {
            return null;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map = this.kakaoAccount;
            return map;
        }
    }

    public void setKakaoAccount(Map<String, Object> kakaoAccount) {
        if (kakaoAccount != null) {
            // 키로 정렬
            Object[] mapKeys = kakaoAccount.keySet().toArray();
            Arrays.sort(mapKeys);

            this.kakaoAccount = new HashMap<String, Object>();
            for (Object key : mapKeys) {
                Object obj = kakaoAccount.get(key.toString());
                this.kakaoAccount.put(key.toString(), obj);
            }
        }
    }

    public Map<String, Object> getProperties() {
        if (this.properties == null) {
            return null;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map = this.properties;
            return map;
        }
    }

    public void setProperties(Map<String, Object> properties) {
        if (properties != null) {
            // 키로 정렬
            Object[] mapKeys = properties.keySet().toArray();
            Arrays.sort(mapKeys);

            this.properties = new HashMap<String, Object>();
            for (Object key : mapKeys) {
                Object obj = properties.get(key.toString());
                this.properties.put(key.toString(), obj);
            }
        }
    }

    public Map<String, String> getResponse() {
        if (this.response == null) {
            return null;
        } else {
            Map<String, String> map = new HashMap<String, String>();
            map = this.response;
            return map;
        }
    }

    public void setResponse(Map<String, String> response) {
        if (response != null) {
            // 키로 정렬
            Object[] mapKeys = response.keySet().toArray();
            Arrays.sort(mapKeys);

            this.response = new HashMap<String, String>();
            for (Object key : mapKeys) {
                String obj = response.get(key.toString());
                this.response.put(key.toString(), obj);
            }
        }
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
