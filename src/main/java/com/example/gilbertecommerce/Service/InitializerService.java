package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.Entity.ProductListing;
import com.example.gilbertecommerce.Entity.Tag;
import com.example.gilbertecommerce.Entity.TagCategory;
import com.example.gilbertecommerce.Entity.TagInsertForm;
import com.example.gilbertecommerce.Framework.TagCategoryRepo;
import com.example.gilbertecommerce.Framework.TagRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InitializerService {

    private final TagRepo tagRepo;
    private final TagCategoryRepo catRepo;
    private final ProductListingService listingService;

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

    private List<ProductListing> approvedListings;
    private List<ProductListing> featuredListings;


    public InitializerService(TagRepo tagRepo, TagCategoryRepo catRepo, ProductListingService listingService) {

        try {
            this.tagRepo = tagRepo;
            this.catRepo = catRepo;
            this.listingService = listingService;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostConstruct
    public void init() {
        try {
            // Finishes the constructor before the rest of the calls
            System.out.println("Loading Tags from DB into the program...");
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
            this.approvedListings = listingService.getAllApprovedListings();
            this.featuredListings = listingService.getAllFeaturedListings();

            // Starts Mapping
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

            // Adds Tags and Brands & Owner's Display names to Listings needed for front page
            // Add Owners DisplayName to productListing
            System.out.println("Setting up Listings...");
            listingService.getListingOwnerNameByListingId(approvedListings);
            listingService.getListingOwnerNameByListingId(featuredListings);

            // Gets the tags for each Listing and add their tags.
            for (ProductListing product : approvedListings) {
                List<Tag> approvedListingsTags = getTagsByListingId(product.getListingID());
                product.setTags(approvedListingsTags);
            }

            for (ProductListing product : featuredListings) {
                List<Tag> featuredListingsTags = getTagsByListingId(product.getListingID());
                product.setTags(featuredListingsTags);
            }


            System.out.println("Program Initialized successfully");
        } catch (Exception e) {
            System.err.println("Error in InitializerService.init(): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Program", e);
        }
    }


    // This method should be able to fill our list with the data from DB
    public List<Tag> tagDataFiller(String categoryName) {
        List<Tag> tagsFromDB = tagRepo.getAllTagsFromCategory(categoryName);
        List<Tag> tagsList = new ArrayList<>();

        for (Tag tag : tagsFromDB) {
            String formattedValue = formatTagValue(tag.getTagValue());
            tagsList.add(new Tag(tag.getId(), formattedValue));
        }

        return tagsList;
    }

    public Map<String, String> getCategoryToFormFieldMap() {
        return this.categoryToFormFieldMap;
    }

    public List<ProductListing> getApprovedListings() {
        return approvedListings;
    }

    public void setApprovedListings(List<ProductListing> approvedListings) {
        this.approvedListings = approvedListings;
    }

    public List<ProductListing> getFeaturedListings() {
        return featuredListings;
    }

    public void setFeaturedListings(List<ProductListing> featuredListings) {
        this.featuredListings = featuredListings;
    }

    // In turn this will be used to "normalise" the SearchForm data
    public HashMap<String, List<Tag>> normalizeTagsMap(Map<TagCategory, List<Tag>> originalMap) {
        LinkedHashMap<String, List<Tag>> normalizedMap = new LinkedHashMap<>();

        for (Map.Entry<TagCategory, List<Tag>> entry : originalMap.entrySet()) {
            String catName = entry.getKey().getName();
            String fieldName = categoryToFormFieldMap.get(catName);
            if (fieldName != null) {
                normalizedMap.put(fieldName, entry.getValue());
            }
        }

        return normalizedMap;
    }

    private String formatTagValue(String raw) {
        return Arrays.stream(raw.split("_"))
                .filter(part -> !part.isBlank())
                .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1))
                .collect(Collectors.joining(" "));
    }

    public List<TagCategory> getAllCategories() {
        List<TagCategory> tagCategoryList = null;
        tagCategoryList = catRepo.getAllTagCategory();

        return tagCategoryList;
    }

    public List<Tag> getTagsByListingId(int listingId) {
        List<Tag> tagList = new ArrayList<>();
        tagList = tagRepo.getTagsByListingId(listingId);

        return tagList;
    }


    // Section for map building, this is how we can define precisely what tags we want from where, and in what order.
    // By using a LinkedHashMap we can control the order that the Category buttons appear while also able to make predefined templates when you build a Listing
    public Map<TagCategory, List<Tag>> buildStandardSearchFilterTagsMap() {
        Map<TagCategory, List<Tag>> map = new LinkedHashMap<>();
        map.put(new TagCategory("Gender"), genderTags);
        map.put(new TagCategory("Condition"), conditionTags);
        map.put(new TagCategory("Brand"), brandTags);
        map.put(new TagCategory("Designer"), designerTags);
        map.put(new TagCategory("Clothing"), clothingTags);
        map.put(new TagCategory("InternationalSize"), internationalSizeTags);
        map.put(new TagCategory("Shoes"), shoeTags);
        map.put(new TagCategory("Shoe Size"), shoeSizeTags);
        map.put(new TagCategory("Accessories"), accessoryTags);
        map.put(new TagCategory("Bags & Luggage"), bagsAndLuggageTags);
        map.put(new TagCategory("Beauty"), beautyTags);
        map.put(new TagCategory("Home"), homeTags);

        return map;
    }

    public Map<TagCategory, List<Tag>> buildGenericClothing() {
        Map<TagCategory, List<Tag>> map = new LinkedHashMap<>();
        map.put(new TagCategory("Clothing"), clothingTags);
        map.put(new TagCategory("InternationalSize"), internationalSizeTags);
        map.put(new TagCategory("Gender"), genderTags);
        map.put(new TagCategory("Condition"), conditionTags);
        map.put(new TagCategory("Brand"), brandTags);
        map.put(new TagCategory("Designer"), designerTags);
        map.put(new TagCategory("Clothing"), clothingTags);
        map.put(new TagCategory("InternationalSize"), internationalSizeTags);

        return map;
    }

    public Map<TagCategory, List<Tag>> buildGenericFootWear() {
        Map<TagCategory, List<Tag>> map = new LinkedHashMap<>();
        map.put(new TagCategory("Shoes"), shoeTags);
        map.put(new TagCategory("Shoe Size"), shoeSizeTags);
        map.put(new TagCategory("Gender"), genderTags);
        map.put(new TagCategory("Condition"), conditionTags);
        map.put(new TagCategory("Brand"), brandTags);
        map.put(new TagCategory("Designer"), designerTags);

        return map;
    }

    public Map<TagCategory, List<Tag>> buildGenericAccessory() {
        Map<TagCategory, List<Tag>> map = new LinkedHashMap<>();
        map.put(new TagCategory("Accessories"), accessoryTags);
        map.put(new TagCategory("Gender"), genderTags);
        map.put(new TagCategory("Condition"), conditionTags);
        map.put(new TagCategory("Brand"), brandTags);
        map.put(new TagCategory("Designer"), designerTags);

        return map;
    }

    public Map<TagCategory, List<Tag>> buildGenericLuggage() {
        Map<TagCategory, List<Tag>> map = new LinkedHashMap<>();
        map.put(new TagCategory("Bags & Luggage"), bagsAndLuggageTags);
        map.put(new TagCategory("InternationalSize"), internationalSizeTags);
        map.put(new TagCategory("Gender"), genderTags);
        map.put(new TagCategory("Condition"), conditionTags);
        map.put(new TagCategory("Brand"), brandTags);
        map.put(new TagCategory("Designer"), designerTags);

        return map;
    }



    public List<Tag> getTagsBySelection(TagInsertForm form, Map<String, List<Tag>> map) {
        List<Tag> result = new ArrayList<>();

        for (Map.Entry<String, String> entry : form.getTagSelections().entrySet()) {
            String categoryKey = entry.getKey();
            String selectedValue = entry.getValue();

            List<Tag> tags = map.get(categoryKey);

            for (Tag tag : tags) {
                if (tag.getTagValue().trim().equalsIgnoreCase(selectedValue.trim())) {
                    System.out.println("Match found: " + tag.getTagValue());
                    result.add(tag);
                    break;
                }
            }
        }

        return result;
    }
}
