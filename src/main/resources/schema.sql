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
