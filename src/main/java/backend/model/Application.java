package backend.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Application")
public class Application
{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "applicationID")
    private int applicationID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="candidateID", insertable=false, updatable=false, nullable=false)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="requisitionID", insertable=false, updatable=false, nullable=false)
    private Requisition requisition;

    @OneToOne (cascade = CascadeType.ALL, 
    fetch = FetchType.EAGER)
    @JoinColumn(name = "statusID", nullable = false)
    private Status status;

    @Column(name = "gitLink")
    private String gitLink;
}