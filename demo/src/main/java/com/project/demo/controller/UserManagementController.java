package com.project.demo.controller;

import com.project.demo.dto.ReqRes;
import com.project.demo.entity.User;
import com.project.demo.service.UserManagementSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserManagementController {
    @Autowired
    private UserManagementSystem userManagementSystem;

    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> registration(@RequestBody ReqRes dto)
    {
        return ResponseEntity.ok(userManagementSystem.register(dto));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes dto)
    {
        return ResponseEntity.ok(userManagementSystem.login(dto));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUser()
    {
        return ResponseEntity.ok(userManagementSystem.getAllUsers());
    }

    @GetMapping("/admin/get-user/{id}")
    public ResponseEntity<ReqRes> getUserId(@PathVariable Integer id)
    {
        return ResponseEntity.ok(userManagementSystem.getUserById(id));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<ReqRes> deleteUserById(@PathVariable Integer id)
    {
        return ResponseEntity.ok(userManagementSystem.deleteUser(id));
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<ReqRes> updateUserById(@PathVariable Integer id, @RequestBody User dto)
    {
        return ResponseEntity.ok(userManagementSystem.updateUser(id,dto));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(userManagementSystem.getMyInfo(email));
    }
}
