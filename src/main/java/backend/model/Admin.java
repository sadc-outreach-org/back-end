package backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Table(name = "Admin")
public class Admin {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    @Column(name = "adminId")
    private int adminID;

    @Column(name = "position")
    private String position;

    @OneToOne
    @JoinColumn(name = "userID", nullable = false)
    private Profile profile;

    // Getters
    public int getAdminID()
    {
        return adminID;
    }

    public String getPosition()
    {
        return position;
    }

    public Profile getProfile()
    {
        return profile;
    }

    // Setters
    public void setPosition(String position)
    {
        this.position = position;
    }

    public void setUser(Profile profile)
    {
        this.profile = profile;
    }
}