package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.Entity.ProductListing;
import com.example.gilbertecommerce.Entity.User;
import com.example.gilbertecommerce.Framework.ProductListingRepo;
import com.example.gilbertecommerce.Framework.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final UserService userService;
    private final UserRepo userRepo;
    private final ProductListingRepo productListingRepo;
    public AdminService(UserService userService, UserRepo userRepo, ProductListingRepo productListingRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.productListingRepo = productListingRepo;
    }
    public List<User> getAllUsers(){
        return userRepo.getAllUsers();
    }
    public void approveListing(int id){
        productListingRepo.updateStatus(id, "approved");
    }
    public void rejectListing(int id){
        productListingRepo.updateStatus(id, "rejected");
    }
}
