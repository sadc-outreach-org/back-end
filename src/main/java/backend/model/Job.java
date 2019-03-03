package backend.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Job")
public class Job
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "jobID")
    private int jobID;
    
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "requirements")
    private String requirements;

    @JsonIgnore
    @OneToMany(mappedBy = "job", 
                cascade = CascadeType.ALL)
    private List<Requisition> requisitions;

    @JsonIgnore
    @ManyToMany(mappedBy = "jobs",
                fetch = FetchType.LAZY)
    List<Candidate> candidates;

    //Getter meethods
    public int getJobID()
    {
        return jobID;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getRequirements()
    {
        return requirements;
    }

    public List<Requisition> getRequisitions()
    {
        return requisitions;
    }

    public List<Candidate> getCandidates()
    {
        return candidates;
    }

    // Setter methods
    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setRequirements(String requirements)
    {
        this.requirements = requirements;
    }

    public void setRequisitions(List<Requisition> requisitions)
    {
        this.requisitions = requisitions;
    }

    public void setCandidates(List<Candidate> candidates)
    {
        this.candidates = candidates;
    }
}