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
import javax.persistence.SqlResultSetMapping;
import javax.persistence.ConstructorResult;
import javax.persistence.ColumnResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import backend.dto.JobDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SqlResultSetMapping(
    name = "JobDTO",
    classes = @ConstructorResult(
        targetClass = JobDTO.class,
        columns = {
            @ColumnResult(name = "jobID"),
            @ColumnResult(name = "title"),
            @ColumnResult(name = "description"),
            @ColumnResult(name = "requirements")
        }
    )
)
@Entity
@Table(name = "Job")
@JsonInclude(Include.NON_NULL)
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

    @OneToMany(mappedBy = "job", 
                cascade = CascadeType.ALL,
                fetch = FetchType.LAZY)
    private List<Requisition> requisitions;

    @ManyToMany(mappedBy = "jobs",
                fetch = FetchType.LAZY)
    List<Candidate> candidates;
}