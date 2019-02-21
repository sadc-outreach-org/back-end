package backend.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@Table(name = "UserType")
public class UserType 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "userTypeID")
    private int userTypeID;

    @Column(name = "type")
    private String type;

    @JsonIgnore
    @OneToMany(mappedBy = "userType")
    private List<Profile> profiles;

    // Getter

    public int getUserTypeID ()
    {
        return userTypeID;
    }

    public String getType()
    {
        return type;
    }

    public void setId(int userTypeID)
    {
        this.userTypeID = userTypeID;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    
    public List<Profile> getProfiles()
    {
        return profiles;
    }


}