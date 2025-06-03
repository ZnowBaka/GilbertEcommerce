package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.Entity.Tag;
import com.example.gilbertecommerce.Entity.TagCategory;
import com.example.gilbertecommerce.Framework.TagCategoryRepo;
import com.example.gilbertecommerce.Framework.TagRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryTagMapService {

    private final TagRepo tagRepo;
    private final TagCategoryRepo catRepo;

    // Doing this here keeps the controller clean, but man its annoying to map out
    private final Map<String, String> categoryToFormFieldMap = new LinkedHashMap<>();

    private List<Tag> genderTags;
    private List<Tag> designerTags;
    private List<Tag> homeTags;
    private List<Tag> beautyTags;
    private List<Tag> brandTags;
    private List<Tag> conditionTags;
    private List<Tag> clothingTags;
    private List<Tag> accessoryTags;
    private List<Tag> shoeTags;
    private List<Tag> bagsAndLuggageTags;
    private List<Tag> jewelryTags;
    private List<Tag> internationalSizeTags;
    private List<Tag> shoeSizeTags;




    public CategoryTagMapService(TagRepo tagRepo, TagCategoryRepo catRepo) {
       try{
           this.tagRepo = tagRepo;
           this.catRepo = catRepo;

       } catch (Exception e) {
           e.printStackTrace();
           throw e;
       }
    }

    @PostConstruct
    public void init() {
        try {
            // This actually gets the data from DB without killing the program
            System.out.println("Loading Tags from DB into CategoryTagMapService...");
            this.genderTags = tagDataFiller("Gender");
            this.designerTags = tagDataFiller("Designer");
            this.homeTags = tagDataFiller("Home");
            this.beautyTags = tagDataFiller("Beauty");
            this.brandTags = tagDataFiller("Brand");
            this.conditionTags = tagDataFiller("Condition");
            this.clothingTags = tagDataFiller("Clothing");
            this.accessoryTags = tagDataFiller("Accessories");
            this.shoeTags = tagDataFiller("Shoes");
            this.bagsAndLuggageTags = tagDataFiller("Bags & Luggage");
            this.jewelryTags = tagDataFiller("Jewelry");
            this.internationalSizeTags = tagDataFiller("InternationalSize");
            this.shoeSizeTags = tagDataFiller("Shoe Size");

            // Initialize "category to field" mappings, this is to link it with the SearchForm
            System.out.println("Setting up field mappings...");
            categoryToFormFieldMap.put("Gender", "gender");
            categoryToFormFieldMap.put("Designer", "designer");
            categoryToFormFieldMap.put("Home", "home");
            categoryToFormFieldMap.put("Beauty", "beauty");
            categoryToFormFieldMap.put("Brand", "brand");
            categoryToFormFieldMap.put("Condition", "condition");
            categoryToFormFieldMap.put("Clothing", "clothing");
            categoryToFormFieldMap.put("Accessories", "accessories");
            categoryToFormFieldMap.put("Shoes", "shoes");
            categoryToFormFieldMap.put("Bags & Luggage", "bags_and_luggage");
            categoryToFormFieldMap.put("Jewelry", "jewelry");
            categoryToFormFieldMap.put("InternationalSize", "international_size");
            categoryToFormFieldMap.put("Shoe Size", "shoe_size");

            System.out.println("Initialized CategoryTagMapService");
        } catch (Exception e) {
            // Log and fail fast — this will stop Spring Boot if critical
            System.err.println("Failed to initialize CategoryTagMapService: " + e.getMessage());
            throw e;
        }
    }


    // This method should be able to fill our list with the data from DB
    public List<Tag> tagDataFiller(String categoryName) {
        List<Tag> tagsFromDB = tagRepo.getAllTagsFromCategory(categoryName);
        List<Tag> tagsList = new ArrayList<>();

        for (Tag tag : tagsFromDB) {
            tagsList.add(new Tag(tag.getId(), tag.getTagValue()));
        }

        return tagsList;
    }

    // In turn this will be used to "normalise" the SearchForm data
    public Map<String, String> getCategoryToFormFieldMap() {
        return this.categoryToFormFieldMap;
    }


    public Map<String, List<Tag>> buildNormalizedCategoryTagsMap() {
        Map<String, List<Tag>> normalizedMap = new LinkedHashMap<>();
        Map<TagCategory, List<Tag>> originalMap = buildTestCategoryTagsMap();

        for (Map.Entry<TagCategory, List<Tag>> entry : originalMap.entrySet()) {
            String catName = entry.getKey().getName();
            String fieldName = categoryToFormFieldMap.get(catName);
            if (fieldName != null) {
                normalizedMap.put(fieldName, entry.getValue());
            }
        }

        return normalizedMap;
    }

    public List<TagCategory> getAllCategories() {
        List<TagCategory> tagCategoryList = null;
        tagCategoryList = catRepo.getAllTagCategory();

        return tagCategoryList;
    }

    // Section for map building, this is how we can define precisely what tags we want from where, and in what order.
    // By using a LinkedHashMap we can control the order that the Category buttons appear while also able to make predefined templates when you build a Listing
    public Map<TagCategory, List<Tag>> buildTestCategoryTagsMap() {
        Map<TagCategory, List<Tag>> map = new LinkedHashMap<>();
        map.put(new TagCategory("Gender"), genderTags);
        map.put(new TagCategory("Designer"), designerTags);
        map.put(new TagCategory("Brand"), brandTags);
        map.put(new TagCategory("Condition"), conditionTags);
        map.put(new TagCategory("Bags & Luggage"), bagsAndLuggageTags);
        return map;
    }


    public Map<TagCategory, List<Tag>> buildExampleListingTagsMap() {
        Map<TagCategory, List<Tag>> map = new LinkedHashMap<>();
        map.put(new TagCategory("Gender"), genderTags);
        map.put(new TagCategory("Designer"), designerTags);
        map.put(new TagCategory("Brand"), brandTags);
        return map;
    }


}
