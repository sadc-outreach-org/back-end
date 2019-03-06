package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import backend.model.Profile;
import backend.model.UserType;

public class UserTest {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Test
    public void testUserHasAllFields()
    {
        Profile profile = new Profile();
        profile.setEmail("asfasf");
        profile.setFirstName("123");
        profile.setLastName("456");
        profile.setPassword("123213123");
        profile.setPhoneNum("314-123-4567");
        UserType userType = new UserType();
        userType.setUserTypeID(1);
        profile.setUserType(userType);
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        assertTrue("User has all fields", profile.hasAllFields());
        
    }

    @Test
    public void testUserSomeEmptyFields()
    {
        Profile profile = new Profile();
        profile.setEmail("");
        profile.setFirstName("123");
        profile.setLastName("456");
        profile.setPassword("123213123");
        profile.setPhoneNum("");
        UserType userType = new UserType();
        userType.setUserTypeID(1);
        profile.setUserType(userType);
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        assertFalse("User does not has all required fields", profile.hasAllFields());
    }

    @Test
    public void testUserNullFields()
    {
        Profile profile = new Profile();
        assertFalse("User does not has all requried fields", profile.hasAllFields());
        profile.setFirstName("123");
        assertFalse("User does not has all requried fields", profile.hasAllFields());
        profile.setLastName("456");
        assertFalse("User does not has all requried fields", profile.hasAllFields());
        profile.setPassword("123213123");
        assertFalse("User does not has all requried fields", profile.hasAllFields());
        profile.setPhoneNum("314-123-4567");
        assertFalse("User does not has all requried fields", profile.hasAllFields());
    }
}