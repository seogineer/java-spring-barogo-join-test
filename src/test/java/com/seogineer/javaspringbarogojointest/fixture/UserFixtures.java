package com.seogineer.javaspringbarogojointest.fixture;

import com.seogineer.javaspringbarogojointest.entity.User;
import com.seogineer.javaspringbarogojointest.enums.Role;

public class UserFixtures {
    public static User 사용자1 = new User("user1", "1234", "사용자1", Role.USER);
    public static User 사용자2 = new User("user2", "1234", "사용자2", Role.USER);
    public static User 사용자3 = new User("user3", "1234", "사용자3", Role.USER);
    public static User 사용자4 = new User("user4", "1234", "사용자4", Role.USER);
    public static User 사용자5 = new User("user5", "1234", "사용자5", Role.USER);
}
