package com.example.gilbertecommerce.Framework;

import com.example.gilbertecommerce.CustomException.UserAlreadyExistException;
import com.example.gilbertecommerce.Entity.LoginInfo;
import com.example.gilbertecommerce.Entity.User;
import com.example.gilbertecommerce.Entity.UserRole;
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
        String sql = "select * from LOGIN_INFO where user_loginEmail = ?";
        //the line below allows for more flexiable input but not needed in this case
        // return jdbcTemplate.queryForObject(sql, new Object[]{loginEmail}, String.class);
        //jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(LoginInfo.class), loginInfo.getLoginEmail());
        return jdbcTemplate.queryForObject(sql, new Object[]{loginInfo.getLoginEmail()}, (rs, rowNum) -> {

            LoginInfo userFromdb = new LoginInfo();
            userFromdb.setLoginId(rs.getInt("login_id"));
            userFromdb.setLoginEmail(rs.getString("user_loginEmail"));
            userFromdb.setLoginPass(rs.getString("user_pass"));
            System.out.println("logininfo " + userFromdb.getLoginEmail() + " is in the db with id:" + userFromdb.getLoginId());
            // result (User jack is in the db with id:1) it just works
            // result User bob is in the db with id:4
            return userFromdb;
        });
    }

    public boolean doesLoginInfoExist(String loginEmail) throws UserAlreadyExistException {
        String sql = "select count(*) from LOGIN_INFO where user_loginEmail = ?";
        int existingCount = jdbcTemplate.queryForObject(sql, Integer.class, loginEmail);
        return existingCount > 0;
    }

    public boolean createNewUser(LoginInfo loginInfo, User user) {
        try {
            String sql = "insert into USER (first_name, last_name, user_email, displayName) values (?, ?, ?, ?)";
            jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getEmail(), user.getDisplayName());

            sql = "select user_id from USER where user_email = ?";
            int id = jdbcTemplate.queryForObject(sql, Integer.class, user.getEmail());

            sql = "insert into LOGIN_INFO (login_id, user_loginEmail, user_pass) values (?, ?, ?)";
            jdbcTemplate.update(sql, id, loginInfo.getLoginEmail(), loginInfo.getLoginPass());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public User getUserById(LoginInfo loginInfo) {
        String sql = "select * from USER where user_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{loginInfo.getLoginEmail()}, (rs, rowNum) -> {

            User userFromdb = new User();
            userFromdb.setUserID(rs.getInt("user_id"));
            userFromdb.setFirstName(rs.getString("first_name"));
            userFromdb.setLastName(rs.getString("last_name"));
            userFromdb.setEmail(rs.getString("user_email"));
            userFromdb.setDisplayName(rs.getString("displayName"));
            int code = rs.getInt("user_role");
            userFromdb.setRole(UserRole.fromCode(code));
            return userFromdb;
        });
    }
}

