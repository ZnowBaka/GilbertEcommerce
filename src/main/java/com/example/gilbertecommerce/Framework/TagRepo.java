package com.example.gilbertecommerce.Framework;

import com.example.gilbertecommerce.Entity.Tag;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepo {

    private JdbcTemplate jdbcTemplate;

    public TagRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public boolean createTag(Tag tag) {
        String sql = "INSERT INTO tags (tag_value) VALUES (?)";
        int affectedRows = jdbcTemplate.update(sql, tag.getTagValue());

        return affectedRows > 0;
    }

    public Tag getTagById(int id) {
        try {
            String sql = "SELECT * FROM tags WHERE tag_id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Tag tag = new Tag();
                tag.setId(rs.getInt("tag_id"));
                tag.setTagValue(rs.getString("tag_value"));
                return tag;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /*
     Added some more SQL to order the tag values correctly
     The "^" sign ensure that the ordering happen from the first char.
     The [0-9] Checks for chars matching in that range I.E Numbers.
     The "+" sign here checks for more digits in the String.
     The "$" properly does the sam thing it does in the like %STRING% SQL, but idk what that is, likely just a stop block.
     This should then first check for only numbers, then mixed numbers and then no numbers.
     */
    public List<Tag> getAllTagsFromCategory(String categoryName) {
        String sql = ("""
                SELECT DISTINCT tag.tag_id, tag.tag_value FROM tags tag
                LEFT JOIN tag_has_category tagConnection ON tag.tag_id = tagConnection.tag_id
                LEFT JOIN tag_category tagCategory ON tagConnection.cat_id = tagCategory.cat_id
                WHERE tagCategory.category_name = ?
                ORDER BY tag.tag_value REGEXP '^[0-9]+$' DESC, tag.tag_value + 0, tag.tag_value
                """);


        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Tag tag = new Tag();
            tag.setId(rs.getInt("tag_id"));           // map tag_id to id explicitly
            tag.setTagValue(rs.getString("tag_value"));
            return tag;
        }, categoryName);
    }

    public List<Tag> getTagsByListingId(int listingId) {
        String sql = """
                    SELECT tag.tag_id AS tagId, tag.tag_value AS tagValue
                    FROM tags tag
                             JOIN product_tags tagConnection ON tag.tag_id = tagConnection.tag_id
                    WHERE tagConnection.product_tag = ?
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tag.class), listingId);
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
