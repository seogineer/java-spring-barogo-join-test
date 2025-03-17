package com.seogineer.javaspringbarogojointest.service;

import com.seogineer.javaspringbarogojointest.dto.UserCreateRequest;
import com.seogineer.javaspringbarogojointest.entity.User;
import com.seogineer.javaspringbarogojointest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.seogineer.javaspringbarogojointest.enums.ErrorMessage.ID_ALREADY_EXISTS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void 이미_존재하는_아이디로_회원가입을_하면_예외가_발생한다() {

        UserCreateRequest request = new UserCreateRequest("existingUser", "password123", "John Doe");
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ID_ALREADY_EXISTS.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void 새로운_사용자가_정상적으로_등록된다() {

        UserCreateRequest request = new UserCreateRequest("newUser", "password123", "Jane Doe");
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");

        userService.registerUser(request);

        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }
}
