CREATE TABLE GrabDataTable (
    id SERIAL PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    jsonArrData json NOT NULL,
    date_added timestamp NOT NULL,
    date_updated timestamp NOT NULL
);