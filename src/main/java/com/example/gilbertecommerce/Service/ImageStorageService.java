package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.Framework.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Service
public class ImageStorageService {

    private final ImageRepository imgRepo;
    private final Path rootImageDir = Paths.get("src/main/resources/static/images");

    public ImageStorageService(ImageRepository imgRepo) {
        this.imgRepo = imgRepo;
    }

    /*
    The goal of this service is to store img data, in a structured & logical way.
    Default img data is stored here: "src/main/resources/static/images/default-images/imgfile.png"
    Users profile pictures are stored here: "src/main/resources/static/images/uploaded-images/users/{userID}/imgfile.png"

    Since our ProductListings already contains a reference to the user who created it, we can yse that relation to store data more structured
    Listing are therefore stored this way: "src/main/resources/static/images/uploaded-images/listings/{userID}/{listingID}/imgfile.png"
    This allows users to store multiple img files in a single Listing, if that will be need in the future.
     import java.nio.file.* is an an API that allows for easy but basic file management.
    */


    // Save Profile Image (to disk + DB)
    public boolean saveProfileImage(MultipartFile imageFile, int userId) throws SQLException {
        String filename = saveProfileImageToFolder(imageFile, userId);
        return imgRepo.saveProfileImage(userId, filename);
    }

    // This saves image file under "src/main/resources/static/images/uploaded-images/users/{userID}"
    public String saveProfileImageToFolder(MultipartFile imageFile, int userId) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }

        try {
            Path userDir = rootImageDir
                    .resolve("uploaded-images")
                    .resolve("users")
                    .resolve(String.valueOf(userId));

            Files.createDirectories(userDir);

            String originalFilename = imageFile.getOriginalFilename();

            if (originalFilename == null || originalFilename.isBlank()) {
                throw new IllegalArgumentException("Invalid filename");
            }

            Path targetPath = userDir.resolve(originalFilename);
            Files.copy(imageFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return originalFilename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to save profile image to disk", e);
        }
    }

    // Get Profile Image Path with Fallback -> returns a default profile image if none is present
    public String getProfileImagePath(int userId) {
        try {
            return imgRepo.getProfileImageFullPath(userId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve profile image path", e);
        }
    }

    // Save Listing Image (DB + File)
    public String saveListingImage(MultipartFile file, int userId, int listingId, String imageName) throws IOException {
        Path listingDir = rootImageDir
                .resolve("uploaded-images")
                .resolve("listings")
                .resolve(String.valueOf(userId))
                .resolve(String.valueOf(listingId));
        Files.createDirectories(listingDir);

        String filename = imageName + getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        Path targetPath = listingDir.resolve(filename);
        file.transferTo(targetPath.toFile());

        try {
            imgRepo.saveListingImage(listingId, filename);
        } catch (SQLException e) {
            throw new RuntimeException("Could not save listing image in database", e);
        }

        return "/uploads/listings/" + userId + "/" + listingId + "/" + filename;
    }

    // Get Listing Image Paths
    public List<String> getListingImagePaths(int listingId, int userId) {
        return imgRepo.getListingImagePaths(listingId, userId);
    }

    // Delete All Listing Images (DB only)
    public boolean deleteAllListingImages(int listingId) {
        return imgRepo.deleteAllListingImages(listingId);
    }

    // Delete Specific Listing Image (DB + Disk)
    public boolean deleteSpecificListingImage(int listingId, int userId, String filename) {
        Path baseDir = rootImageDir.resolve("uploaded-images");
        return imgRepo.deleteSpecificListingImage(listingId, userId, filename, baseDir);
    }

    // Delete All Listing Images and Files
    public boolean deleteAllListingImagesAndFiles(int listingId, int userId) {
        Path baseDir = rootImageDir.resolve("uploaded-images");
        return imgRepo.deleteAllListingImagesAndALLFiles(listingId, userId, baseDir);
    }

    // Extracts the file extension from the filename, including the dot (e.g., ".png")
    private String getFileExtension(String original) {
        int lastDot = original.lastIndexOf(".");
        return lastDot != -1 ? original.substring(lastDot) : "";
    }
}
