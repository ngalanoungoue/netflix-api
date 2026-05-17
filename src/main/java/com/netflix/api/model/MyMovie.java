package com.netflix.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "my_movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imdbId;         // ex: "tt0848228"

    @Column(nullable = false)
    private String title;          // ex: "The Avengers"

    private String posterUrl;      // URL de l'affiche

    private String year;           // ex: "2012"

    private String genre;          // ex: "Action, Adventure"

    private String imdbRating;     // ex: "8.0"

    @Column(length = 1000)
    private String personalNote;   // Ta note personnelle sur le film

    @Column(nullable = false)
    private LocalDateTime addedAt; // Date d'ajout aux favoris

    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}