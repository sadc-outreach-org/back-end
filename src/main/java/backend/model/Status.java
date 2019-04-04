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
@Table(name = "Status")
public class Status
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "statusID")
    private int statusID;
    
    @Column(name = "status")
    private String status;

}