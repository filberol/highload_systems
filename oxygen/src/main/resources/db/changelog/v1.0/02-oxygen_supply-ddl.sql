CREATE TABLE IF NOT EXISTS oxygen_supply
(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    size BIGINT NOT NULL,
    oxygen_storage_id UUID REFERENCES oxygen_storage,
    department_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp
);