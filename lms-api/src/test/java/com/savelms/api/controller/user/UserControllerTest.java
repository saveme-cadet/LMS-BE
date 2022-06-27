package com.savelms.api.controller.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.larry.fc.finalproject.core.domain.entity.repository.UserRepository;
import com.larry.fc.finalproject.core.domain.entity.user.User;
import com.savelms.api.user.controller.dto.UserSignUpRequest;
import com.savelms.api.user.controller.dto.UserSignUpResponse;
import com.savelms.api.user.service.UserService;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void signUp() throws Exception {
        //given

        UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        userSignUpRequest.setUsername("takim");
        userSignUpRequest.setPassword("1234");
        userSignUpRequest.setEmail("email");

        String domain = "http://localhost:";
        String requestPath = "/api/users";
        String url = domain + port + requestPath;

        int expectedUserNum = 5;
        //when
        ResponseEntity<UserSignUpResponse> response = testRestTemplate.postForEntity(url,
            userSignUpRequest, UserSignUpResponse.class);
        List<User> all = userRepository.findAll();

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(all.size()).isEqualTo(expectedUserNum);
        assertThat(response.getBody().getUserId()).isEqualTo(all.get(expectedUserNum - 1).getId());
    }

}