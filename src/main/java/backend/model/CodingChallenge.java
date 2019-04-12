package backend.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name = "CodingChallenge")
public class CodingChallenge
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ccID")
    private int ccID;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    @Lob
    private String description;

    @OrderBy("exampleID asc")
    @OneToMany(mappedBy = "codingChallenge", 
                cascade = {CascadeType.PERSIST,CascadeType.MERGE},
                fetch = FetchType.LAZY)
    private List<Example> examples;

    @JsonIgnore
    @ManyToMany(mappedBy = "codingChallenges",
                fetch = FetchType.LAZY)
    List<Job> jobs;

    @JsonIgnore
    @ManyToMany(mappedBy = "codingChallenges",
                fetch = FetchType.LAZY)
    List<Requisition> requisitions;
}