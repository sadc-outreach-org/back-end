package backend.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Getter
@Setter
@Entity
@Table(name = "User")
public class Profile 
{
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userTypeID")
    private UserType userType;

    @OrderBy("createdAt DESC")
    @OneToMany(fetch = FetchType.LAZY, 
                mappedBy = "profile")
    private List<Notification> notifications;

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