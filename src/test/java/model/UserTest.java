package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import backend.model.User;

public class UserTest {

    @Test
    public void testUserHasAllFields()
    {
        User user = new User();
        user.setEmail("asfasf");
        user.setFirstName("123");
        user.setLastName("456");
        user.setPassword("123213123");
        user.setPhoneNum("314-123-4567");
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
        assertFalse("User does not has all required fields", user.hasAllFields());
    }

    @Test
    public void testUserNullFields()
    {
        User user = new User();
        assertFalse("User does not has all requried fields", user.hasAllFields());
    }
}