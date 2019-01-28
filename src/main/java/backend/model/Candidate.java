package backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Table(name = "candidate")
public class Candidate {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    @Column(name = "id")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "streetAddress")
    private String streetAddress;

    @Column(name = "zipCode")
    private String zipCode;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "githubLink")
    private String githubLink;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @Lob
    @Column(name = "resume")
    private byte[] resume;

    //Setters and getters
    public int getid()
    {
        return id;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstname(String firstName){
        this.firstName= firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }


    public String getStreetAddress(){
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress){
        this.streetAddress = streetAddress;
    }

    public String getZipCode(){
        return zipCode;
    }

    public void setZipcode(String zipCode){
        this.zipCode = zipCode;
    }

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getCity(){
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber= phoneNumber;
    }

    public String getGithubLink(){
        return githubLink;
    }

    public void setGithublink(String githubLink){
        this.githubLink = githubLink;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public byte[] getResume(){
        return resume;
    }

    public void setResume(byte[] resume){
        this.resume = resume;
    }

    // Check pertinent info that are not allowed to be empty
    public boolean checkEmpty()
    {
        if (( password    == null || password.isEmpty()   ) 
        || (  firstName   == null || firstName.isEmpty()  ) 
        || (  lastName    == null || lastName.isEmpty()   ) 
        || (  email       == null || email.isEmpty()      ) 
        || (  phoneNumber == null || phoneNumber.isEmpty()) )
            return true;
        else
            return false;
    }
}