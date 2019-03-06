package backend.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Getter
@Setter
@Entity
@Table(name = "UserType")
public class UserType 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "userTypeID")
    private int userTypeID;

    @Column(name = "type")
    private String type;

    @JsonIgnore
    @OneToMany(mappedBy = "userType")
    private List<Profile> profiles;
}