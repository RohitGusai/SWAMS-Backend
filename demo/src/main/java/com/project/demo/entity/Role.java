package com.project.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String roleName;

    @OneToMany(mappedBy = "role")
//    @JsonManagedReference
    @JsonIgnoreProperties("role")
    private Set<User> user = new HashSet<>();

    public void addUser(User newUser)
    {
        if(newUser != null)
        {

            this.user.add(newUser);
            newUser.setRole(this);
        }
    }


}
