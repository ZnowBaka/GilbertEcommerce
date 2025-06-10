package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.Entity.SearchForm;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class SearchQueryService {
    /*
    So this class turned out differently from expected...
    Originally we wanted to just use a giant Query that took the entire Form and used every part from it, -
    but that would be a huge query that is not really flexible enough to suit our needs.

    Therefor we made a new type, that also has to deal with the form names & DB names for categories not being a 1-1...
     */

    private final StringBuilder sql;
    private final List<Object> params;
    private final Set<String> uniqueTagFilters = new HashSet<>();


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
        clear(); // Another chance to ensure the filter is clean.
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
     * Adds a tag existence filter:
     * This simple Filter makes sure that any appended SQL is the exact same, utilising both the safe start trick 1=1 and the
     * EXISTS tag, this ensures that the searched listing has at least one matching tag in that category.
     * Joins:
     * - product_tags links listings to tags
     * - tags holds tag values
     * - tag_has_category links tags to categories
     * - tag_category holds category names
     */

    private void addTagFilter(String tagValue, String tagCategory) {
        if (tagValue == null || tagValue.isBlank()) return;

        String key = tagCategory + "|" + tagValue;
        if (!uniqueTagFilters.add(key)) return; // Already added, skip

        sql.append("""
                 AND EXISTS (
                SELECT 1
                FROM product_tags pt
                JOIN tags t ON pt.tag_id = t.tag_id
                JOIN tag_has_category thc ON t.tag_id = thc.tag_id
                JOIN tag_category tc ON thc.cat_id = tc.cat_id
                WHERE pt.product_tag = productListing.listing_id
                AND tc.category_name = ?
                AND t.tag_value = ?
                )
                """);
        params.add(tagCategory);
        params.add(tagValue);
    }

    /**
     * Unrefined explanation:
     * Uses Java reflection to dynamically extract the value of a field from the SearchForm instance.
     * Instead of manually calling form.getBags_and_luggage(), form.getShoes(), etc. for every single field, -
     * reflection lets you access any field dynamically by its name as a string.
     * This allows us to Map stuff as <String,<String>> - like our (TAG_FILTERS), it remaps data from the frontend, and the backend, into something the DB recognises.
     * That way we can simply loop values without a huge if or switch-case. Very smart!
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

    public void clear() {
        sql.setLength(0);           // Setting the size to 0 works, it clears the whole sql
        sql.append("WHERE 1=1");    // We need to re-add the constructor build statement otherwise sql will die
        params.clear();             // Clears parameter list, ready to be filled out again.
        uniqueTagFilters.clear();   // This is now needed after we added the (!uniqueTagFilters.add(key)) to prevent duplicates, if not cleared correctly the form skipped values.
    }


}