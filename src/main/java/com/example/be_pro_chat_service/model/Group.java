package com.example.be_pro_chat_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "bp_groups")
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;
    private String adminUsername;

    @ElementCollection
    @CollectionTable(name = "group_users", joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "username")
    private List<String> users = new ArrayList<>();


}
