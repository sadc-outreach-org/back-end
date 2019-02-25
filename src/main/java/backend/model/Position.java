package backend.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name = "Position")
public class Position
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "positionID")
    private int positionID;

    @Column(name = "position")
    private String position;

    @JsonIgnore
    @ManyToMany(mappedBy = "positions",
                fetch = FetchType.LAZY)
    List<Candidate> candidates;

    @JsonIgnore
    @ManyToMany(mappedBy = "positions",
        fetch = FetchType.LAZY)
    List<Requisition> requisitions;

    // Getter methods
    public int getPositionID()
    {
        return positionID;
    }
    
    public String getPosition()
    {
        return position;
    }

    public List<Candidate> getCandidates()
    {
        return candidates;
    }

    public List<Requisition> getRequisitions()
    {
        return requisitions;
    }

    // Setter methods
    public void setPosition(String position)
    {
        this.position = position;
    }

    public void setCandidates(List<Candidate> candidates)
    {
        this.candidates = candidates;
    }

    public void setRequisitions(List<Requisition> requisitions)
    {
        this.requisitions = requisitions;
    }
}