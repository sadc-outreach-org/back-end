package backend.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.SqlResultSetMapping;
import backend.dto.CandidateSortDTO;
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
import javax.persistence.OrderBy;

@Getter
@Setter
@SqlResultSetMapping(
    name = "CandidateSortDTO",
    classes = @ConstructorResult(
        targetClass = CandidateSortDTO.class,
        columns = {
            @ColumnResult(name = "candidateID"),
            @ColumnResult(name = "email"),
            @ColumnResult(name = "firstName"),
            @ColumnResult(name = "lastName"),
            @ColumnResult(name = "status")
        }
    )
)
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
    private Set<Job> jobs;

    @OneToMany(mappedBy = "candidate",
                cascade = CascadeType.ALL,
                orphanRemoval = true    
    )
    @OrderBy("statusID DESC")
    private List<Application> applications;

}