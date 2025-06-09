package com.example.gilbertecommerce.Framework;


import com.example.gilbertecommerce.Entity.TagCategory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagCategoryRepo {

    private JdbcTemplate jdbcTemplate;

    public TagCategoryRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }


    public boolean createTagCategory(TagCategory tagCategory) {
        String sql = "INSERT INTO tag_category (category_name) VALUES (?)";
        int affectedRows = jdbcTemplate.update(sql, tagCategory.getName());

        return affectedRows > 0;
    }

    public TagCategory getTagCategoryById(int id) {
        try {
            String sql = "SELECT * FROM tag_category WHERE cat_id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                TagCategory category = new TagCategory();
                category.setId(rs.getInt("cat_id"));
                category.setName(rs.getString("category_name"));
                return category;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<TagCategory> getAllTagCategory() {
        String sql = "SELECT * FROM tag_category ORDER BY cat_id";
        List<TagCategory> categories = null;
        categories = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TagCategory.class));

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
    public void addTagToListing(int tagId, int listingId) {
        System.out.println("Adding tag " + tagId + " to listing " + listingId + " in TagCategoryRepo");
        String sql = "INSERT INTO product_tags (product_tag, tag_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, listingId, tagId);
    }

}
