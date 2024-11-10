CREATE TABLE IF NOT EXISTS person
(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    middle_name VARCHAR(20),
    is_alive BOOLEAN NOT NULL DEFAULT TRUE
);