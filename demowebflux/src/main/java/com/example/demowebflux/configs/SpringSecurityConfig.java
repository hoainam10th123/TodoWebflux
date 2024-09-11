package com.example.demowebflux.configs;


import com.example.demowebflux.repositories.RoleRepository;
import com.example.demowebflux.repositories.UserRepository;
import com.example.demowebflux.repositories.UserRoleRepository;
import com.example.demowebflux.security.JwtTokenAuthenticationFilter;
import com.example.demowebflux.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(useAuthorizationManager=true)
public class SpringSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         JwtTokenProvider tokenProvider,
                                                         ReactiveAuthenticationManager reactiveAuthenticationManager){

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(it -> it
                        //.pathMatchers(HttpMethod.GET, PATH_POSTS).permitAll()
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/api/errors/**").permitAll()
                        .pathMatchers("**").authenticated()
                        //.pathMatchers("/users/{user}/**").access(this::currentUserMatchesPath)
                        //.anyExchange().permitAll()
                )
                .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();
    }

//    private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication,
//                                                               AuthorizationContext context) {
//
//        return authentication
//                .map(a -> context.getVariables().get("user").equals(a.getName()))
//                .map(AuthorizationDecision::new);
//
//    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(UserRepository users,
                                                         UserRoleRepository userRoleRepository,
                                                         RoleRepository roleRepository) {

        return username -> users.findByUsername(username)
                .flatMap(user -> userRoleRepository.findRolesByUserId(user.getId()) // Lấy danh sách roles
                        .flatMap(role -> roleRepository.findById(role.getRoleId())) // Truy vấn Role theo RoleId
                        .map(role -> new SimpleGrantedAuthority(role.getName())) // Chuyển đổi Role thành SimpleGrantedAuthority
                        .collectList() // Gom tất cả thành một danh sách authorities
                        .map(authorities -> User // Tạo đối tượng User với các authorities
                                .withUsername(user.getUsername())
                                .password(user.getPassword())
                                .authorities(authorities) // Thiết lập authorities cho user
                                .accountExpired(false)
                                .credentialsExpired(false)
                                .disabled(false)
                                .accountLocked(false)
                                .build()
                        )
                );
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    //https://docs.spring.io/spring-framework/reference/web/webflux-cors.html
    @Bean
    CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // Possibly...
        // config.applyPermitDefaultValues()
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
