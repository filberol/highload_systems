CREATE TABLE IF NOT EXISTS room
(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    capacity BIGINT DEFAULT 100 NOT NULL,
    department_id UUID NOT NULL REFERENCES department,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp
);