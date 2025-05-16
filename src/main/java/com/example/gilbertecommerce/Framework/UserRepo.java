package com.example.gilbertecommerce.Framework;

import com.example.gilbertecommerce.CustomException.UserAlreadyExistException;
import com.example.gilbertecommerce.Entity.LoginInfo;
import com.example.gilbertecommerce.Entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepo {
    private final JdbcTemplate jdbcTemplate;
    public SecurityConfig securityConfig;

    public UserRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public LoginInfo getLoginInfo(LoginInfo loginInfo) {
        String sql = "select * from LOGIN_INFO where user_loginName = ?";
        //the line below allows for more flexiable input but not needed in this case
        // return jdbcTemplate.queryForObject(sql, new Object[]{loginName}, String.class);
        LoginInfo loginInfo1 = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(LoginInfo.class), loginInfo.getLoginName());
        System.out.println(loginInfo1.getLoginName());
        return loginInfo1;
        //return jdbcTemplate.queryForObject(sql, String.class, loginName);
    }
    public boolean doesLoginInfoExist(String loginName) throws UserAlreadyExistException {
        String sql = "select * from LOGIN_INFO where user_loginName = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LoginInfo.class), loginName).isEmpty();
    }
    public boolean createNewUser(LoginInfo loginInfo, User user) {
        try {
            String sql = "insert into USER (first_name, last_name, user_email, displayName) values (?, ?, ?, ?)";
            jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getEmail(), user.getDisplayName());

            sql = "select user_id from USER where user_email = ?";
            int id = jdbcTemplate.queryForObject(sql, Integer.class, user.getEmail());

            sql = "insert into LOGIN_INFO (login_id, user_loginName, user_pass) values (?, ?, ?)";
            jdbcTemplate.update(sql, id,loginInfo.getLoginName(), loginInfo.getLoginPass());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    public User getUserById(int id) {
        String sql = "select * from USER where user_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
    }
}
