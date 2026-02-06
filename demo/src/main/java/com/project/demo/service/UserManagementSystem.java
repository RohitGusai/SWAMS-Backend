package com.project.demo.service;

import com.project.demo.dto.ReqRes;
import com.project.demo.dto.UserDto;
import com.project.demo.entity.Role;
import com.project.demo.entity.User;
import com.project.demo.repository.RoleRepository;
import com.project.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserManagementSystem {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RoleRepository roleRepository;



    public ReqRes register(ReqRes requestResigter)
    {
        ReqRes res = new ReqRes();

        try {
            User user = new User();
            user.setEmail(requestResigter.getEmail());
            user.setPassword(passwordEncoder.encode(requestResigter.getPassword()));
            if (requestResigter.getRole() == null) {
                throw new IllegalArgumentException("Role must not be null");
            }

            Optional<User> emailcheck = userRepository.findByEmail(requestResigter.getEmail());

            if (emailcheck.isPresent()) {
                throw new RuntimeException("Email already registered");
            }

            Role role = roleRepository
                    .findByRoleName(requestResigter.getRole().toUpperCase())
                        .orElseThrow(() -> new RuntimeException("Role not found"));

            user.setRole(role);
            user.setName(requestResigter.getName());

            User ourUserResult = userRepository.save(user);

            if (ourUserResult.getId() > 0) {
                ourUserResult.setPassword(null);
                res.setUser(ourUserResult);
                res.setMessage("User Saved Successfully");
                res.setStatusCode(200);
            }
        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setError(e.getMessage());
        }
        return res;

    }


    public ReqRes login(ReqRes loginRequest)
    {
        ReqRes loginRes = new ReqRes();
        try {
            System.out.println("Login attempt for email: " + loginRequest.getEmail());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));
            System.out.println("Hello");
            Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
            if(user.isPresent())
            {
                User unWrappeUser = user.get();
                var jwt = jwtUtil.generateToken(unWrappeUser);
                loginRes.setStatusCode(200);
//                loginRes.setRoles(String.valueOf(unWrappeUser.getRoles()));
                loginRes.setToken(jwt);
                String roles = unWrappeUser.getRole().getRoleName();
//                        .stream()
//                        .findFirst()
//                        .map(role -> role.getName())
//                        .orElse("User");
                loginRes.setUserId(unWrappeUser.getId());
                loginRes.setRole(roles);
                loginRes.setExpirationTime("5 Minute");
                loginRes.setMessage("Successfully Login");
            }
            else
            {
                loginRes.setStatusCode(500);
                loginRes.setMessage("User not Found");
            }

        } catch (Exception e) {
            loginRes.setStatusCode(500);
            loginRes.setMessage(e.getMessage());
        }
        return loginRes;
    }

    public ReqRes getAllUsers() {
        try {
            List<User> users = userRepository.findAll();

            if (users.isEmpty()) {
                return ReqRes.builder()
                        .StatusCode(404)
                        .error("No users found")
                        .build();
            }

            List<UserDto> userDTOList = users.stream()
                    .map(user -> UserDto.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .email(user.getEmail())
                            .role(user.getRole().getRoleName())
                            .build())
                    .toList();

            return ReqRes.builder()
                    .StatusCode(200)
                    .message("Users fetched successfully")
                    .userList(userDTOList)
                    .build();

        } catch (Exception e) {
            return ReqRes.builder()
                    .StatusCode(500)
                    .error(e.getMessage())
                    .build();
        }
    }



    public ReqRes getUserById(Integer id)
    {
        ReqRes userById = new ReqRes();
        try{
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));

            userById.setUser(user);
            userById.setStatusCode(400);
            userById.setMessage("Users with id '" + id + "' found successfully");

        } catch (Exception e) {
            userById.setStatusCode(500);
            userById.setMessage(e.getMessage());
        }
        return userById;
    }

    public ReqRes deleteUser(Integer id)
    {
        ReqRes res = new ReqRes();
        try{
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            if(user != null)
            {
                userRepository.deleteById(id);
                res.setStatusCode(200);
                res.setMessage("user Deleted Successfully");
            }
        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    public ReqRes updateUser(Integer id, User updateUser)
    {
        ReqRes res = new ReqRes();
        try{
            User existingUser = userRepository.findById(id).orElseThrow(()->new RuntimeException("User Not Found"));
            existingUser.setName(updateUser.getName());
            existingUser.setEmail(updateUser.getEmail());
            if (updateUser.getPassword() != null && !updateUser.getPassword().isBlank()) {
                existingUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
            }
            if (updateUser.getRole() != null) {
                existingUser.setRole(updateUser.getRole());
            }
            User saved = userRepository.save(existingUser);
            res.setUser(saved);
            res.setStatusCode(200);
            res.setMessage("User Updated Successfully");
        } catch (Exception e) {
            res.setStatusCode(500);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    public ReqRes getMyInfo(String email)
    {
        ReqRes req = new ReqRes();
        try{
            Optional<User> userEmail =  userRepository.findByEmail(email);
            if(userEmail.isPresent())
            {
                User temp = userEmail.get();
                String roles = temp.getRole().toString();
//                        .stream()
//                        .findFirst()
//                        .map(role -> role.getName())
//                        .orElse("User");
                req.setRole(roles);
                req.setUser(userEmail.get());
                req.setStatusCode(200);
                req.setMessage("Successful");

            }
            else {
                req.setStatusCode(404);
                req.setMessage("User not found for update");
            }


        }
        catch (Exception e)
        {
            req.setStatusCode(500);
            req.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return req;
    }
}
