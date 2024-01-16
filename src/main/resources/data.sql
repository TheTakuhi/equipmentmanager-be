INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('5f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'pivkadom', 'dominik.test@tietoevry.com', 'Dominik', 'Pivka', 'Dominik Pivka', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('4f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'vyroudom', 'dominik.v.test@tietoevry.com', 'Dominik', 'Vyroubal', 'Dominik Vyroubal', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('38ef73d8-70b8-4bf9-953d-b69f32c1762c', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'damektom', 'tomas.test@tietoevry.com', 'Tomaš', 'Dámek', 'Tomaš Dámek', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('5f81c201-d43a-4c91-80ca-0f02b43202de', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'maturkat', 'katerina.test@tietoevry.com', 'Kateřina', 'Maturová', 'Kateřina Maturová', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('1f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'ondrupav', 'pavel.test@tietoevry.com', 'Pavel', 'Ondrusz', 'Pavel Ondrusz', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('0f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'kowalmic', 'michal.test@tietoevry.com', 'Michal', 'Kowalski', 'Michal Kowalski', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('9f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'mlichmat', 'matus.test@tietoevry.com', 'Matus', 'Mlich', 'Matus Mlich',  null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('8f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'vavrepav', 'pavel.test@tietoevry.com', 'Pavel', 'Vavrečka', 'Pavel Vavrečka', null, false);
INSERT INTO users (id, created_at, created_by, last_modified_at, last_modified_by, login, email, first_name, last_name, full_name, photo, removed) VALUES ('7f969e35-17b7-450e-8d95-e60e8c76309d', '2017-01-26 16:05:03.967000', 'SYSTEM', '2023-11-26 16:05:03.967000', 'SYSTEM', 'indrajak', 'jakub.test@tietoevry.com', 'Jakub', 'Indrák', 'Jakub Indrák', null, false);

INSERT INTO user_role (user_id, role) VALUES ('5f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('5f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('4f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('4f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('38ef73d8-70b8-4bf9-953d-b69f32c1762c', 'GUEST'), ('38ef73d8-70b8-4bf9-953d-b69f32c1762c', 'ADMIN');
INSERT INTO user_role (user_id, role) VALUES ('5f81c201-d43a-4c91-80ca-0f02b43202de', 'GUEST'), ('5f81c201-d43a-4c91-80ca-0f02b43202de', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('1f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('1f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('0f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('0f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('9f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('9f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('8f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('8f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');
INSERT INTO user_role (user_id, role) VALUES ('7f969e35-17b7-450e-8d95-e60e8c76309d', 'GUEST'), ('7f969e35-17b7-450e-8d95-e60e8c76309d', 'MANAGER');

INSERT INTO contracts (contract_id, contract_type, end_date, start_date, contract_owner_id) VALUES ('859abff8-737b-4bfb-bf42-f3a8589680d7', 'DPC', '2025-01-01', '2023-01-01', '5f969e35-17b7-450e-8d95-e60e8c76309d');
INSERT INTO contracts (contract_id, contract_type, end_date, start_date, contract_owner_id) VALUES ('759abff8-737b-4bfb-bf42-f3a8589680d7', 'DPC', '2025-01-01', '2023-01-01', '4f969e35-17b7-450e-8d95-e60e8c76309d');
INSERT INTO contracts (contract_id, contract_type, end_date, start_date, contract_owner_id) VALUES ('659abff8-737b-4bfb-bf42-f3a8589680d7', 'DPC', '2025-01-01', '2023-01-01', '38ef73d8-70b8-4bf9-953d-b69f32c1762c');
INSERT INTO contracts (contract_id, contract_type, end_date, start_date, contract_owner_id) VALUES ('559abff8-737b-4bfb-bf42-f3a8589680d7', 'DPC', '2025-01-01', '2023-01-01', '5f81c201-d43a-4c91-80ca-0f02b43202de');
INSERT INTO contracts (contract_id, contract_type, end_date, start_date, contract_owner_id) VALUES ('459abff8-737b-4bfb-bf42-f3a8589680d7', 'DPC', '2025-01-01', '2023-01-01', '1f969e35-17b7-450e-8d95-e60e8c76309d');
INSERT INTO contracts (contract_id, contract_type, end_date, start_date, contract_owner_id) VALUES ('359abff8-737b-4bfb-bf42-f3a8589680d7', 'DPC', '2025-01-01', '2023-01-01', '0f969e35-17b7-450e-8d95-e60e8c76309d');
INSERT INTO contracts (contract_id, contract_type, end_date, start_date, contract_owner_id) VALUES ('259abff8-737b-4bfb-bf42-f3a8589680d7', 'DPC', '2025-01-01', '2023-01-01', '9f969e35-17b7-450e-8d95-e60e8c76309d');
INSERT INTO contracts (contract_id, contract_type, end_date, start_date, contract_owner_id) VALUES ('159abff8-737b-4bfb-bf42-f3a8589680d7', 'DPC', '2025-01-01', '2023-01-01', '8f969e35-17b7-450e-8d95-e60e8c76309d');
INSERT INTO contracts (contract_id, contract_type, end_date, start_date, contract_owner_id) VALUES ('059abff8-737b-4bfb-bf42-f3a8589680d7', 'DPC', '2025-01-01', '2023-01-01', '7f969e35-17b7-450e-8d95-e60e8c76309d');

INSERT INTO items (creation_date, item_id, owner_id, comment, quality_state, serial_code, state, type) VALUES ('2023-12-22', '0a6c47b9-a966-40ff-bb9f-db5ce6f5f24d', '5b77ffd8-92e4-4e7f-9e7f-e0206e68006e', 'už ne tak fajnový stolík', 'USED','86748615641345', 'AVAILABLE', 'TABLE');