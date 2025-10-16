CREATE TABLE roles (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE employees (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  role_id INTEGER REFERENCES roles(id)
);

CREATE TABLE employee_details (
  employee_id INTEGER PRIMARY KEY REFERENCES employees(id),
  email VARCHAR(255),
  address VARCHAR(255)
);

CREATE TABLE salary_records (
  id SERIAL PRIMARY KEY,
  base_salary DOUBLE PRECISION,
  bonus DOUBLE PRECISION,
  hours_worked DOUBLE PRECISION,
  total_salary DOUBLE PRECISION,
  calculated_at DATE,
  employee_id INTEGER REFERENCES employees(id)
);

-- seed roles
INSERT INTO roles (name) VALUES ('ADMIN'), ('FULLTIME'), ('PARTTIME');
