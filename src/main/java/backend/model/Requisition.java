package backend.model;

import java.util.List;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jobID")
    private Job job;

    @Column(name = "isOpen", nullable = false, columnDefinition="TINYINT", length = 1)
    private boolean isOpen;

    @OneToMany(mappedBy = "requisition",
            cascade = CascadeType.ALL,
            orphanRemoval = true    
    )
    private Set<Application> applications;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},
        fetch = FetchType.EAGER)
    @JoinTable(name = "ReqToCC",
    joinColumns         = @JoinColumn(name = "requisitionID"),
    inverseJoinColumns  = @JoinColumn(name = "ccID"))
    private List<CodingChallenge> codingChallenges;
}