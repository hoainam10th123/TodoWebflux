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
@Table(name = "roles")
public class Role {
    @Id
    private Integer id;

    @Column("name")
    private String name;

    public Role(String name){
        this.name = name;
    }
}
