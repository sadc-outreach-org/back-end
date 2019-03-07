package backend.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ApplicationID implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Column(name = "candidateID")
    private int candidateID;
    
    @Column(name = "requisitionID")
    private int requisitionID;

    private ApplicationID() {}

    public ApplicationID(int candidateID, int requisitionID)
    {
        this.candidateID = candidateID;
        this.requisitionID = requisitionID;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ApplicationID that = (ApplicationID) o;
        return Objects.equals(candidateID, that.candidateID) && Objects.equals(requisitionID, that.requisitionID);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(candidateID, requisitionID);
    }
}