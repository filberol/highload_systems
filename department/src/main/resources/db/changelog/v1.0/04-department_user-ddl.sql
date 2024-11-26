CREATE TABLE IF NOT EXISTS department_user
(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    department_id UUID NOT NULL REFERENCES department,
    user_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp
);