------------------------------EXCHANGE RATES------------------------------
insert into exchange_rate (type, rate) values ('RSD', 1);
insert into exchange_rate (type, rate) values ('EUR', 117.48);
insert into exchange_rate (type, rate) values ('USD', 98.77);
insert into exchange_rate (type, rate) values ('CHF', 106.14);
insert into exchange_rate (type, rate) values ('GBP', 137.26);

------------------------------1.5 NKS VALUES------------------------------
insert into nks_values (type, level, months_rate, base_rate) values ('RSD', 1, 21, 100000);
insert into nks_values (type, level, months_rate, base_rate) values ('EUR', 1, 30, 3000);
insert into nks_values (type, level, months_rate, base_rate) values ('USD', 1, 27, 2900);
insert into nks_values (type, level, months_rate, base_rate) values ('CHF', 1, 24, 2800);
insert into nks_values (type, level, months_rate, base_rate) values ('GBP', 1, 33, 3010);

------------------------------1.2 NKS VALUES------------------------------
insert into nks_values (type, level, months_rate, base_rate) values ('RSD', 2, 15, 50000);
insert into nks_values (type, level, months_rate, base_rate) values ('EUR', 2, 24, 2000);
insert into nks_values (type, level, months_rate, base_rate) values ('USD', 2, 21, 1900);
insert into nks_values (type, level, months_rate, base_rate) values ('CHF', 2, 18, 1800);
insert into nks_values (type, level, months_rate, base_rate) values ('GBP', 2, 27, 2100);

------------------------------1.0 NKS VALUES------------------------------
insert into nks_values (type, level, months_rate, base_rate) values ('RSD', 3, 9, 30000);
insert into nks_values (type, level, months_rate, base_rate) values ('EUR', 3, 18, 1000);
insert into nks_values (type, level, months_rate, base_rate) values ('USD', 3, 15, 900);
insert into nks_values (type, level, months_rate, base_rate) values ('CHF', 3, 12, 800);
insert into nks_values (type, level, months_rate, base_rate) values ('GBP', 3, 21, 1100);

------------------------------0.8 NKS VALUES------------------------------
insert into nks_values (type, level, months_rate, base_rate) values ('RSD', 4, 6, 10000);
insert into nks_values (type, level, months_rate, base_rate) values ('EUR', 4, 12, 500);
insert into nks_values (type, level, months_rate, base_rate) values ('USD', 4, 9, 480);
insert into nks_values (type, level, months_rate, base_rate) values ('CHF', 4, 6, 470);
insert into nks_values (type, level, months_rate, base_rate) values ('GBP', 4, 15, 520);

------------------------------AUTHORITIES------------------------------
insert into authority (name) values ('ADMIN');
insert into authority (name) values ('SLUZBENIK');
insert into authority (name) values ('KLIJENT');

------------------------------USERS------------------------------
insert into user_table (email, password, first_name, last_name, enabled)
values ('admin@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'Pera', 'Peric', true);
insert into user_table (email, password, first_name, last_name, enabled)
values ('sluzbenik@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'Pera', 'Peric', true);
insert into user_table (email, password, first_name, last_name, enabled)
values ('klijent@gmail.com', '$2a$10$aL2cRpbMvSsvTcIGxUoauO4RMefDmYtEEARsmKJpwJ7T585HfBsra', 'Pera', 'Peric', true);

------------------------------USER AUTHORITY------------------------------
insert into user_authority (user_id, authority_id) values (1, 1);
insert into user_authority (user_id, authority_id) values (2, 2);
insert into user_authority (user_id, authority_id) values (3, 3);

------------------------------ACCOUNTS------------------------------
insert into account (user_id, date, birth_date, jmbg, address, city, zip_code, balance)
values (3, '2019-10-13', '1998-05-28', '2805998805053', 'Lasla Gala 23', 'Novi Sad', '21000', 30000000);
update user_table set account_id = 1 where id = 3;