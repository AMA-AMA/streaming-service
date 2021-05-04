DELETE FROM artist_genre;
DELETE FROM artists;
DELETE FROM genres;

INSERT INTO genres (id, name) VALUES (1, 'genre');
INSERT INTO artists (id, name, notes) VALUES (2, 'artist','notes');
INSERT INTO artist_genre (artist_id, genre_id) VALUES (2, 1);

ALTER SEQUENCE hibernate_sequence RESTART WITH 3;



