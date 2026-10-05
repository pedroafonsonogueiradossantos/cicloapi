CREATE TABLE IF NOT EXISTS person (
    id          integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        text NOT NULL,
    created_at  timestamptz NOT NULL DEFAULT now(),
    updated_at  timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS city (
    id          integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        text NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS address (
    id           integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    person_id    integer NOT NULL REFERENCES person(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    street       text,
    number       text,
    neighborhood text,
    city_id      integer REFERENCES city(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    created_at   timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX ON address (person_id);
CREATE INDEX ON address (city_id);

CREATE TABLE IF NOT EXISTS phone (
    id          integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    person_id   integer NOT NULL REFERENCES person(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    number      text NOT NULL,
    type        text NOT NULL DEFAULT 'mobile' CHECK (type IN ('mobile', 'landline', 'whatsapp'))
);
CREATE INDEX ON phone (person_id);

CREATE TABLE IF NOT EXISTS item (
    id          integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        text NOT NULL UNIQUE,
    description text,
    created_at  timestamptz NOT NULL DEFAULT now(),
    updated_at  timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS orders (
    id                    integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    person_id             integer NOT NULL REFERENCES person(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    observation           text,
    order_date            timestamptz DEFAULT now(),
    created_at            timestamptz NOT NULL DEFAULT now(),
    address_street        text,
    address_number        text,
    address_neighborhood  text,
    address_city_name     text,
    origin_address_id     integer REFERENCES address(id) ON DELETE SET NULL ON UPDATE CASCADE
);
CREATE INDEX ON orders (person_id);
CREATE INDEX ON orders (origin_address_id);

CREATE TABLE IF NOT EXISTS order_item (
    id                          integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id                    integer NOT NULL REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE,
    item_id                     integer NOT NULL REFERENCES item(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    quantity                    smallint NOT NULL DEFAULT 1 CHECK (quantity > 0),
    unit_price                  numeric(12,2),
    maintenance_interval_months integer
);
CREATE INDEX ON order_item (order_id);
CREATE INDEX ON order_item (item_id);

CREATE TABLE IF NOT EXISTS app_user (
    id            integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username      text NOT NULL UNIQUE,
    password_hash text NOT NULL,
    created_at    timestamptz NOT NULL DEFAULT now()
);
