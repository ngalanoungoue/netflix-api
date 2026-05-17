package com.netflix.api.service;

import com.netflix.api.model.MyMovie;
import com.netflix.api.repository.MyMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MyMovieService {

    @Autowired
    private MyMovieRepository repository;

    // Récupérer tous les favoris avec pagination
    public Page<MyMovie> getAllMovies(int page, int size) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "addedAt") // Les plus récents en premier
        );
        return repository.findAll(pageable);
    }

    // Récupérer tous les favoris sans pagination (pour affichage React)
    public List<MyMovie> getAllMoviesList() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "addedAt"));
    }

    // Ajouter un film aux favoris
    public MyMovie addMovie(MyMovie movie) {
        if (repository.existsByImdbId(movie.getImdbId())) {
            throw new RuntimeException("Ce film est déjà dans vos favoris !");
        }
        return repository.save(movie);
    }

    // Récupérer un film par son ID
    public Optional<MyMovie> getMovieById(Long id) {
        return repository.findById(id);
    }

    // Vérifier si un film est en favoris
    public boolean isFavorite(String imdbId) {
        return repository.existsByImdbId(imdbId);
    }

    // Mettre à jour la note personnelle
    @Transactional
    public MyMovie updateNote(String imdbId, String note) {
        MyMovie movie = repository.findByImdbId(imdbId)
            .orElseThrow(() -> new RuntimeException("Film introuvable"));
        movie.setPersonalNote(note);
        return repository.save(movie);
    }

    // Supprimer des favoris
    @Transactional
    public void deleteMovie(String imdbId) {
        if (!repository.existsByImdbId(imdbId)) {
            throw new RuntimeException("Film introuvable dans les favoris");
        }
        repository.deleteByImdbId(imdbId);
    }

    // Rechercher dans les favoris
    public Page<MyMovie> searchMovies(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByTitleContainingIgnoreCase(title, pageable);
    }
}