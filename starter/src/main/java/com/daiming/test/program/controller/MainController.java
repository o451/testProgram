package com.daiming.test.program.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.daiming.test.program.components.UserComponent;
import com.daiming.test.program.user.HeartbeatInfo;
import com.daiming.test.program.dal.dataobject.User;
import com.daiming.test.program.user.UserIdentityId;
import com.daiming.test.program.user.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "")
public class MainController {

    @Value("${com.daiming.version}")
    private String versionNumber;

    @Value("${com.daiming.release.time}")
    private String releaseTime;

    @Resource
    private UserComponent userComponent;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect("/docs/index.html");
        return null;
    }

    @RequestMapping(value = "/docs", method = RequestMethod.GET)
    public String docs(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect("/docs/index.html");
        return null;
    }

    @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
    public HeartbeatInfo heartbeat(HttpServletRequest request, HttpServletResponse response) {

        HeartbeatInfo heartbeatInfo = new HeartbeatInfo();
        heartbeatInfo.setReleasedAt(releaseTime);
        heartbeatInfo.setVersion(versionNumber);
        return heartbeatInfo;
    }

    @PostMapping(value="/user", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public UserIdentityId userCreate(@NotNull @Valid @RequestBody final User requestBody) {

        User user = new User();
        user.setFirst(requestBody.getFirst());
        user.setLast(requestBody.getLast());
        user.setEmail(requestBody.getEmail());
        user.setPassword(requestBody.getPassword());

        String userId = userComponent.createUser(user);

        UserIdentityId ret = new UserIdentityId();
        ret.setId(userId);
        return ret;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value="/user", produces = "application/json;charset=UTF-8")
    public User userGet(HttpServletRequest request, @NotNull @Valid @RequestBody UserToken userToken) {

        String token = userToken.getToken();
        if (!StringUtils.isEmpty(token)) {
            return userComponent.getUser(token);
        }
        return null;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/user/login", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public UserToken userLogin(HttpServletRequest request, @NotNull @Valid @RequestBody User body) {

        String email = body.getEmail();
        String password = body.getPassword();
        User u = new User();
        u.setEmail(email);
        u.setPassword(password);

        String token = userComponent.createToken(u);
        if (!StringUtils.isEmpty(token)) {
            UserToken userToken = new UserToken();
            userToken.setToken(token);
            return userToken;
        }
        return null;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public String userLogout(HttpServletRequest request, @NotNull @Valid @RequestBody UserToken userToken) {

        String token = userToken.getToken();
        userComponent.removeToken(token);
        return "";
    }

}
