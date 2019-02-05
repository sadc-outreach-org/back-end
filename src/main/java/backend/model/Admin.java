package backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
@Table(name = "Admin")
public class Admin {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    @Column(name = "id")
    private int id;

    @Column(name = "position")
    private String position;

    @OneToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    // Getters
    public int getid()
    {
        return id;
    }

    public String getPosition()
    {
        return position;
    }

    public User getUser()
    {
        return user;
    }

    // Setters
    public void setPosition(String position)
    {
        this.position = position;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}