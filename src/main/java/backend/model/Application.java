package backend.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Application")
public class Application
{
    @EmbeddedId
    private ApplicationID applicationID;

    @MapsId("candidateID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="candidateID", insertable=false, updatable=false, nullable=false)
    private Candidate candidate;

    @MapsId("requisitionID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="requisitionID", insertable=false, updatable=false, nullable=false)
    private Requisition requisition;

    @Column(name = "status")
    private int status;

    private Application() {}

    public Application(Candidate candidate, Requisition requisition, int status)
    {
        this.candidate = candidate;
        this.requisition = requisition;
        this.applicationID = new ApplicationID(candidate.getCandidateID(), requisition.getRequisitionID());
        this.status = status;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Application application = (Application) o;
        return Objects.equals(candidate, application.candidate) && Objects.equals(requisition, application.requisition);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(candidate, requisition);
    }
}