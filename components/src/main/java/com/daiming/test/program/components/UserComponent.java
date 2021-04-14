package com.daiming.test.program.components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSON;

import com.daiming.test.program.dal.dataobject.User;
import com.daiming.test.program.utils.IdGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class UserComponent {

    private static String userFile = "./user.txt";

    public static Map<String, User> emailUserMap = new HashMap<>();

    public static Map<String, String> tokenEmailMap = new HashMap<>();

    @PostConstruct
    public void initUser() {
        File file = new File(userFile);
        if (file.exists()) {
            loadUserFile(file);
        }
    }

    public String createUser(User user) {

        if (emailUserMap.containsKey(user.getEmail())) {
            return emailUserMap.get(user.getEmail()).getUserId();
        }
        String userId = IdGeneratorUtil.getUUID();
        user.setUserId(userId);
        emailUserMap.put(user.getEmail(), user);
        log.info("User " + user.getEmail() + " is created");

        /**
         * run in async mode
         */
        saveUserFile(userFile);
        return userId;
    }

    public String createToken(User user) {

        User userExisted = emailUserMap.get(user.getEmail());

        if (Objects.isNull(userExisted)) {
            return null;
        }
        if (userExisted.getPassword().equalsIgnoreCase(user.getEmail())) {
            return null;
        }
        List<Entry<String, String>> existed =
        tokenEmailMap.entrySet().stream()
            .filter(item -> item.getValue().equalsIgnoreCase(user.getEmail()))
            .collect(Collectors.toList());

        if (!existed.isEmpty()) {
            return existed.get(0).getKey();
        }
        String token = IdGeneratorUtil.getUUID();
        tokenEmailMap.put(token, user.getEmail());
        return token;
    }

    public void removeToken(String token) {
        tokenEmailMap.remove(token);
    }

    public User getUser(String token) {
        if (tokenEmailMap.containsKey(token)) {
            String email = tokenEmailMap.get(token);
            return emailUserMap.get(email);
        }
        return null;
    }

    private static void loadUserFile(File f) {

        StringBuffer sb = new StringBuffer();
        try {
            FileReader fr = new FileReader(f);
            char buf[] = new char[256];
            int len = 0;
            while((len = fr.read(buf)) != -1) {
                sb.append(buf, 0, len);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            return;
        } catch (IOException e) {
            log.error(e.getMessage());
            return;
        }
        String json = sb.toString();
        if (!StringUtils.isEmpty(json)) {
            List<User> users = JSON.parseArray(json, User.class);
            users.forEach(item -> emailUserMap.put(item.getEmail(), item));
        }
    }

    @Async
    private synchronized void saveUserFile(String file) {
        File f = new File(file);
        try (FileWriter fw = new FileWriter(f)) {
            String s = JSON.toJSON(emailUserMap.values()).toString();
            fw.write(s);
        } catch (IOException e) {
            log.error(e.getMessage());
            return;
        }
    }
}
