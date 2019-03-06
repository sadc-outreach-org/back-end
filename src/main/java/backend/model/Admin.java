package backend.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Getter
@Setter
@Entity
@Table(name = "Admin")
public class Admin 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "adminId")
    private int adminID;

    @Column(name = "position")
    private String position;

    @OneToOne(cascade = CascadeType.ALL, 
                fetch = FetchType.EAGER)
    @JoinColumn(name = "userID", nullable = false)
    private Profile profile;

    @JsonIgnore
    @OneToMany(mappedBy = "admin", 
                cascade = {CascadeType.PERSIST,CascadeType.MERGE},
                fetch = FetchType.LAZY)
    private List<Requisition> requisitions;
}