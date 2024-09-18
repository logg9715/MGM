package com.example.MultiGreenMaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usermember")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ToString
public class UserENT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String name;

    private String phonenumber;
    private String email;
    private User_RoleENUM role;
    private boolean disable;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    @Builder.Default
    private Set<FriendENT> friends = new HashSet<>();

    public void patch(UserENT newData) {
        if (newData.loginId != null) this.loginId = newData.loginId;
        if (newData.password != null) this.password = newData.password;
        if (newData.nickname != null) this.nickname = newData.nickname;
        if (newData.name != null) this.name = newData.name;
        if (newData.phonenumber != null) this.phonenumber = newData.phonenumber;
        if (newData.email != null) this.email = newData.email;
        if (newData.role != null) this.role = newData.role;
    }

    public void guestBookPatch() {
        this.password = null;
        this.name = null;
        this.phonenumber = null;
        this.email = null;
    }
}
