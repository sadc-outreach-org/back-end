package backend.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Getter
@Setter
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

    @Lob
    @Column(name = "resume")
    private byte[] resume;

    @OneToOne (cascade = CascadeType.ALL, 
                fetch = FetchType.EAGER)
    @JoinColumn(name = "userID", nullable = false)
    private Profile profile;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},
                fetch = FetchType.LAZY)
    @JoinTable(name = "CandToJob",
                joinColumns         = @JoinColumn(name = "candidateID"),
                inverseJoinColumns  = @JoinColumn(name = "jobID"))
    private List<Job> jobs;

    /*
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},
                fetch = FetchType.LAZY)
    @JoinTable(name = "Application",
                joinColumns         = @JoinColumn(name = "candidateID"),
                inverseJoinColumns  = @JoinColumn(name = "requisitionID"))
    private List<Requisition> requisitions;*/

    @OneToMany(mappedBy = "candidate",
                cascade = CascadeType.ALL,
                orphanRemoval = true    
    )
    private List<Application> applications;

}