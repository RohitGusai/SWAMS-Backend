package com.project.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Getter
@Setter
@Table(name="users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String email;
    private String password;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId", nullable=false)
//    @JsonBackReference
//    @JsonIgnore
    @JsonIgnoreProperties("user")
    @JsonIgnore
    private Role role;

    @OneToMany(mappedBy = "requester" ,cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<WorkFlowRequest> requests = new ArrayList<>();

    @OneToMany(mappedBy = "approver")
    @JsonIgnore
    private List<ApprovalHistory> approvals = new ArrayList<>();

    @OneToMany(mappedBy = "performed")
    @JsonIgnore
    private List<AuditLog> perform = new ArrayList<>();

}
