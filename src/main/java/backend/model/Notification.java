package backend.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Notification
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "notificationID")
    private int notificationID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="userID", updatable=false, nullable=false)
    private Profile profile;

    @Lob
    @Column(name = "message")
    private String message;

    @Column(name = "hasRead", nullable = false, columnDefinition="TINYINT")
    private boolean hasRead;

    @Column(name = "createdAt", insertable=false)
    private LocalDateTime createdAt;

}

