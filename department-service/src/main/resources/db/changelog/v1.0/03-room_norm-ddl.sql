CREATE TABLE IF NOT EXISTS room_norm
(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    size BIGINT NOT NULL DEFAULT 0,
    people_count BIGINT DEFAULT 0 NOT NULL,
    room_id UUID NOT NULL REFERENCES room,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp
);