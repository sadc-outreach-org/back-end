package backend.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
@Table(name = "Candidate")
public class Candidate 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "candidateID")
    private int candidateID;

    @Column(name = "streetAddress")
    private String streetAddress;

    @Column(name = "zipCode")
    private String zipCode;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "gitLink")
    private String gitLink;

    @JsonIgnore
    @Lob
    @Column(name = "resume")
    private byte[] resume;

    @JsonIgnore
    @OneToOne (cascade = CascadeType.ALL, 
                fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    private Profile profile;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},
                fetch = FetchType.LAZY)
    @JoinTable(name = "CandToJob",
                joinColumns         = @JoinColumn(name = "candidateID"),
                inverseJoinColumns  = @JoinColumn(name = "jobID"))
    private List<Job> jobs;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},
                fetch = FetchType.LAZY)
    @JoinTable(name = "CandToReq",
                joinColumns         = @JoinColumn(name = "candidateID"),
                inverseJoinColumns  = @JoinColumn(name = "requisitionID"))
    private List<Requisition> requisitions;


    //Setters and getters
    public int getCandidateID()
    {
        return candidateID;
    }

    public String getStreetAddress(){
        return streetAddress;
    }

    public String getZipCode(){
        return zipCode;
    }

    public String getState(){
        return state;
    }

    public String getCity(){
        return city;
    }

    public String getGitLink(){
        return gitLink;
    }

    public byte[] getResume(){
        return resume;
    }

    public Profile getProfile()
    {
        return profile;
    }

    public List<Job> getJobs()
    {
        return jobs;
    }

    public List<Requisition> getRequisitions()
    {
        return requisitions;
    }

    public void setStreetAddress(String streetAddress){
        this.streetAddress = streetAddress;
    }

    public void setZipcode(String zipCode){
        this.zipCode = zipCode;
    }

    public void setState(String state){
        this.state = state;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setGitLink(String gitLink){
        this.gitLink = gitLink;
    }

    public void setResume(byte[] resume){
        this.resume = resume;
    }

    public void setProfile(Profile profile)
    {
        this.profile = profile;
    }

    public void setPositions(List<Job> jobs)
    {
        this.jobs = jobs;
    }
}