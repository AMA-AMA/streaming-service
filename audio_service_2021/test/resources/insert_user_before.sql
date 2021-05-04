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

INSERT INTO  users (id, activation_code, email, email_verified, first_name, last_name, password, user_role)
VALUES (1, null, 'email@email.com', true ,'Artem', 'Manko', '$2a$08$czozvKFGSggwlHgSoXgZf.tSwYLzhhVdrHaIQlspqI85oZDjaxKai',
 'ROLE_ADMIN');
ALTER SEQUENCE hibernate_sequence RESTART WITH 2;