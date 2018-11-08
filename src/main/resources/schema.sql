CREATE TABLE client (
  id BIGSERIAL NOT NULL,
  surname VARCHAR(50) NOT NULL,
  name VARCHAR(20) NOT NULL,
  pesel VARCHAR(11) NOT NULL,
  second_name VARCHAR(20),
  vip BOOLEAN NOT NULL,
  foreigner BOOLEAN NOT NULL,
  birthDate DATE NOT NULL,
  version INT8 DEFAULT 0,
  CONSTRAINT pk_client PRIMARY KEY (id)
);
COMMENT ON TABLE client IS 'Klienci banku';

CREATE TABLE currency (
  code VARCHAR(3) NOT NULL,
  label VARCHAR(50) NOT NULL,
  exchange_rate NUMERIC(18, 2) NOT NULL,
  CONSTRAINT pk_currency PRIMARY KEY (code)
);
COMMENT ON TABLE currency IS 'Waluty kont bankowych';

CREATE TABLE operation (
  id BIGSERIAL NOT NULL,
  source_account_id INT8 NOT NULL,
  destination_account_number VARCHAR(26) NOT NULL,
  amount NUMERIC(18, 2) NOT NULL,
  currency VARCHAR(3) NOT NULL,
  transaction_date DATE NOT NULL,
  CONSTRAINT pk_operation PRIMARY KEY (id)
);
CREATE TABLE account (
  id BIGSERIAL NOT NULL,
  client_id INT8 NOT NULL,
  account_number VARCHAR(26) NOT NULL,
  currency_code VARCHAR(3) NOT NULL,
  balance NUMERIC(18, 2) NOT NULL,
  version INT8 DEFAULT 0,
  CONSTRAINT pk_account PRIMARY KEY (id),
  CONSTRAINT fk_account_client FOREIGN KEY (client_id) REFERENCES client(id),
  CONSTRAINT fk_account_currency FOREIGN KEY (currency_code) REFERENCES currency(code),
  CONSTRAINT uq_account_account_number UNIQUE (account_number)
);
COMMENT ON TABLE account IS 'Konta klientów banku';

