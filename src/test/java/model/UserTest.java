package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import backend.model.User;
import backend.model.UserType;

public class UserTest {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Test
    public void testUserHasAllFields()
    {
        User user = new User();
        user.setEmail("asfasf");
        user.setFirstName("123");
        user.setLastName("456");
        user.setPassword("123213123");
        user.setPhoneNum("314-123-4567");
        UserType userType = new UserType();
        userType.setId(1);
        user.setUserType(userType);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        assertTrue("User has all fields", user.hasAllFields());
        
    }

    @Test
    public void testUserSomeEmptyFields()
    {
        User user = new User();
        user.setEmail("");
        user.setFirstName("123");
        user.setLastName("456");
        user.setPassword("123213123");
        user.setPhoneNum("");
        UserType userType = new UserType();
        userType.setId(1);
        user.setUserType(userType);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        assertFalse("User does not has all required fields", user.hasAllFields());
    }

    @Test
    public void testUserNullFields()
    {
        User user = new User();
        assertFalse("User does not has all requried fields", user.hasAllFields());
        user.setFirstName("123");
        assertFalse("User does not has all requried fields", user.hasAllFields());
        user.setLastName("456");
        assertFalse("User does not has all requried fields", user.hasAllFields());
        user.setPassword("123213123");
        assertFalse("User does not has all requried fields", user.hasAllFields());
        user.setPhoneNum("314-123-4567");
        assertFalse("User does not has all requried fields", user.hasAllFields());
    }
}