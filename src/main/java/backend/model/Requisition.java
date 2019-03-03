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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Requisition")
public class Requisition
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "requisitionID")
    private int requisitionID;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminID")
    private Admin admin;

    @ManyToOne()
    @JoinColumn(name = "jobID")
    private Job job;

    @JsonIgnore
    @ManyToMany(mappedBy = "requisitions",
                fetch = FetchType.LAZY)
    private List<Candidate> candidates;

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

}