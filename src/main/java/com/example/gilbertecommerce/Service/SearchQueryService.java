package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.Entity.SearchForm;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchQueryService {
    /*
    So this class turned out differently from expected...
    Originally we wanted to just use a giant Query that took the entire Form and used every part  from it, -
    but that would be a huge query that is not really flexible enough to suit our needs.

    Therefor we made a new type, that also has to deal with the form names & DB names for categories not being a 1-1...
     */

    private final StringBuilder sql;
    private final List<Object> params;


    public SearchQueryService() {
        // Start with a safe default condition, again no clue why this is needed
        this.sql = new StringBuilder("WHERE 1=1");
        this.params = new ArrayList<>();
    }

    // This is kinda the same map solution that we use int the CategoryTagMapService to normalize names.
    // The reason why it is in uppercase is that it mimics how enums also could be used here instead?
    private static final Map<String, String> TAG_FILTERS = Map.of(
            "clothing", "Clothing",
            "accessories", "Accessories",
            "shoes", "Shoes",
            "bags_and_luggage", "Bags & Luggage",
            "jewelry", "Jewelry",
            "home", "Home",
            "beauty", "Beauty"
    );

    /*
    So instead of using a Billion appends that needs to be handmade for each category adding more indexes...
    We just map everything together, this way we, like in the CategoryTagMapService, we can easily just add more -
    TagCategories, as we map what they are called in the program vs the DB.
     */
    public void buildFromForm(SearchForm form) {
        System.out.println("Started Building Form: ");
        addSearchText(form.getSearchText());
        addTagFilter(form.getGender(), "Gender");
        addTagFilter(form.getDesigner(), "Designer");
        addTagFilter(form.getHome(), "Home");
        addTagFilter(form.getBeauty(), "Beauty");
        addTagFilter(form.getBrand(), "Brand");
        addTagFilter(form.getCondition(), "Condition");
        addTagFilter(form.getAccessories(), "Accessories");
        addTagFilter(form.getShoes(), "Shoes");
        addTagFilter(form.getBags_and_luggage(), "Bags & Luggage");
        addTagFilter(form.getJewelry(), "Jewelry");
        addTagFilter(form.getInternational_size(), "International Size");
        addTagFilter(form.getShoe_size(), "Shoe Size");

        // Add all mapped category filters (like the "Bags & Luggage" example)
        for (Map.Entry<String, String> entry : TAG_FILTERS.entrySet()) {
            String formField = entry.getKey();
            String tagCategory = entry.getValue();
            String fieldValue = getFieldValue(form, formField);

            if (fieldValue != null && !fieldValue.isBlank()) {
                // This is where each entry that is filled from the SearchForm should create an appended sql String -
                // That has been mapped correctly hopefully
                addTagFilter(fieldValue, tagCategory);
            }
        }
    }

    // Previously this was at the very top, but just underneath, the giant query block; it is just for product name searching.
    private void addSearchText(String searchText) {
        if (searchText != null && !searchText.isBlank()) {
            sql.append(" AND productListing.title LIKE ? ");
            params.add("%" + searchText.trim() + "%");
        }
    }

    /**
     * Adds a tag existence filter, assuming the field maps to tag value under a specific level.
     * Filter listings that have the given tag value under the specified tag category.
     * This uses EXISTS to ensure the listing has at least one matching tag in that category.
     * Joins:
     * - product_tags links listings to tags
     * - tags holds tag values
     * - tag_has_category links tags to categories
     * - tag_category holds category names
     */

    private void addTagFilter(String tagValue, String tagCategory) {
        if (tagValue == null || tagValue.isBlank()) {
            return;
        }
        // If you are confused as to why we start from the "product_tags" instead of the listing it is because that will -
        // be called from the controller, it is because like the TagCategoryMaps, you can define specific searches this way
        // for future use, other than that don't ask me why.
        // Also left joins are not needed here because we specifically look for Listing with tag relations.
        sql.append("""
          AND EXISTS (
         SELECT 1
         FROM product_tags connectedTags
          JOIN tags tag ON connectedTags.tag_id = tag.tag_id
         JOIN tag_has_category connectedCategory ON tag.tag_id = connectedCategory.tag_id
         JOIN tag_category tagCategory ON connectedCategory.cat_id = tagCategory.cat_id
         WHERE tagCategory.category_name = ?
         AND tag.tag_value = ?
        )
        """);
        params.add(tagCategory);
        params.add(tagValue);
    }

    /**
     * Unrefined explanation
     * Uses Java reflection to dynamically extract the value of a field from the SearchForm instance.
     * Instead of manually calling form.getBags_and_luggage(), form.getShoes(), etc. for every single field, reflection lets you access any field dynamically by its name as a string.
     * This helps when you have a map (TAG_FILTERS) that relates form field names to database category names, so you can loop through them and get the value without a huge if or switch-case.
     */
    private String getFieldValue(SearchForm form, String fieldName) {
        try {
            // This selects the specific "field" that matches the values from the SearchForm.
            Field field = SearchForm.class.getDeclaredField(fieldName);

            // This is just a small "check" to ensure that data accessible.
            field.setAccessible(true);

            // Returns value as a "cast" String value.
            return (String) field.get(form);
        } catch (Exception e) {
            return null;
        }
    }

    public String getSql() {
        return sql.toString();
    }

    public List<Object> getParams() {
        return params;
    }
}