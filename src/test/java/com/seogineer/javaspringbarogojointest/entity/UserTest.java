package com.seogineer.javaspringbarogojointest.entity;

import com.seogineer.javaspringbarogojointest.enums.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    @Test
    void 생성() {
        User user = new User("username", "1234", "name", Role.USER);

        assertThat(user.getUsername()).isEqualTo("username");
        assertThat(user.getPassword()).isEqualTo("1234");
        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }
}
