package com.example.demowebflux.services;

import com.example.demowebflux.dtos.LoginDto;
import com.example.demowebflux.dtos.UserDto;
import com.example.demowebflux.entities.User;
import com.example.demowebflux.repositories.UserRepository;
import com.example.demowebflux.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final ReactiveAuthenticationManager authenticationManager;

    private final ModelMapper modelMapper;

    public Mono<UserDto> login(LoginDto loginDto){

        // Tạo token dựa trên authentication
        return this.authenticationManager
               .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()))
                .map(jwtTokenProvider::generateToken)
        .map(jwt -> {
            Mono<User> userDb = userRepository.findByUsername(loginDto.getUsername());
                User user = userDb.block();
                UserDto returnUserDto = modelMapper.map(user, UserDto.class);

                return  new UserDto(returnUserDto.getId(),
                        returnUserDto.getUsername(),
                        returnUserDto.getDisplayName(),
                        jwt);
            });
    }

    public Mono<UserDto> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> userRepository.findByUsername(auth.getName())
                        .map(user -> modelMapper.map(user, UserDto.class))
                );
    }
}
