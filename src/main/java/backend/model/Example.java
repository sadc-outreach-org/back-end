package backend.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Example")
public class Example
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "exampleID")
    private int exampleID;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, 
                fetch = FetchType.EAGER)
    @JoinColumn(name = "ccID", nullable = false)
    private CodingChallenge codingChallenge;

    @Lob
    @Column(name = "input")
    private String input;

    @Lob
    @Column(name = "output")
    private String output;

    @Lob
    @Column(name = "explanation")
    private String explanation;
}