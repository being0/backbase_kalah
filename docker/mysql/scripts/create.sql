create database kalah_game_db character set utf8mb4 collate utf8mb4_unicode_ci;

use kalah_game_db;

create table if not exists kalah_game
(
    id       bigint auto_increment primary key,
    status   integer,
    no_of_stones   integer,
    no_of_pits   integer,
    player_side   bit,
    board_json   json,
    created  datetime(6),
    modified datetime(6)
);
