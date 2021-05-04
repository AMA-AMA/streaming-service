DELETE FROM artist_genre;
DELETE FROM album_genre;
DELETE FROM album_artist;
DELETE FROM songs_list;
DELETE FROM play_lists;
DELETE FROM songs;
DELETE FROM users;
DELETE FROM genres;
DELETE FROM artists;
DELETE FROM albums;
DELETE FROM resource;

INSERT INTO genres (id, name) VALUES (1, 'genre');
INSERT INTO artists (id, name, notes) VALUES (2, 'artist','notes');
INSERT INTO artist_genre (artist_id, genre_id) VALUES (2, 1);
INSERT INTO albums (id, name, notes, year) VALUES (3, 'album', 'notes' , 2000);
INSERT INTO album_genre (album_id, genre_id) VALUES (3, 1);
INSERT INTO album_artist (album_id, artist_id) VALUES (3, 2);
INSERT INTO resource (id, checksum, name, path, size, storage_types) VALUES (4, 'checksum' , 'resource','test-bucket' ,
 2000000, 'AMAZON_S3');
INSERT INTO songs (id, name, notes, year, album_id, artist_id, genre_id, source_id) VALUES (5, 'song' , 'notes', 2000,
3 , 2 , 1 , 4);
INSERT INTO  users (id, activation_code, email, email_verified, first_name, last_name, password, user_role)
VALUES (6, null, 'email@email.com', true ,'Artem', 'Manko', '$2a$08$czozvKFGSggwlHgSoXgZf.tSwYLzhhVdrHaIQlspqI85oZDjaxKai',
 'ROLE_ADMIN');
INSERT INTO  play_lists (id, name, user_id) VALUES (7, 'play_list', 6);
INSERT INTO  songs_list (song_id, list_id) VALUES (5,7);

ALTER SEQUENCE hibernate_sequence RESTART WITH 8;