CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users
(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    role user_role NOT NULL,
    name VARCHAR(40) NOT NULL,
    login VARCHAR(40) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp
);