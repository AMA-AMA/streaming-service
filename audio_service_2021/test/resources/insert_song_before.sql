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

INSERT INTO resource (id, checksum, name, path, size, storage_types,file_types) VALUES (1, 'dc3b3b5dc2021ff4d66a852e02c24fc0' ,
'Audio2.mp3','C:\Manko\audio_service_2021\audio_service_2021\src\main\\resources\static\audio\\' , 51781, 'FILE_SYSTEM',
'audio/mpeg');
INSERT INTO genres (id, name) VALUES (2, 'Hip');
INSERT INTO artists (id, name, notes) VALUES (3, 'Artist','eng - good music');
INSERT INTO artist_genre (artist_id, genre_id) VALUES (3, 2);
INSERT INTO albums (id, name, notes, year) VALUES (4, 'Album', 'eng - good music' , 1999);
INSERT INTO album_genre (album_id, genre_id) VALUES (4, 2);
INSERT INTO album_artist (album_id, artist_id) VALUES (4, 3);
INSERT INTO songs (id, name, notes, year, album_id, artist_id, genre_id, source_id) VALUES (5, 'Audio2.mp3' ,
 'eng - good music', 1999, 4 , 3 , 2 , 1);
INSERT INTO  users (id, activation_code, email, email_verified, first_name, last_name, password, user_role)
VALUES (6, null, 'email@email.com', true ,'Artem', 'Manko', '$2a$08$czozvKFGSggwlHgSoXgZf.tSwYLzhhVdrHaIQlspqI85oZDjaxKai',
 'ROLE_ADMIN');
INSERT INTO  play_lists (id, name, user_id) VALUES (7, 'play_list', 6);
INSERT INTO  songs_list (song_id, list_id) VALUES (5,7);

ALTER SEQUENCE hibernate_sequence RESTART WITH 8;