package com.example.demowebflux.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TodoDto {
    private Integer id;

    @Length(min = 3, message = "Minimum is 3 character")
    @NotEmpty(message = "Required title")
    private String title;

    private String description;

    private boolean completed;

    private String username;
}
