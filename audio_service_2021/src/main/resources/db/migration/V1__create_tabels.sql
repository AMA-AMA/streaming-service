--create sequence hibernate_sequence start 1 increment 1;
--
--create table albums (
--id int8 not null,
--name varchar(255),
--notes varchar(255),
--year int4,
--genre_id int8,
--primary key (id));
--
--create table artist_genre (
--artist_id int8 not null,
--genre_id int8 not null);
--
--create table artists (
--id int8 not null,
--name varchar(255),
--notes varchar(255),
--primary key (id));
--
--create table credentials (
--id int8 not null,
--name varchar(255),
--password varchar(255),
--username varchar(255),
--primary key (id));
--
--create table genres (
--id int8 not null,
--name varchar(255),
--primary key (id));
--
--create table participation (
--id int8 not null,
--album_id int8,
--artist_id int8,
--primary key (id));
--
--create table play_lists (
--id int8 not null,
--name varchar(255),
--user_id varchar(255),
--primary key (id));
--
--create table resource (
--id int8 not null,
--checksum varchar(255),
--path varchar(255),
--size int4,
--storage_id int8,
--primary key (id));
--
--create table songs (
--id int8 not null,
--name varchar(255),
--notes varchar(255),
--year int4,
--album_id int8,
--artist_id int8,
--genre_id int8,
--resource_id int8,
--primary key (id));
--
--create table storage (
--id int8 not null,
--type varchar(255),
--primary key (id));
--
--create table user_table (
--id varchar(255) not null,
--email varchar(255),
--first_name varchar(255),
--primary key (id));
--
--create table users_play_list (
--user_id int8 not null,
--play_list_id int8 not null);
--
--alter table if exists albums
--add constraint FKgo1exs517g8n9osc20m3qidib
--foreign key (genre_id) references genres;
--
--alter table if exists artist_genre
--add constraint FK268c3w1s4sl33koktykvsys7s
--foreign key (genre_id) references genres;
--
--alter table if exists artist_genre
--add constraint FKq0okm9v81nfuren3e5y1i8dng
--foreign key (artist_id) references artists;
--
--alter table if exists participation
--add constraint FKforcw33r4omr3ibbr2iiq8ayn
--foreign key (album_id) references albums;
--
--alter table if exists participation
--add constraint FKb26lj4g4n6jyrer42uodhlx66
--foreign key (artist_id) references artists;
--
--alter table if exists play_lists
--add constraint FK32se38nb2aan37e71knn8de2i
--foreign key (user_id) references user_table;
--
--alter table if exists resource
--add constraint FKhakt58bss98136qbmp0uqm08p
--foreign key (storage_id) references storage;
--
--alter table if exists songs
--add constraint FKte4gkb2cqtk2erfa87oopj2cj
--foreign key (album_id) references albums;
--
--alter table if exists songs
--add constraint FKdjq2ujqovw5rc14q60f8p6b6e
--foreign key (artist_id) references artists;
--
--alter table if exists songs
--add constraint FKd5mor9lg3wkqhn2tp0r75nkm
--foreign key (genre_id) references genres;
--
--alter table if exists songs
--add constraint FK3lfbqkukmjww0vyxf230l3t3f
--foreign key (resource_id) references resource;
--
--alter table if exists users_play_list
--add constraint FKkhyvq0ay6uvk9233vysxvot4i
--foreign key (play_list_id) references songs;
--
--alter table if exists users_play_list
--add constraint FKh847vriod7ohb1qu3r8woi6fe
--foreign key (user_id) references play_lists;
