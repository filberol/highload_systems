create table `department` (
    `id` int primary key,
    `name` varchar(128) not null,
    `oxygen_storages_count` int not null default 0,
    `workers_count` int not null default 0
);

create table `oxygen_storage` (
    `id` int primary key,
    `filled_percent` float not null default 0 check ( `filled_percent` <= 100 ),
    `department_id` int not null references `department`(`id`)
);