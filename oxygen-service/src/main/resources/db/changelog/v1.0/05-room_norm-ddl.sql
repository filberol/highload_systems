CREATE TABLE IF NOT EXISTS room
(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    number BIGINT NOT NULL,
    capacity BIGINT DEFAULT 0 NOT NULL,
    room_norm_id UUID NOT NULL REFERENCES room_norm,
    department_id UUID NOT NULL REFERENCES department,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp
);