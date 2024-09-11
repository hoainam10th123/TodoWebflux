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
@AllArgsConstructor
@Table(name = "todos")
public class Todo {
    @Id
    private Integer id;

    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("completed")
    private boolean completed;

    @Column("username")
    private String username;
}
