package com.dah.camel.domain.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    public User getById(@Param("id") String id);

    public List<User> getAll();

    public void save(User user);

    public void update(User user);

}
