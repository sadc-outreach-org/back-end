package backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

    //Getter methods
    public int getCCID()
    {
        return ccID;
    }

    public String getName()
    {
        return name;
    }

    public String getDifficulty()
    {
        return difficulty;
    }

    public String getQuestion()
    {
        return question;
    }

    public String getExpectedResults()
    {
        return expectedResults;
    }


    // Setter methods
    public void setName(String name)
    {
        this.name = name;
    }

    public void setDifficulty(String difficulty)
    {
        this.difficulty = difficulty;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public void setExpectedResults(String expectedResults)
    {
        this.expectedResults = expectedResults;
    }
}