INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('5f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'pivkadom', 'dominik.test@tietoevry.com', 'Dominik', 'Pivka', 'Dominik Pivka', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('9db29be9-9c87-4c28-9a77-f8a05261a2ef', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'vyroudom', 'dominik.v.test@tietoevry.com', 'Dominik', 'Vyroubal', 'Dominik Vyroubal', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('3f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'damektom', 'tomas.test@tietoevry.com', 'Tomaš', 'Dámek', 'Tomaš Dámek', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('2f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'maturkat', 'katerina.test@tietoevry.com', 'Kateřina', 'Maturová', 'Kateřina Maturová', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('1f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'ondrupav', 'pavel.test@tietoevry.com', 'Pavel', 'Ondrusz', 'Pavel Ondrusz', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('5b77ffd8-92e4-4e7f-9e7f-e0206e68006e', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'kowanmic', 'michal.test@tietoevry.com', 'Michal', 'Kowalski', 'Michal Kowalski', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('9f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'mlichmat', 'matus.test@tietoevry.com', 'Matus', 'Mlich', 'Matus Mlich',  null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('8f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'vavrepav', 'pavel.test@tietoevry.com', 'Pavel', 'Vavrečka', 'Pavel Vavrečka', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('7f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'indrajak', 'jakub.test@tietoevry.com', 'Jakub', 'Indrák', 'Jakub Indrák', null, false);

INSERT INTO user_role (user_id, role) VALUES ('5f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('5f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('9db29be9-9c87-4c28-9a77-f8a05261a2ef', 'GUEST'), ('9db29be9-9c87-4c28-9a77-f8a05261a2ef', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('3f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('3f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('2f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('2f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('1f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('1f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('5b77ffd8-92e4-4e7f-9e7f-e0206e68006e', 'GUEST'), ('5b77ffd8-92e4-4e7f-9e7f-e0206e68006e', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('9f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('9f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('8f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('8f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('7f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('7f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');

INSERT INTO teams (team_id, owner_id, team_name) VALUES ('0f969e35-17b7-450e-8d95-e60e8c763055' ,'5f969e35-17b7-450e-8d95-e60e8c76309d','team1');
INSERT INTO teams (team_id, owner_id, team_name) VALUES ('1f969e35-17b7-450e-8d95-e60e8c763055' ,'5b77ffd8-92e4-4e7f-9e7f-e0206e68006e','team2');

INSERT INTO user_teams (users_id, teams_id) VALUES ('9db29be9-9c87-4c28-9a77-f8a05261a2ef', '0f969e35-17b7-450e-8d95-e60e8c763055');
INSERT INTO user_teams (users_id, teams_id) VALUES ('3f969e35-17b7-450e-8d95-e60e8c76309d', '0f969e35-17b7-450e-8d95-e60e8c763055');
INSERT INTO user_teams (users_id, teams_id) VALUES ('2f969e35-17b7-450e-8d95-e60e8c76309d', '0f969e35-17b7-450e-8d95-e60e8c763055');
INSERT INTO user_teams (users_id, teams_id) VALUES ('5f969e35-17b7-450e-8d95-e60e8c76309d', '0f969e35-17b7-450e-8d95-e60e8c763055');
INSERT INTO user_teams (users_id, teams_id) VALUES ('9f969e35-17b7-450e-8d95-e60e8c76309d', '1f969e35-17b7-450e-8d95-e60e8c763055');
INSERT INTO user_teams (users_id, teams_id) VALUES ('8f969e35-17b7-450e-8d95-e60e8c76309d', '1f969e35-17b7-450e-8d95-e60e8c763055');
INSERT INTO user_teams (users_id, teams_id) VALUES ('5b77ffd8-92e4-4e7f-9e7f-e0206e68006e', '1f969e35-17b7-450e-8d95-e60e8c763055');


INSERT INTO items (creation_date, item_id, owner_id, comment, quality_state, serial_code, state, type) VALUES ('2023-12-22', '0a6c47b9-a966-40ff-bb9f-db5ce6f5f24d', '5b77ffd8-92e4-4e7f-9e7f-e0206e68006e', 'už ne tak fajnový stolík', 'USED','86748615641345', 'BORROWED', 'TABLE');
INSERT INTO items (creation_date, item_id, owner_id, comment, quality_state, serial_code, state, type) VALUES ('2024-01-07', '1a6c47b9-a966-40ff-bb9f-db5ce6f5f24d', '9db29be9-9c87-4c28-9a77-f8a05261a2ef', 'Nový lesklý monitor', 'NEW','942502992543295', 'AVAILABLE', 'MONITOR');



INSERT INTO loans (loan_date, return_date, borrower_id, item_id, lender_id, loan_id) VALUES ('2024-01-08', null,'3f969e35-17b7-450e-8d95-e60e8c76309d' ,'0a6c47b9-a966-40ff-bb9f-db5ce6f5f24d','8f969e35-17b7-450e-8d95-e60e8c76309d', '00c832b0-dbe8-439b-8c53-9e4522ce0470');
INSERT INTO loans (loan_date, return_date, borrower_id, item_id, lender_id, loan_id) VALUES ('2024-01-15', null,'7f969e35-17b7-450e-8d95-e60e8c76309d' ,'1a6c47b9-a966-40ff-bb9f-db5ce6f5f24d','9db29be9-9c87-4c28-9a77-f8a05261a2ef', '11c832b0-dbe8-439b-8c53-9e4522ce0470');
