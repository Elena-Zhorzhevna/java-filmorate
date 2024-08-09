INSERT INTO genre (genre_id, name) VALUES(1, 'Комедия');
INSERT INTO genre (genre_id, name) VALUES(2, 'Драма');
INSERT INTO genre (genre_id, name) VALUES(3, 'Мультфильм');
INSERT INTO genre (genre_id, name) VALUES(4, 'Триллер');
INSERT INTO genre (genre_id, name) VALUES(5, 'Документальный');
INSERT INTO genre (genre_id, name) VALUES(6, 'Боевик');

INSERT INTO rating_mpa (rating_id, name, description) VALUES(1, 'G', 'У фильма нет возрастных ограничений');
INSERT INTO rating_mpa (rating_id, name, description) VALUES(2, 'PG', 'Детям рекомендуется смотреть фильм с родителями');
INSERT INTO rating_mpa (rating_id, name, description) VALUES(3, 'PG-13', 'Детям до 13 лет просмотр не желателен');
INSERT INTO rating_mpa (rating_id, name, description) VALUES(4, 'R', 'Лицам до 17 лет просмотр только в присутствии взрослого');
INSERT INTO rating_mpa (rating_id, name, description) VALUES(5, 'NC-17', 'Лицам до 18 лет просмотр запрещён');

INSERT INTO friendship_status (status_id, status_name) VALUES(1, 'Unconfirmed');
INSERT INTO friendship_status (status_id, status_name) VALUES(2, 'Confirmed');