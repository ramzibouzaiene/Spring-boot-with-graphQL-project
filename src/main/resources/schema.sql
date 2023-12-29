create table department(
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

create table employee(
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    salary VARCHAR(255) NOT NULL,
    department_id smallint NOT NULL
)