package com.example.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ad")
public class Ad {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

    @Column(nullable=false)
    private String companyName;

    @Column(nullable=false)
    private String typeJob;

    @Column(nullable=false)
    private int phone;

    @Column(nullable=false)
    private String city;

    @Column(nullable=false)
    private String descriptionOrLink;

    @Column(nullable=false)
    private String adType;

    @ManyToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        this.users.add(user);
    }
}
