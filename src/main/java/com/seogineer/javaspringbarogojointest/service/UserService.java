package com.seogineer.javaspringbarogojointest.service;

import com.seogineer.javaspringbarogojointest.dto.UserCreateRequest;
import com.seogineer.javaspringbarogojointest.dto.UserResponse;
import com.seogineer.javaspringbarogojointest.entity.User;
import com.seogineer.javaspringbarogojointest.enums.ErrorMessage;
import com.seogineer.javaspringbarogojointest.enums.Role;
import com.seogineer.javaspringbarogojointest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.seogineer.javaspringbarogojointest.enums.ErrorMessage.ID_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserCreateRequest userCreateRequest) {
        if (userRepository.findByUsername(userCreateRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException(ID_ALREADY_EXISTS.getMessage());
        }

        String encodedPassword = passwordEncoder.encode(userCreateRequest.getPassword());

        User user = new User(userCreateRequest.getUsername(), encodedPassword, userCreateRequest.getName(), Role.USER);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getInfo(org.springframework.security.core.userdetails.User user) {
        User userInfo = userRepository.findByUsername(user.getUsername()).get();
        return new UserResponse(userInfo);
    }
}
