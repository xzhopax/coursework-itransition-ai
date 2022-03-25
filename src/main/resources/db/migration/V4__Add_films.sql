insert into films (user_id, budget, description, duration, picture, rating, title, url_video, year, id)
values (1,
    54531762,
    'Окружной прокурор пошёл на сделку с преступниками и освободил их из тюрьмы.' ||
    'Тогда человек, чьи жена и ребёнок погибли от рук убийц, решает отомстить прокурору,' ||
    'совершив правосудие самостоятельно. Его ловят и сажают в тюрьму, но он неожиданно ставит' ||
    'ультиматум: он будет убивать, не выходя из-за решетки, если его требования не будут выполнены.' ||
    'Смешное заявление, но вскоре люди правда начинают гибнуть...',
    108,
    '5b1072ab-78cf-4c16-b478-d17f4a4500d1.people.jpg',
    8.2,
    'Законопослушный гражданин',
    'https://www.youtube.com/watch?v=93LAq7YmZ4Q',
    2009,
    1);

insert into actors (full_name, id) values ('Джерард Баьлер', 1);
insert into actors (full_name, id) values ('Колм Миниб Лесли Бибб', 2);
insert into producers (full_name, id) values ('Курт Уимер', 1);
insert into film_actor (actor_id, film_id) values (1, 1);
insert into film_actor (actor_id, film_id) values (2, 1);
insert into film_producer (producer_id, film_id) values (1, 1);

insert into films (user_id, budget, description, duration, picture, rating, title, url_video, year, id)
values (1,
        200000000,
        'В первом и последнем плавании шикарного «Титаника» встречаются двое. Пассажир нижней палубы ' ||
        'Джек выиграл билет в карты, а богатая наследница Роза отправляется в Америку, чтобы выйти замуж по расчёту.' ||
        'Чувства молодых людей только успевают расцвести, и даже не классовые различия создадут испытания влюблённым, ' ||
        'а айсберг, вставший на пути считавшегося непотопляемым лайнера.',
        194,
        '354ba790-8818-4a05-b9a4-27556fd9da08.titanic.jpg',
        8.2,
        'Титаник',
        'https://www.youtube.com/watch?v=C7L3g4Z2QmM',
        1997,
        2);

insert into actors (full_name, id) values ('Леонардо ДиКаприо', 3);
insert into actors (full_name, id) values ('Кейт Уинслет', 4);
insert into actors (full_name, id) values ('Билли Зейн', 5);
insert into actors (full_name, id) values ('Кэти Бейтс', 6);
insert into actors (full_name, id) values ('Фрэнсис Фишер', 7);
insert into actors (full_name, id) values ('Глория Стюарт', 8);
insert into producers (full_name, id) values ('Джеймс Кэмерон', 2);
insert into producers (full_name, id) values ('Джон Ландау', 3);
insert into producers (full_name, id) values ('Памела Исли', 4);
insert into film_actor (actor_id, film_id) values (3, 2);
insert into film_actor (actor_id, film_id) values (4, 2);
insert into film_actor (actor_id, film_id) values (5, 2);
insert into film_actor (actor_id, film_id) values (6, 2);
insert into film_actor (actor_id, film_id) values (7, 2);
insert into film_actor (actor_id, film_id) values (8, 2);
insert into film_producer (producer_id, film_id) values (2, 2);
insert into film_producer (producer_id, film_id) values (3, 2);
insert into film_producer (producer_id, film_id) values (4, 2);

insert into films (user_id, budget, description, duration, picture, rating, title, url_video, year, id)
values (1,
        150000000,
        'В течение многих столетий две расы роботов-инопланетян — Автоботы и Десептиконы — вели войну, ' ||
        'ставкой в которой была судьба Вселенной. И вот война докатилась до Земли. В то время, когда силы ' ||
        'зла ищут ключ к верховной власти, наш последний шанс на спасение находится в руках юного землянина. ' ||
        'Единственное, что стоит между несущими зло Десептиконами и высшей властью - это ключ, находящийся в' ||
        ' руках простого парнишки. ' ||
        ' ' ||
        'Обычный подросток, Сэм Уитвикки озабочен повседневными хлопотами — школа, друзья, машины, девочки.' ||
        'Не ведая о том, что он является последним шансом человечества на спасение, Сэм вместе со своей подругой ' ||
        'Микаэлой оказывается вовлеченным в противостояние Автоботов и Десептиконов. Только тогда Сэм понимает истинное ' ||
        'значение семейного девиза Уитвикки — «без жертв нет победы!»',
        143,
        '57a379e3-b66a-4613-b971-ef8095618420.transformer.jpeg',
        8.1,
        'Трансформеры',
        'https://www.youtube.com/watch?v=wBISa7Mfx14',
        2007,
        3);

insert into actors (full_name, id) values ('Шайа ЛаБаф', 9);
insert into actors (full_name, id) values ('Меган Фокс', 10);
insert into actors (full_name, id) values ('Джош Дюамель', 11);
insert into actors (full_name, id) values ('Тайриз Гибсон', 12);
insert into actors (full_name, id) values ('Джон Туртурро', 13);
insert into actors (full_name, id) values ('Рэйчел Тейлор', 14);
insert into producers (full_name, id) values ('Майкл Бэй', 5);
insert into film_actor (actor_id, film_id) values (9, 3);
insert into film_actor (actor_id, film_id) values (10, 3);
insert into film_actor (actor_id, film_id) values (11, 3);
insert into film_actor (actor_id, film_id) values (12, 3);
insert into film_actor (actor_id, film_id) values (13, 3);
insert into film_actor (actor_id, film_id) values (14, 3);
insert into film_producer (producer_id, film_id) values (5, 3);


