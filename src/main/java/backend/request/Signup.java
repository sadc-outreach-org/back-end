package backend.request;

public class Signup 
{
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String streetAddress;
    private String zipCode;
    private String state;
    private String city;
    private String phoneNum;
    private String gitLink;

    //Getter
    public String getEmail()
    {
        return this.email;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public String getLastName()
    {
        return this.lastName;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getStreetAddress()
    {
        return this.streetAddress;
    }
    public String getZipCode()
    {
        return this.zipCode;
    }
    public String getState()
    {
        return this.state;
    }
    public String getCity()
    {
        return this.city;
    }
    public String getPhoneNum()
    {
        return this.phoneNum;
    }

    public String getGitLink()
    {
        return this.gitLink;
    }

    //Setteres

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setStreetAddress(String streetAddress)
    {
        this.streetAddress = streetAddress;
    }
    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }
    public void setState(String state)
    {
        this.state = state;
    }
    public void setCity(String city)
    {
        this.city = city;
    }
    public void setPhoneNum(String phoneNum)
    {
        this.phoneNum = phoneNum;
    }

    public void setGitLink(String githubLink)
    {
        this.gitLink = githubLink;
    }

}