package com.example.demowebflux.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {
    private Integer id;

    private String username;

    private String displayName;

    private String token;

    public UserDto(int id, String username, String displayName, String token){
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.token = token;
    }
}
