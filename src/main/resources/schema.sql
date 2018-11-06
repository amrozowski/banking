CREATE TABLE client (
  id BIGSERIAL NOT NULL,
  surname VARCHAR(50) NOT NULL,
  name VARCHAR(20) NOT NULL,
  pesel VARCHAR(11) NOT NULL,
  CONSTRAINT pk_client PRIMARY KEY (id)
);
COMMENT ON TABLE client IS 'Klienci banku';

ALTER TABLE client ADD second_name VARCHAR(20);
ALTER TABLE client ADD vip BOOLEAN NOT NULL;

CREATE TABLE account (
  id BIGSERIAL NOT NULL,
  client_id INT8 NOT NULL,
  account_number VARCHAR(26) NOT NULL,
  currency VARCHAR(3) NOT NULL,
  balance NUMERIC(18, 2) NOT NULL,
  CONSTRAINT pk_account PRIMARY KEY (id),
  CONSTRAINT fk_account_client FOREIGN KEY (client_id) REFERENCES client(id),
  CONSTRAINT uq_account_account_number UNIQUE (account_number)
);
COMMENT ON TABLE account IS 'Konta klientów banku';

CREATE TABLE currency (
  code VARCHAR(3) NOT NULL,
  label VARCHAR(50) NOT NULL,
  exchange_rate NUMERIC(18, 2) NOT NULL,
  CONSTRAINT pk_currency PRIMARY KEY (code)
);
COMMENT ON TABLE currency IS 'Waluty kont bankowych';

ALTER TABLE account RENAME COLUMN currency TO currency_code;
ALTER TABLE account ADD CONSTRAINT fk_account_currency FOREIGN KEY (currency_code) REFERENCES currency(code);

CREATE TABLE operation (
  id BIGSERIAL NOT NULL,
  source_account_id INT8 NOT NULL,
  destination_account_number VARCHAR(26) NOT NULL,
  amount NUMERIC(18, 2) NOT NULL,
  currency VARCHAR(3) NOT NULL,
  transaction_date DATE NOT NULL,
  CONSTRAINT pk_operation PRIMARY KEY (id)
);

ALTER TABLE client ADD COLUMN version INT8 DEFAULT 0;
ALTER TABLE account ADD COLUMN version INT8 DEFAULT 0;