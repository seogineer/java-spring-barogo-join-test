package com.seogineer.javaspringbarogojointest.controller;

import com.seogineer.javaspringbarogojointest.dto.UserCreateRequest;
import com.seogineer.javaspringbarogojointest.dto.UserResponse;
import com.seogineer.javaspringbarogojointest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.seogineer.javaspringbarogojointest.enums.ErrorMessage.REGISTRATION_SUCCESS;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        userService.registerUser(userCreateRequest);
        return ResponseEntity.ok(REGISTRATION_SUCCESS.getMessage());
    }

    @GetMapping("/info")
    public ResponseEntity<UserResponse> getInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getInfo(user));
    }
}
