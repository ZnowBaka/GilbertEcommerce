package com.example.gilbertecommerce.Framework;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepo {
    private final JdbcTemplate jdbcTemplate;

    public UserRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getLoginInfo(String loginName) {
        String sql = "select user_pass from LoginInfo where user_login_name = ?";
        return jdbcTemplate.query(sql, )

    }
}
