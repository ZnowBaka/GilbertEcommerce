package com.example.gilbertecommerce.Framework;

import com.example.gilbertecommerce.CustomException.AuthenticationException.UserAlreadyExistException;
import com.example.gilbertecommerce.CustomException.AuthenticationException.UserDoesNotExistException;
import com.example.gilbertecommerce.CustomException.DatabaseExceptionS.DataIntegrityException;
import com.example.gilbertecommerce.Entity.LoginInfo;
import com.example.gilbertecommerce.Entity.User;
import com.example.gilbertecommerce.Entity.UserRole;
import com.example.gilbertecommerce.Service.LoggerService;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserRepo {
    private final JdbcTemplate jdbcTemplate;
    public SecurityConfig securityConfig;
    private final LoggerService logger;

    public UserRepo(LoggerService logger, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.logger = logger;
    }

    public LoginInfo getLoginInfo(LoginInfo loginInfo) {
        try {
            String sql = "select * from LOGIN_INFO where user_loginEmail = ?";

            //the line below allows for more flexible input but not needed in this case
            // return jdbcTemplate.queryForObject(sql, new Object[]{loginEmail}, String.class);
            //jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(LoginInfo.class), loginInfo.getLoginEmail());
            return jdbcTemplate.queryForObject(sql, new Object[]{loginInfo.getLoginEmail()}, (rs, rowNum) -> {

                LoginInfo userFromdb = new LoginInfo();
                userFromdb.setLoginId(rs.getInt("login_id"));
                userFromdb.setLoginEmail(rs.getString("user_loginEmail"));
                userFromdb.setLoginPass(rs.getString("user_pass"));
                System.out.println("logininfo " + userFromdb.getLoginEmail() + " is in the db with id:" + userFromdb.getLoginId());

                return userFromdb;
            });
        } catch (EmptyResultDataAccessException e) {
            UserDoesNotExistException ex = new UserDoesNotExistException(
                    "No user found with email: " + loginInfo.getLoginEmail(),
                    "Login attempt with unknown email: " + loginInfo.getLoginEmail()
            );
            logger.logException(ex);
            throw ex;
        } catch (DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Unexpected database error during login lookup",
                    "Database error while searching login info for: " + loginInfo.getLoginEmail()
            );
            logger.logException(ex);
            throw ex;
        }
    }

    public boolean doesLoginInfoExist(String loginEmail) throws UserAlreadyExistException {
        try {
            String sql = "select count(*) from LOGIN_INFO where user_loginEmail = ?";
            int existingCount = jdbcTemplate.queryForObject(sql, Integer.class, loginEmail);
            return existingCount > 0;
        } catch (DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Unexpected database error while looking for profile",
                    "Could not determine if login email exists: " + loginEmail
            );
            logger.logException(ex);
            throw ex;
        }
    }

    @Transactional
    public boolean createNewUser(LoginInfo loginInfo, User user) {
        try {
            if (doesLoginInfoExist(loginInfo.getLoginEmail())) {
                UserAlreadyExistException ex = new UserAlreadyExistException(
                        "There's already a registered user with the email: " + loginInfo.getLoginEmail(),
                        "Attempt at creating a user with duplicate email: " + loginInfo.getLoginEmail()
                );
                logger.logException(ex);
                throw ex;
            }
            String sql = "insert into USER (first_name, last_name, user_email, displayName) values (?, ?, ?, ?)";
            jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getEmail(), user.getDisplayName());

            sql = "select user_id from USER where user_email = ?"; //Tvunget til at ændre id til interger istedet for int
            Integer id = jdbcTemplate.queryForObject(sql, Integer.class, user.getEmail());

            if (id == null) {
                throw new DataIntegrityException(
                        "Failed to get user ID after creation",
                        "Created user appeared as successful but DB call for id failed for profile: " + loginInfo.getLoginEmail()
                );
            }
            sql = "insert into LOGIN_INFO (login_id, user_loginEmail, user_pass) values (?, ?, ?)";
            jdbcTemplate.update(sql, id, loginInfo.getLoginEmail(), loginInfo.getLoginPass());
            return true;
        } catch (DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Error: Saving new user to database was unsuccessful",
                    "Unsuccessful attempt at inserting user or login info to DB with email: " + user.getEmail()
            );
            logger.logException(ex);
            throw ex;
        }
    }

    public User getUserByEmail(LoginInfo loginInfo) {
        try {
            String sql = "select * from USER where user_email = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{loginInfo.getLoginEmail()}, (rs, rowNum) -> {

                User userFromdb = new User();
                userFromdb.setUserID(rs.getInt("user_id"));
                userFromdb.setFirstName(rs.getString("first_name"));
                userFromdb.setLastName(rs.getString("last_name"));
                userFromdb.setEmail(rs.getString("user_email"));
                userFromdb.setDisplayName(rs.getString("displayName"));
                int code = rs.getInt("user_role");
                userFromdb.setRole(UserRole.fromCode(code));
                System.out.println("user from db: " + userFromdb.getUserID());
                return userFromdb;
            });
        } catch (EmptyResultDataAccessException e) {
            UserDoesNotExistException ex = new UserDoesNotExistException(
                    "No user found with provided email: " + loginInfo.getLoginEmail(),
                    "Email search for non-existent user: " + loginInfo.getLoginEmail()
            );
            logger.logException(ex);
            throw ex;

        } catch (DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Database error while retrieving user by email",
                    "Error querying user with email: " + loginInfo.getLoginEmail()
            );
            logger.logException(ex);
            throw ex;
        }
    }

    public List<User> getAllUsers() {
        try {
            String sql = "SELECT * FROM USER";
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                User userFromDb = new User();
                userFromDb.setUserID(rs.getInt("user_id"));
                userFromDb.setFirstName(rs.getString("first_name"));
                userFromDb.setLastName(rs.getString("last_name"));
                userFromDb.setEmail(rs.getString("user_email"));
                userFromDb.setDisplayName(rs.getString("displayName"));
                int code = rs.getInt("user_role");
                userFromDb.setRole(UserRole.fromCode(code));
                return userFromDb;
            });
        } catch (DataAccessException e) {
            DataIntegrityException ex = new DataIntegrityException(
                    "Database error when fetching all users",
                    "Could not fetch all users from database"
            );
            logger.logException(ex);
            throw ex;
        }
    }
}