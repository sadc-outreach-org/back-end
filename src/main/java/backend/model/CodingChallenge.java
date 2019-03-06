package backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "question")
    private String question;

    @Column(name = "expectedResults")
    private String expectedResults;
}