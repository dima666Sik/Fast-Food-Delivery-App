package ua.dev.food.fast.service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.repository.PermissionRepository;
import ua.dev.food.fast.service.repository.UserPermissionRepository;
import ua.dev.food.fast.service.repository.UserRepository;
import ua.dev.food.fast.service.util.ConstantMessageExceptions;
import ua.dev.jwt.service.JwtDecodeService;

@Configuration
@RequiredArgsConstructor
public class UserReactiveSecurityConfig {

    private final UserRepository repository;
    private final PermissionRepository permissionRepository;
    private final UserPermissionRepository userPermissionRepository;

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return username -> repository.findByEmail(username)
            .flatMap(user -> userPermissionRepository.findAllByUserId(user.getId())
                .flatMap(userPermission -> permissionRepository.findById(userPermission.getPermissionId()))
                .collectList()
                .map(permissions -> {
                    user.setPermissions(permissions);
                    return user;
                }))
            .switchIfEmpty(Mono.error(new UsernameNotFoundException(ConstantMessageExceptions.USER_NOT_FOUND)))
            .cast(UserDetails.class);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
