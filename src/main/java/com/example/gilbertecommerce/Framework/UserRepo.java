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
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public SecurityConfig securityConfig;

    public UserRepo(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public String getLoginInfo(String loginName) {
        String sql = "select user_pass from LOGIN_INFO where user_loginName = ?";
        //the line below allows for more flexiable input but not needed in this case
        // return jdbcTemplate.queryForObject(sql, new Object[]{loginName}, String.class);
        return jdbcTemplate.queryForObject(sql, String.class, loginName);
    }
    public boolean doesLoginInfoExist(String loginName) throws UserAlreadyExistException {
        String sql = "select * from LOGIN_INFO where login_Name = ?";
        if (jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LoginInfo.class), loginName).size() > 0) {
            return false;
        } else {
            return true;
        }
    }
    public boolean createNewUser(LoginInfo loginInfo, User user) {
        try {
            String sql = "insert into USER (first_name, last_name, user_email) values (?, ?, ?)";
            jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getEmail());

            sql = "select user_id from USER where display_name = ?";
            int id = jdbcTemplate.queryForObject(sql, Integer.class, user.getDisplayName());

            sql = "insert into LOGIN_INFO (login_id, user_loginName, user_pass) values (?, ?, ?)";
            jdbcTemplate.update(sql, id,loginInfo.getLoginName(), loginInfo.getLoginPass());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
