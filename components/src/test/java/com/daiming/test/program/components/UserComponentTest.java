package com.daiming.test.program.components;

import java.io.File;
import java.util.HashMap;

import com.daiming.test.program.dal.dataobject.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static com.daiming.test.program.components.UserComponent.emailUserMap;

@ExtendWith(MockitoExtension.class)
class UserComponentTest {

    @InjectMocks
    private UserComponent userComponent;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
            userComponent,
            "userFile",
            new File("src/test/resources/testUserfile.json").getAbsolutePath()
        );
        UserComponent.emailUserMap = new HashMap<>();
        UserComponent.tokenEmailMap = new HashMap<>();
        userComponent.initUser();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void initUser() {
        Assertions.assertTrue(emailUserMap.size() > 0);
    }

    @Test
    void createUser() {
        int old = emailUserMap.size();
        User user = new User();
        user.setLast("newLast");
        user.setFirst("newFirst");
        user.setEmail("new" + System.currentTimeMillis() + "@email.com");
        user.setUserId("userId");
        userComponent.createUser(user);
        Assertions.assertTrue(emailUserMap.size() > old);
    }

    @Test
    void tokenOperations() {
        userComponent.initUser();
        User user = (User)emailUserMap.values().toArray()[0];
        Assertions.assertNotNull(user);
        String token = userComponent.createToken(user);
        Assertions.assertTrue(StringUtils.isNotBlank(token));
        User getUser = userComponent.getUser(token);
        Assertions.assertNotNull(getUser);
        Assertions.assertEquals(getUser.getEmail(), user.getEmail());
        userComponent.removeToken(token);
        User getUser2 = userComponent.getUser(token);
        Assertions.assertNull(getUser2);
    }
}