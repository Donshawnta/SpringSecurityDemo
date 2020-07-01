package com.example.demo;

import com.example.demo.util.LangUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

public class BaseTest {


    @LocalServerPort
    @Getter
    private int port;

    @Value("${oauth.paths.token:/authentication}")
    private String tokenPath;

    CustomComparator userDtoComparator = new CustomComparator(JSONCompareMode.LENIENT,
            new Customization("createdAt", (o1, o2) -> true));
    ObjectMapper objectMapper = new ObjectMapper();

    OAuth2AccessToken getAccessToken(String clientId, String password) {
        HttpHeaders headers = new HttpHeaders();
        String basicValue = clientId + ":" + password;
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(basicValue.getBytes()));

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<Map> response = new TestRestTemplate(clientId, password).postForEntity(
                "http://localhost:" + port + tokenPath, request, Map.class);

        OAuth2AccessToken accessToken = DefaultOAuth2AccessToken.valueOf(response.getBody());

        return accessToken;
    }

    ResponseEntity<String> callGet(OAuth2AccessToken oAuth2AccessToken, String pathStr) {

        String url = "http://localhost:" + port + pathStr;

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

        if (oAuth2AccessToken != null) {
            headers.add("Authorization", String.format("%s %s", oAuth2AccessToken.getTokenType(), oAuth2AccessToken.getValue()));
        }

        RestTemplate restTemplate = new TestRestTemplate().getRestTemplate();


        HttpEntity<String> request = new HttpEntity<String>(headers);

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return result;
    }

    ResponseEntity<String> callHttp(OAuth2AccessToken oAuth2AccessToken, HttpMethod httpMethod, String pathStr, Object data) {

        String url = "http://localhost:" + port + pathStr;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        if (StringUtils.hasText(oAuth2AccessToken.getTokenType())) {
            headers.add("Authorization", String.format("%s %s", oAuth2AccessToken.getTokenType(), oAuth2AccessToken.getValue()));
        }

        RestTemplate restTemplate = new TestRestTemplate().getRestTemplate();

        String bodyStr = LangUtils.sneakyThrows(() -> objectMapper.writeValueAsString(data));

        return restTemplate.exchange(url, httpMethod, new HttpEntity<>(bodyStr, headers), String.class);
    }


}
