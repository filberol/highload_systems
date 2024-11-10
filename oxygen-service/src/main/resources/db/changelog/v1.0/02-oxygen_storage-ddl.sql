CREATE TABLE IF NOT EXISTS oxygen_storage
(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    size BIGINT DEFAULT 0 NOT NULL,
    capacity BIGINT NOT NULL,
    department_id UUID NOT NULL REFERENCES department,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp
);