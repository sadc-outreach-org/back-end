package backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "User")
public class Profile {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    @Column(name = "userID")
    private int userID;

    @Column(name = "email")
    private String email;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "phoneNum")
    private String phoneNum;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userTypeID")
    private UserType userType;

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName= firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getPhoneNum(){
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum){
        this.phoneNum= phoneNum;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUserType()
    {
        return userType.getType();
    }

    public void setUserType(UserType userType)
    {
        this.userType = userType;
    }

    // Check pertinent info that are not allowed to be empty
    public boolean hasAllFields()
    {
        if (( password    == null || password.isEmpty()   ) 
        || (  firstName   == null || firstName.isEmpty()  ) 
        || (  lastName    == null || lastName.isEmpty()   ) 
        || (  email       == null || email.isEmpty()      ) 
        || (  phoneNum    == null || phoneNum.isEmpty()   )
        || (  userType == null)                       )
            return false;
        else
            return true;
    }

}