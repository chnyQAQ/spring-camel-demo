package com.dah.camel.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getById(String id) {
        return userMapper.getById(id);
    }

    public List<User> getAll(User user) {
        return userMapper.getAll();
    }

    public User update(String id, User user) {
        user.setId(id);
        userMapper.update(user);
        return user;
    }

    public User save(User user) {
        user.setId(UUID.randomUUID().toString());
        userMapper.save(user);
        return user;
    }
}
