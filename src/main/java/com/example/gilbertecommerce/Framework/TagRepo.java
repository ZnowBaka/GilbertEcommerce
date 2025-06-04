package com.example.gilbertecommerce.Framework;

import com.example.gilbertecommerce.Entity.Tag;
import com.example.gilbertecommerce.Service.LoggerService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepo {

    private JdbcTemplate jdbcTemplate;

    public TagRepo(JdbcTemplate jdbcTemplate, LoggerService logger) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public boolean createTag(Tag tag) {
        String sql = "INSERT INTO tags (tag_value) VALUES (?)";
        int affectedRows = jdbcTemplate.update(sql, tag.getTagValue());

        return affectedRows > 0;
    }

    public Tag getTagById(int id) {
        String sql = "SELECT * FROM tags WHERE tag_id = ?";
        Tag tag = null;

        tag = jdbcTemplate.queryForObject(sql, Tag.class, id);
        return tag;
    }

    public List<Tag> getAllTagsFromCategory(String categoryName) {
        String sql = ("""
                SELECT DISTINCT tag.tag_id, tag.tag_value FROM tags tag
                LEFT JOIN tag_has_category tagConnection ON tag.tag_id = tagConnection.tag_id
                LEFT JOIN tag_category tagCategory ON tagConnection.cat_id = tagCategory.cat_id
                WHERE tagCategory.category_name = ?
                ORDER BY tag.tag_value
                """);

        List<Tag> tags = null;
        tags = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tag.class), categoryName);

        return tags;
    }

    public boolean updateTagById(Tag tag, int id) {
        String sql = "UPDATE tags SET tag_value = ? WHERE tag_id = ?";
        int affectRows = jdbcTemplate.update(sql, tag.getTagValue(), id);

        return affectRows > 0;
    }

    public boolean updateTagByName(Tag tag, String name) {
        String sql = "UPDATE tags SET tag_value = ? WHERE tag_value = ?";
        int affectRows = jdbcTemplate.update(sql, tag.getTagValue(), name);

        return affectRows > 0;
    }

    public boolean deleteTagById(int id) {
        String sql = "DELETE FROM tags WHERE tag_id = ?";
        int affectRows = jdbcTemplate.update(sql, id);

        return affectRows > 0;
    }


}
