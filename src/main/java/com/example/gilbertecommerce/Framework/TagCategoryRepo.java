package com.example.gilbertecommerce.Framework;

import com.example.gilbertecommerce.Entity.Tag;
import com.example.gilbertecommerce.Entity.TagCategory;
import com.example.gilbertecommerce.Service.LoggerService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TagCategoryRepo {

    private final JdbcOperations jdbcOperations;
    private JdbcTemplate jdbcTemplate;

    public TagCategoryRepo(JdbcTemplate jdbcTemplate, JdbcOperations jdbcOperations, LoggerService logger) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcOperations = jdbcOperations;
    }


    public boolean createTagCategory(TagCategory tagCategory) {
        String sql = "INSERT INTO tag_category (category_name) VALUES (?)";
        int affectedRows = jdbcTemplate.update(sql, tagCategory.getName());

        return affectedRows > 0;
    }

    public Tag getTagCategoryById(int id) {
        String sql = "SELECT * FROM tag_category WHERE cat_id = ?";
        Tag tag = null;

        tag = jdbcTemplate.queryForObject(sql, Tag.class, id);
        return tag;
    }

    public List<TagCategory> getAllTagCategory() {
        String sql = "SELECT * FROM tag_category ORDER BY cat_id";
        List<TagCategory> categories = null;
        categories = jdbcOperations.query(sql, new BeanPropertyRowMapper<>(TagCategory.class));

        return categories;
    }



    public boolean updateTagCategoryById(TagCategory tagCategory, int id) {
        String sql = "UPDATE tag_category SET category_name = ? WHERE cat_id = ?";
        int affectRows = jdbcTemplate.update(sql, tagCategory.getName(), id);

        return affectRows > 0;
    }

    public boolean updateTagCategoryByName(TagCategory tagCategory, String name) {
        String sql = "UPDATE tag_category SET category_name = ? WHERE category_name = ?";
        int affectRows = jdbcTemplate.update(sql, tagCategory, name);

        return affectRows > 0;
    }

    public boolean deleteTagCategoryById(int id) {
        String sql = "DELETE FROM tag_category WHERE cat_id = ?";
        int affectRows = jdbcTemplate.update(sql, id);

        return affectRows > 0;
    }

}
