package backend.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Requisition")
public class Requisition
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "requisitionID")
    private int requisitionID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminID")
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobID")
    private Job job;

    @ManyToMany(mappedBy = "requisitions",
                fetch = FetchType.LAZY)
    private List<Candidate> candidates;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},
                fetch = FetchType.LAZY)
    @JoinTable(name = "PosToReq",
                joinColumns         = @JoinColumn(name = "requisitionID"),
                inverseJoinColumns  = @JoinColumn(name = "positionID"))
    private List<Position> positions;

    //Getter methods
    public int getRequisitionID()
    {
        return requisitionID;
    }
    public Admin getAdmin()
    {
        return admin;
    }

    public Job getJob()
    {
        return job;
    }

    public List<Candidate> getCandidates()
    {
        return candidates;
    }

    public List<Position> getPositions()
    {
        return positions;
    }


    //Setter methods
    public void setAdmin(Admin admin)
    {
        this.admin = admin;
    }

    public void setJob(Job job)
    {
        this.job = job;
    }

    public void setCandidates(List<Candidate> candidates)
    {
        this.candidates = candidates;
    }

    public void setPositions(List<Position> positions)
    {
        this.positions = positions;
    }
}