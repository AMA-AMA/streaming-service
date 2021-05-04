DELETE FROM artist_genre;
DELETE FROM album_genre;
DELETE FROM album_artist;
DELETE FROM genres;
DELETE FROM artists;
DELETE FROM albums;


INSERT INTO genres (id, name) VALUES (1, 'genre');
INSERT INTO artists (id, name, notes) VALUES (2, 'artist','notes');
INSERT INTO artist_genre (artist_id, genre_id) VALUES (2, 1);
INSERT INTO albums (id, name, notes, year) VALUES (3, 'album', 'notes' , 2000);
INSERT INTO album_genre (album_id, genre_id) VALUES (3, 1);
INSERT INTO album_artist (album_id, artist_id) VALUES (3, 2);

ALTER SEQUENCE hibernate_sequence RESTART WITH 4;
