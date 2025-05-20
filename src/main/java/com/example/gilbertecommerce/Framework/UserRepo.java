package com.example.gilbertecommerce.Framework;

import com.example.gilbertecommerce.CustomException.UserAlreadyExistException;
import com.example.gilbertecommerce.Entity.LoginInfo;
import com.example.gilbertecommerce.Entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        //jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(LoginInfo.class), loginInfo.getLoginName());
        return jdbcTemplate.queryForObject(sql, new Object[]{loginInfo.getLoginName()}, (rs, rowNum) -> {

            LoginInfo loginInfo1 = new LoginInfo();
            loginInfo1.setLoginId(rs.getInt("login_id"));
            loginInfo1.setLoginName(rs.getString("user_loginName"));
            loginInfo1.setLoginPass(rs.getString("user_pass"));
            System.out.println("logininfo " + loginInfo1.getLoginName() + " is in the db with id:" + loginInfo1.getLoginId());
            // result (User jack is in the db with id:1) it just works
            // result User bob is in the db with id:4
            return loginInfo1;
        });
        }

    public boolean doesLoginInfoExist(String loginName) throws UserAlreadyExistException {
        String sql = "select count(*) from LOGIN_INFO where user_loginName = ?";
        int existisngCount = jdbcTemplate.queryForObject(sql, Integer.class, loginName);
        if(existisngCount < 0){
            return false;
        }else
            return true;
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
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
        } catch (EmptyResultDataAccessException e) {
            // Log the exception, return null or handle appropriately
            System.out.println("No user found with ID: " + id);
            return null; // Safely handle in case no user is found
        }
    }
}
