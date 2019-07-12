package com.dah.camel.domain.role;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    public Role getById(@Param("id") String id);

    public List<Role> getAll();

    public void save(Role role);

    public void update(Role role);

}
