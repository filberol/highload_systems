CREATE TABLE IF NOT EXISTS oxygen_supply
(
    id UUID DEFAULT uuid() PRIMARY KEY,
    size BIGINT NOT NULL,
    oxygen_storage_id UUID REFERENCES oxygen_storage,
    department_id UUID NOT NULL REFERENCES department,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);