INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('c220f2a3-ba6d-4142-9580-f058f35e30bd', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'pivkadom', 'dominik.test@tietoevry.com', 'Dominik', 'Pivka', 'Dominik Pivka', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('9db29be9-9c87-4c28-9a77-f8a05261a2ef', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'vyroudom', 'dominik.v.test@tietoevry.com', 'Dominik', 'Vyroubal', 'Dominik Vyroubal', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('38ef73d8-70b8-4bf9-953d-b69f32c1762c', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'damektom', 'tomas.test@tietoevry.com', 'Tomaš', 'Dámek', 'Tomaš Dámek', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('5f81c201-d43a-4c91-80ca-0f02b43202de', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'maturkat', 'katerina.test@tietoevry.com', 'Kateřina', 'Maturová', 'Kateřina Maturová', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('38866342-0277-4d4b-b33a-54cb8fb10709', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'ondrupav', 'pavel.test@tietoevry.com', 'Pavel', 'Ondrusz', 'Pavel Ondrusz', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('5b77ffd8-92e4-4e7f-9e7f-e0206e68006e', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'kowanmic', 'michal.test@tietoevry.com', 'Michal', 'Kowalski', 'Michal Kowalski', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('e31ccd56-12a4-4416-8a3b-d5db4346c3fb', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'mlichmat', 'matus.test@tietoevry.com', 'Matus', 'Mlich', 'Matus Mlich',  null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('e5396d00-4399-4f61-98a1-f3a993cbe953', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'vavrepav', 'pavel.test@tietoevry.com', 'Pavel', 'Vavrečka', 'Pavel Vavrečka', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('7b3c8afc-abfa-44a6-9b7e-6411ade0c2ad', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'indrajak', 'jakub.test@tietoevry.com', 'Jakub', 'Indrák', 'Jakub Indrák', null, false);

INSERT INTO user_role (user_id, role) VALUES ('c220f2a3-ba6d-4142-9580-f058f35e30bd', 'GUEST'), ('c220f2a3-ba6d-4142-9580-f058f35e30bd', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('9db29be9-9c87-4c28-9a77-f8a05261a2ef', 'GUEST'), ('9db29be9-9c87-4c28-9a77-f8a05261a2ef', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('38ef73d8-70b8-4bf9-953d-b69f32c1762c', 'GUEST'), ('38ef73d8-70b8-4bf9-953d-b69f32c1762c', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('5f81c201-d43a-4c91-80ca-0f02b43202de', 'GUEST'), ('5f81c201-d43a-4c91-80ca-0f02b43202de', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('38866342-0277-4d4b-b33a-54cb8fb10709', 'GUEST'), ('38866342-0277-4d4b-b33a-54cb8fb10709', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('5b77ffd8-92e4-4e7f-9e7f-e0206e68006e', 'GUEST'), ('5b77ffd8-92e4-4e7f-9e7f-e0206e68006e', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('e31ccd56-12a4-4416-8a3b-d5db4346c3fb', 'GUEST'), ('e31ccd56-12a4-4416-8a3b-d5db4346c3fb', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('e5396d00-4399-4f61-98a1-f3a993cbe953', 'GUEST'), ('e5396d00-4399-4f61-98a1-f3a993cbe953', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('7b3c8afc-abfa-44a6-9b7e-6411ade0c2ad', 'GUEST'), ('7b3c8afc-abfa-44a6-9b7e-6411ade0c2ad', 'MANAGER');

INSERT INTO teams (team_id, owner_id, team_name) VALUES ('0f969e35-17b7-450e-8d95-e60e8c763055' ,'c220f2a3-ba6d-4142-9580-f058f35e30bd','team1');
INSERT INTO teams (team_id, owner_id, team_name) VALUES ('38866342-0277-4d4b-b33a-54cb8fb10709' ,'5b77ffd8-92e4-4e7f-9e7f-e0206e68006e','team2');

INSERT INTO user_teams (users_id, teams_id) VALUES ('9db29be9-9c87-4c28-9a77-f8a05261a2ef', '0f969e35-17b7-450e-8d95-e60e8c763055');
INSERT INTO user_teams (users_id, teams_id) VALUES ('38ef73d8-70b8-4bf9-953d-b69f32c1762c', '0f969e35-17b7-450e-8d95-e60e8c763055');
INSERT INTO user_teams (users_id, teams_id) VALUES ('5f81c201-d43a-4c91-80ca-0f02b43202de', '0f969e35-17b7-450e-8d95-e60e8c763055');
INSERT INTO user_teams (users_id, teams_id) VALUES ('c220f2a3-ba6d-4142-9580-f058f35e30bd', '0f969e35-17b7-450e-8d95-e60e8c763055');
INSERT INTO user_teams (users_id, teams_id) VALUES ('e31ccd56-12a4-4416-8a3b-d5db4346c3fb', '38866342-0277-4d4b-b33a-54cb8fb10709');
INSERT INTO user_teams (users_id, teams_id) VALUES ('e5396d00-4399-4f61-98a1-f3a993cbe953', '38866342-0277-4d4b-b33a-54cb8fb10709');
INSERT INTO user_teams (users_id, teams_id) VALUES ('5b77ffd8-92e4-4e7f-9e7f-e0206e68006e', '38866342-0277-4d4b-b33a-54cb8fb10709');
INSERT INTO user_teams (users_id, teams_id) VALUES ('9db29be9-9c87-4c28-9a77-f8a05261a2ef', '38866342-0277-4d4b-b33a-54cb8fb10709');


INSERT INTO items (creation_date, item_id, owner_id, comment, quality_state, serial_code, state, type) VALUES ('2023-12-22', '0a6c47b9-a966-40ff-bb9f-db5ce6f5f24d', '5b77ffd8-92e4-4e7f-9e7f-e0206e68006e', 'už ne tak fajnový stolík', 'USED','86748615641345', 'BORROWED', 'TABLE');
INSERT INTO items (creation_date, item_id, owner_id, comment, quality_state, serial_code, state, type) VALUES ('2024-01-07', '1a6c47b9-a966-40ff-bb9f-db5ce6f5f24d', '9db29be9-9c87-4c28-9a77-f8a05261a2ef', 'Nový lesklý monitor', 'NEW','942502992543295', 'AVAILABLE', 'MONITOR');



INSERT INTO loans (loan_date, return_date, borrower_id, item_id, lender_id, loan_id) VALUES ('2024-01-08', null,'38ef73d8-70b8-4bf9-953d-b69f32c1762c' ,'0a6c47b9-a966-40ff-bb9f-db5ce6f5f24d','e5396d00-4399-4f61-98a1-f3a993cbe953', '00c832b0-dbe8-439b-8c53-9e4522ce0470');
INSERT INTO loans (loan_date, return_date, borrower_id, item_id, lender_id, loan_id) VALUES ('2024-01-15', null,'7b3c8afc-abfa-44a6-9b7e-6411ade0c2ad' ,'1a6c47b9-a966-40ff-bb9f-db5ce6f5f24d','9db29be9-9c87-4c28-9a77-f8a05261a2ef', '11c832b0-dbe8-439b-8c53-9e4522ce0470');
