package com.netflix.api.controller;

import com.netflix.api.model.MyMovie;
import com.netflix.api.service.MyMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = {"http://localhost:5173", "https://netflix-clone-coral-theta.vercel.app"})
public class MyMovieController {

    @Autowired
    private MyMovieService service;

    // GET /api/movies — tous les favoris (liste simple pour React)
    @GetMapping
    public ResponseEntity<List<MyMovie>> getAllMovies() {
        return ResponseEntity.ok(service.getAllMoviesList());
    }

    // GET /api/movies/paginated?page=0&size=10 — avec pagination
    @GetMapping("/paginated")
    public ResponseEntity<Page<MyMovie>> getMoviesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAllMovies(page, size));
    }

    // GET /api/movies/search?title=avengers&page=0&size=5
    @GetMapping("/search")
    public ResponseEntity<Page<MyMovie>> searchMovies(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.searchMovies(title, page, size));
    }

    // GET /api/movies/check/{imdbId} — vérifier si en favoris
    @GetMapping("/check/{imdbId}")
    public ResponseEntity<Map<String, Boolean>> checkFavorite(@PathVariable String imdbId) {
        return ResponseEntity.ok(Map.of("isFavorite", service.isFavorite(imdbId)));
    }

    // POST /api/movies — ajouter aux favoris
    @PostMapping
    public ResponseEntity<?> addMovie(@RequestBody MyMovie movie) {
        try {
            MyMovie saved = service.addMovie(movie);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PATCH /api/movies/{imdbId}/note — mettre à jour la note perso
    @PatchMapping("/{imdbId}/note")
    public ResponseEntity<?> updateNote(
            @PathVariable String imdbId,
            @RequestBody Map<String, String> body) {
        try {
            MyMovie updated = service.updateNote(imdbId, body.get("note"));
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/movies/{imdbId} — retirer des favoris
    @DeleteMapping("/{imdbId}")
    public ResponseEntity<?> deleteMovie(@PathVariable String imdbId) {
        try {
            service.deleteMovie(imdbId);
            return ResponseEntity.ok(Map.of("message", "Film retiré des favoris"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}