CREATE TABLE GrabDataTable (
    id SERIAL PRIMARY KEY,
    serp_id INT NOT NULL,
    url VARCHAR(255) NOT NULL,
    jsonArrData json NOT NULL,
    serp_title VARCHAR(255),
    serp_description TEXT,
    organic_pos SMALLINT,
    block_pos SMALLINT,
    date_added timestamp NOT NULL,
    date_updated timestamp NOT NULL
);

CREATE TABLE SerpInfoTable (
    id SERIAL PRIMARY KEY,
    keyword VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    search_engine VARCHAR(50) NOT NULL,
    search_lang VARCHAR(20) NOT NULL,
    device VARCHAR(25) NOT NULL,
    serpArrData json,
    organic json,
    people_also_ask json,
    related_searches json,
    featured_snippet json,
    fetch_date timestamp NOT NULL
);