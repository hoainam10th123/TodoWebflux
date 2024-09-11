package com.example.demowebflux.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private Integer id;

    @Column("username")
    private String username;

    @Column("display_name")
    private String displayName;

    @Column("password")
    private String password;

    public User(String displayName, String username, String password){
        this.username = username;
        this.displayName = displayName;
        this.password = password;
    }
}
