DELETE FROM users;
DELETE FROM authorities;
DELETE FROM accident_rule;
DELETE FROM accident;
DELETE FROM rule;
DELETE FROM accident_type;

INSERT INTO accident_type (name) VALUES
('Проезд на красный свет'),
('Превышение скорости'),
('Парковка в неположенном месте');

INSERT INTO rule (name) VALUES
('Статья 1. Нарушение ПДД'),
('Статья 2. Угроза безопасности движения'),
('Статья 3. Нарушение парковки');

INSERT INTO accident (name, text, address, type_id) VALUES
('А123БВ', 'Проезд на красный свет на перекрёстке', 'ул. Ленина, д.1', 1),
('В456ГД', 'Превышение скорости в жилой зоне', 'пр. Мира, д.10', 2);

INSERT INTO accident_rule (accident_id, rule_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 2);

insert into authorities (authority) values ('ROLE_USER');
insert into authorities (authority) values ('ROLE_ADMIN');
