CREATE TABLE IF NOT EXISTS room
(
    id UUID DEFAULT uuid() PRIMARY KEY,
    number BIGINT NOT NULL,
    capacity BIGINT DEFAULT 0 NOT NULL,
    department_id UUID NOT NULL REFERENCES department,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updatedAt TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);