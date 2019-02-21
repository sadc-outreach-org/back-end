package backend.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Table(name = "Admin")
public class Admin 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "adminId")
    private int adminID;

    @Column(name = "position")
    private String position;

    @OneToOne
    @JoinColumn(name = "userID", nullable = false)
    private Profile profile;

    @OneToMany(mappedBy = "admin", 
                cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Requisition> requisitions;

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

    public List<Requisition> getRequisitions()
    {
        return requisitions;
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

    public void setRequisitions(List<Requisition> requisitions)
    {
        this.requisitions = requisitions;
    }
}