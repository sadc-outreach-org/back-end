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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobID")
    private Job job;

    /*
    @JsonIgnore
    @ManyToMany(mappedBy = "requisitions",
                fetch = FetchType.LAZY)
    private List<Candidate> candidates;*/

    @OneToMany(mappedBy = "requisition",
            cascade = CascadeType.ALL,
            orphanRemoval = true    
    )
    private List<Application> applications;
}