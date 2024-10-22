CREATE TABLE IF NOT EXISTS room_norm
(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    size BIGINT NOT NULL,
    avg_person_norm REAL NOT NULL DEFAULT 100.0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp
);