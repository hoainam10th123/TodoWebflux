package com.example.demowebflux.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "users_roles")
public class UserRole {
    @Id
    private Integer id;

    @Column("user_id")
    private Integer userId;

    @Column("role_id")
    private Integer roleId;

    public UserRole(int roleId, int userId){
        this.userId = userId;
        this.roleId = roleId;
    }
}
