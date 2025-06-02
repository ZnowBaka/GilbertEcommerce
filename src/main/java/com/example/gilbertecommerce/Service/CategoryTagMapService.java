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

            System.out.println("Initialized CategoryTagMapService");
        } catch (Exception e) {
            // Log and fail fast — this will stop Spring Boot if critical
            System.err.println("Failed to initialize CategoryTagMapService: " + e.getMessage());
            throw e;
        }
    }

    public List<TagCategory> getAllCategories() {
        List<TagCategory> tagCategoryList = null;
        tagCategoryList = catRepo.getAllTagCategory();

        return tagCategoryList;
    }

    // This method should be able to fill our list
    public List<Tag> tagDataFiller(String categoryName) {
        List<Tag> tagsFromDB = tagRepo.getAllTagsFromCategory(categoryName);
        List<Tag> tagsList = new ArrayList<>();

        for (Tag tag : tagsFromDB) {
            tagsList.add(new Tag(tag.getId(), tag.getTagValue()));
        }

        return tagsList;
    }


    // Section for map building, this is how we can define precisely what tags we want from where, and in what order.
    // By using a LinkedHashMap we can control the order that the Category buttons appear while also able to make predefined templates when you build a Listing
    public Map<TagCategory, List<Tag>> buildTestCategoryTagsMap() {
        Map<TagCategory, List<Tag>> map = new LinkedHashMap<>();
        map.put(new TagCategory("Gender"), genderTags);
        map.put(new TagCategory("Designer"), designerTags);
        map.put(new TagCategory("Brand"), brandTags);
        map.put(new TagCategory("Condition"), conditionTags);
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
