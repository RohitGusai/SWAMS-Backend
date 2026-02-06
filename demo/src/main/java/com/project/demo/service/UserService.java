//package com.project.demo.service;
//
//import com.project.demo.entity.Role;
//import com.project.demo.entity.User;
//import com.project.demo.repository.RoleRepository;
//import com.project.demo.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    public User addUser(User user)
//    {
//        Role existingUser = roleRepository.findByRoleName(user.getRole().getRoleName()).orElseThrow(() -> new RuntimeException("Role not found"));;
//        existingUser.addUser(user);
//        return userRepository.save(user);
//    }
//
//    public Role getData(String rolename)
//    {
//        Role existingUser = roleRepository.findByRoleName(rolename).orElseThrow(() -> new RuntimeException("Role not found"));;
//        existingUser.findUser(existingUser.getId());
//    }
//}
