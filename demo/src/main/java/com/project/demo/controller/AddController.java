//package com.project.demo.controller;
//
//
//import com.project.demo.entity.Role;
//import com.project.demo.entity.User;
//import com.project.demo.repository.RoleRepository;
//import com.project.demo.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//public class AddController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @PostMapping("/user")
//    public ResponseEntity<User> addingUser(@RequestBody User user)
//    {
//        return ResponseEntity.ok(userService.addUser(user));
//    }
//
//    @GetMapping("/get-data/{rolename}")
//    public ResponseEntity<Role> getAllData(@PathVariable String rolename)
//    {
//        return ResponseEntity.ok(roleRepository.findByRoleName(rolename).orElseThrow(()-> new RuntimeException("User Not Found")));
//    }
//
//}
