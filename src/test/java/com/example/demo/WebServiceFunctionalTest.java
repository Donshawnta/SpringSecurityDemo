package com.example.demo;

import com.example.demo.controller.dto.UserCreateDTO;
import com.example.demo.controller.dto.UserModifyDTO;
import com.example.demo.db.DefaultDataLoader;
import com.example.demo.util.LangUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {DemoApplication.class})
@Slf4j
public class WebServiceFunctionalTest extends BaseTest {


    @Autowired
    private DefaultDataLoader defaultDataLoader;

    @Autowired
    private DataSource dataSource;

    @Before
    public void init() {
        cleanupDatabase();
        defaultDataLoader.reInit();
    }

    private void cleanupDatabase() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("delete from role");
        jdbcTemplate.execute("delete from user");
    }


    @Test
    @SneakyThrows
    public void happyCase() {
        OAuth2AccessToken token = getAccessToken("admin", "admin");
        ResponseEntity<String> entity = callGet(token, "/demo/user/admin");
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        JSONAssert.assertEquals(LangUtils.loadText(this.getClass(), "adminExpectedDetails.json"),
                entity.getBody(), userDtoComparator);
    }

    @Test
    @SneakyThrows
    public void getForNonExisting() {
        OAuth2AccessToken token = getAccessToken("admin", "admin");
        ResponseEntity<String> entity = callGet(token, "/demo/user/xxxx");
        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
    }


    @Test
    public void wrongPassword() {
        OAuth2AccessToken token = getAccessToken("admin", "xxxxx");
        ResponseEntity<String> entity = callGet(token, "/demo/user/admin");
        assertEquals(HttpStatus.UNAUTHORIZED, entity.getStatusCode());
    }


    @Test
    @SneakyThrows
    public void sendUserWithoutUsername() {
        //Query admin token
        OAuth2AccessToken token = getAccessToken("admin", "admin");


        //Create user tivadar without username as admin
        UserCreateDTO userCreateDTO = objectMapper.readValue(LangUtils.loadText(this.getClass(), "createTivadar.json"), UserCreateDTO.class);
        userCreateDTO.setUsername(null);
        ResponseEntity<String> entity = callHttp(token, HttpMethod.POST, "/demo/user", userCreateDTO);
        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());

    }


    @Test
    @SneakyThrows
    public void sendUserWithoutWrongAuthority() {
        //Query admin token
        OAuth2AccessToken token = getAccessToken("admin", "admin");


        String userStr = LangUtils.loadText(this.getClass(), "createTivadar.json");
        //Replace PUBLIC authority to unknown one
        userStr.replace("PUBLIC","XXXX");
        ResponseEntity<String> entity = callHttp(token, HttpMethod.POST, "/demo/user", userStr);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entity.getStatusCode());

    }

    @Test
    @SneakyThrows
    public void sendUserTwice() {
        //Query admin token
        OAuth2AccessToken token = getAccessToken("admin", "admin");


        //Create user tivadar without username as admin
        UserCreateDTO userCreateDTO = objectMapper.readValue(LangUtils.loadText(this.getClass(), "createTivadar.json"), UserCreateDTO.class);
        ResponseEntity<String> entity = callHttp(token, HttpMethod.POST, "/demo/user", userCreateDTO);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        ResponseEntity<String> entitySecond = callHttp(token, HttpMethod.POST, "/demo/user", userCreateDTO);
        assertEquals(HttpStatus.CONFLICT, entitySecond.getStatusCode());

    }

    @Test
    @SneakyThrows
    public void createUserAndTestItsAuthorities() {
        //Query admin token
        OAuth2AccessToken adminToken = getAccessToken("admin", "admin");


        //Create user tivadar as admin
        UserCreateDTO userCreateDTO = objectMapper.readValue(LangUtils.loadText(this.getClass(), "createTivadar.json"), UserCreateDTO.class);
        ResponseEntity<String> entity = callHttp(adminToken, HttpMethod.POST, "/demo/user", userCreateDTO);
        assertEquals(HttpStatus.OK, entity.getStatusCode());

        //Query tivadar as admin
        ResponseEntity<String> tivadarEntity = callGet(adminToken, "/demo/user/tivadar");
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        JSONAssert.assertEquals(LangUtils.loadText(this.getClass(), "tivadarExpectedDetails.json"),
                tivadarEntity.getBody(), userDtoComparator);


        //Change name of tivadar as admin
        UserModifyDTO userModifyDTO = new UserModifyDTO();
        BeanUtils.copyProperties(userModifyDTO, userCreateDTO);
        userModifyDTO.setName("Kelemen Samu");
        ResponseEntity<String> putEntity = callHttp(adminToken, HttpMethod.PUT, "/demo/user/tivadar", userModifyDTO);
        assertEquals(HttpStatus.OK, putEntity.getStatusCode());
        tivadarEntity = callGet(adminToken, "/demo/user/tivadar");
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        JSONAssert.assertEquals(LangUtils.loadText(this.getClass(), "tivadarRenamedExpectedDetails.json"),
                tivadarEntity.getBody(), userDtoComparator);

        //Request a token for tivadar
        OAuth2AccessToken tivadarToken = getAccessToken("tivadar", "demo");


        //Try to add user as tivadar
        UserCreateDTO ferencCreateDTO = objectMapper.readValue(LangUtils.loadText(this.getClass(), "createFerenc.json"), UserCreateDTO.class);
        ResponseEntity<String> ferencCreateEntity = callHttp(tivadarToken, HttpMethod.POST, "/demo/user", ferencCreateDTO);
        assertEquals(HttpStatus.FORBIDDEN, ferencCreateEntity.getStatusCode());

        //Query all users as tivadar
        ResponseEntity<String> allUsersEntity = callGet(tivadarToken, "/demo/user");
        assertEquals(HttpStatus.OK, allUsersEntity.getStatusCode());
        JSONAssert.assertEquals(LangUtils.loadText(this.getClass(), "expectedUserList.json"),
                allUsersEntity.getBody(), userDtoComparator);

    }


    @Test
    public void accessWithoutAuth() {
        ResponseEntity<String> entity = callGet(null, "/demo/user/admin");
        assertEquals(HttpStatus.UNAUTHORIZED, entity.getStatusCode());
    }


}

