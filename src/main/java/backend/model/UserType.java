package backend.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@Table(name = "usertype")
public class UserType {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    @Column(name = "id")
    private int id;

    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "userType")
    private List<User> users;

    // Getter

    public int getId ()
    {
        return id;
    }

    public String getType()
    {
        return type;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    
    public List<User> getUsers()
    {
        return users;
    }
}