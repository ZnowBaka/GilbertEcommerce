package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.Entity.Tag;
import com.example.gilbertecommerce.Entity.TagCategory;
import com.example.gilbertecommerce.Framework.TagCategoryRepo;
import com.example.gilbertecommerce.Framework.TagRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryTagMapService {

    private final TagRepo tagRepo;
    private final TagCategoryRepo catRepo;
    private final LoggerService logger;

    public CategoryTagMapService(TagRepo tagRepo, TagCategoryRepo catRepo, LoggerService logger) {
        this.tagRepo = tagRepo;
        this.catRepo = catRepo;
        this.logger = logger;
    }

    // Tag Categories are filled in from here
    // It is very important that the names are 1-1 with the DB.
    private final List<Tag> genderTags = tagDataFiller("Gender");

    private final List<Tag> designerTags = tagDataFiller("Designer");

    private final List<Tag> homeTags = tagDataFiller("Home");

    private final List<Tag> beautyTags = tagDataFiller("Beauty");

    private final List<Tag> brandTags = tagDataFiller("Brand");

    private final List<Tag> conditionTags = tagDataFiller("Condition");

    private final List<Tag> clothingTags = tagDataFiller("Clothing");

    private final List<Tag> accessoryTags = tagDataFiller("Accessories");

    private final List<Tag> shoeTags = tagDataFiller("Shoes");

    private final List<Tag> bagsAndLuggageTags = tagDataFiller("Bags & Luggage");

    private final List<Tag> jewelryTags = tagDataFiller("Jewelry");

    private final List<Tag> internationalSizeTags = tagDataFiller("InternationalSize");

    private final List<Tag> shoeSizeTags = tagDataFiller("Shoe Size");


    public List<TagCategory> getAllCategories() {
        List<TagCategory> tagCategoryList = null;
        tagCategoryList = catRepo.getAllTagCategory();

        return tagCategoryList;
    }

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
    public Map<TagCategory, List<Tag>> buildCategoryTagsMap() {
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
