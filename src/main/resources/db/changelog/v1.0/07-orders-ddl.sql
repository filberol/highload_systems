CREATE TABLE IF NOT EXISTS orders
(
    id UUID DEFAULT uuid() PRIMARY KEY,
    status order_status NOT NULL,
    daily_norm BIGINT NOT NULL,
    dayCount BIGINT NOT NULL,
    person_id UUID NOT NULL REFERENCES person,
    department_id UUID NOT NULL REFERENCES department,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);