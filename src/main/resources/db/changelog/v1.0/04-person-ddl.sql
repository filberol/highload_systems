CREATE TABLE IF NOT EXISTS person
(
    id UUID DEFAULT uuid() PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    middle_name VARCHAR(20)
);