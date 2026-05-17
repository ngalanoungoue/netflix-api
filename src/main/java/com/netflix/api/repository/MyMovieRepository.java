package com.netflix.api.repository;

import com.netflix.api.model.MyMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MyMovieRepository extends JpaRepository<MyMovie, Long> {

    // Vérifier si un film est déjà en favoris (par imdbId)
    boolean existsByImdbId(String imdbId);

    // Trouver un film par son imdbId
    Optional<MyMovie> findByImdbId(String imdbId);

    // Rechercher par titre avec pagination
    Page<MyMovie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Supprimer par imdbId
    void deleteByImdbId(String imdbId);
}