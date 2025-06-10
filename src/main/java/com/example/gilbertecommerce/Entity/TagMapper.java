package com.example.gilbertecommerce.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class TagMapper implements org.springframework.jdbc.core.RowMapper<Tag> {


    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();

        tag.setId(rs.getInt("tag_id"));
        tag.setTagValue(rs.getString("tag_value"));
        tag.setCategory(rs.getInt("cat_id"));

        if (tag.getCategory() == 2) {
            tag.setBrand(tag.getTagValue());
        }
        return tag;
    }
}
