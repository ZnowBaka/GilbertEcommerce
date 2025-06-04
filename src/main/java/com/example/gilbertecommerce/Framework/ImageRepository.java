package com.example.gilbertecommerce.Framework;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ImageRepository {

    /*
      This repository class handles all database interactions related to image metadata storage.
      Images themselves are stored on disk, while filenames and references are saved in the DB.
      The structure supports profile images and listing images linked to users and listings.
     */

    private final JdbcTemplate jdbcTemplate;

    public ImageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save or update the filename for a user's profile image in the USER table
    public boolean saveProfileImage(int userId, String filename) throws SQLException {
        String sql = "UPDATE USER SET profile_image_filename = ? WHERE id = ?";

        try {
            int affectedRows = jdbcTemplate.update(sql, filename, userId);
            return affectedRows > 0; // Return true if update was successful
        } catch (DataAccessException e) {
            throw new RuntimeException(e); // Wrap any DB exception into runtime for upper layers
        }
    }

    // Retrieve the full path to a user's profile image or fallback to a default image if none exists
    public String getProfileImageFullPath(int userId) throws SQLException {
        String sql = "SELECT profile_image_filename FROM USER WHERE id = ?";

        try {
            String filename = jdbcTemplate.queryForObject(sql, String.class, userId);

            // If a valid filename exists, construct the URL path
            if (filename != null && !filename.isBlank()) {
                return "/images/uploaded-images/users/" + userId + "/" + filename;
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        // Return default profile image path if no user image found
        return "/images/default-images/default-profile.png";
    }

    // Insert a new listing image filename linked to a specific listing in the listing_images table
    public boolean saveListingImage(int listingId, String filename) throws SQLException {
        String sql = "INSERT INTO listing_images (listing_id, image_filename) VALUES (?, ?)";

        try {
            int affectedRows = jdbcTemplate.update(sql, listingId, filename);
            return affectedRows > 0;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // Get all image paths for a given listing, constructing full paths based on user and listing IDs
    public List<String> getListingImagePaths(int listingId, int userId) {
        String sql = "SELECT image_filename FROM listing_images WHERE listing_id = ?";
        List<String> imagePaths = new ArrayList<>();

        try {
            List<String> filenames = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("image_filename"), listingId);

            for (String filename : filenames) {
                if (filename != null && !filename.isBlank()) {
                    String fullPath = "/images/uploaded-images/listings/" + userId + "/" + listingId + "/" + filename;
                    imagePaths.add(fullPath);
                }
            }
            return imagePaths;

        } catch (DataAccessException e) {
            throw new RuntimeException("Error fetching listing image paths", e);
        }
    }

    // Delete all image entries for a given listing from the DB only (does not delete files from disk)
    public boolean deleteAllListingImages(int listingId) {
        String sql = "DELETE FROM listing_images WHERE listing_id = ?";

        try {
            int affectedRows = jdbcTemplate.update(sql, listingId);
            return affectedRows > 0;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error deleting listing images", e);
        }
    }

    /*
      Delete a specific listing image from both the DB and the file system.
      Construct the file path dynamically from baseDir and user/listing IDs.
      Only delete the file if the DB entry was successfully removed.
    */
    public boolean deleteSpecificListingImage(int listingId, int userId, String filename, Path baseDir) {
        Path imagePath = baseDir
                .resolve("listings")
                .resolve(String.valueOf(userId))
                .resolve(String.valueOf(listingId))
                .resolve(filename);

        try {
            String sql = "DELETE FROM listing_images WHERE listing_id = ? AND image_filename = ?";
            int affectedRows = jdbcTemplate.update(sql, listingId, filename);

            if (affectedRows > 0) {
                Files.deleteIfExists(imagePath); // Delete the physical image file
                return true;
            }
            return false;

        } catch (DataAccessException | IOException e) {
            throw new RuntimeException("Error deleting image: " + filename, e);
        }
    }

    /*
      Delete all listing images and their files for a specific listing.
      First, retrieve all filenames to delete them from disk after deleting DB entries.
      Also attempts to delete the listing's folder recursively if empty.
    */
    public boolean deleteAllListingImagesAndALLFiles(int listingId, int userId, Path baseDir) {
        String sql = "SELECT image_filename FROM listing_images WHERE listing_id = ?";

        try {
            List<String> filenames = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("image_filename"), listingId);

            String deleteSql = "DELETE FROM listing_images WHERE listing_id = ?";
            jdbcTemplate.update(deleteSql, listingId);

            for (String filename : filenames) {
                if (filename != null && !filename.isBlank()) {
                    Path imagePath = baseDir.resolve("listings")
                            .resolve(String.valueOf(userId))
                            .resolve(String.valueOf(listingId))
                            .resolve(filename);
                    Files.deleteIfExists(imagePath);
                }
            }

            deleteListingImageFolder(listingId, userId, baseDir);
            return true;

        } catch (IOException | DataAccessException e) {
            throw new RuntimeException("Error deleting listing images or files", e);
        }
    }

    // Attempts to delete the listing image folder recursively if it exists
    public void deleteListingImageFolder(int listingId, int userId, Path baseDir) {
        Path listingDir = baseDir.resolve("listings")
                .resolve(String.valueOf(userId))
                .resolve(String.valueOf(listingId));

        try {
            if (Files.exists(listingDir)) {
                deleteDirectoryRecursively(listingDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting listing image folder", e);
        }
    }

    // Helper method to recursively delete a directory and all its contents
    private void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (var entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    deleteDirectoryRecursively(entry);
                }
            }
        }
        Files.deleteIfExists(path);
    }
}
