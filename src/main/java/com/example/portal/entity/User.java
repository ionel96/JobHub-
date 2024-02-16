package com.example.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String role;

    @Column(nullable=false, unique=true, name = "email")
    private String email;

    @Column(nullable=false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "job_applicants", joinColumns = {@JoinColumn(name = "applicants_email", referencedColumnName = "email")},
                                 inverseJoinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "id")})
    private Set<Ad> ad = new HashSet<>();

    public void addAd(Ad ad) {
        this.ad.add(ad);
    }

    public User(String email) {
        this.email = email;
    }

}
