package com.chatApp.chatApp.models;

import com.chatApp.chatApp.views.Views;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Usr")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of = { "id" })
@ToString(of = { "id", "name"})
public class User implements Serializable {

    @Id
    @Column
    @JsonView(Views.IdName.class)
    private String id;
    @Column(nullable = false)
    @JsonView(Views.IdName.class)
    private String name;
    @Column
    @JsonView(Views.IdName.class)
    private String userpic;
    @Column
    private String email;
    @Column
    @JsonView(Views.FullProfile.class)
    private String gender;
    @Column
    @JsonView(Views.FullProfile.class)
    private String locale;
    @Column
    @JsonView(Views.FullProfile.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastVisit;


    @JsonView(Views.FullProfile.class)
    @OneToMany(
            mappedBy = "subscriber",
            orphanRemoval = true
    )
    private Set<UserSubscription> subscriptions = new HashSet<>();



    @JsonView(Views.FullProfile.class)
    @OneToMany(
            mappedBy = "channel",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private Set<UserSubscription> subscribers = new HashSet<>();

}
