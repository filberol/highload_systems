CREATE TABLE IF NOT EXISTS orders
(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    status order_status NOT NULL,
    size BIGINT NOT NULL,
    person_id UUID NOT NULL REFERENCES person,
    department_id UUID NOT NULL REFERENCES department,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp
);