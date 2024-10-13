CREATE TABLE IF NOT EXISTS orders
(
    id UUID DEFAULT uuid() PRIMARY KEY,
    status oxygen_order_status NOT NULL,
    size BIGINT NOT NULL,
    oxygen_storage_id UUID NOT NULL REFERENCES oxygen_storage,
    department_id UUID NOT NULL REFERENCES department,
    order_id UUID NOT NULL REFERENCES orders,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);