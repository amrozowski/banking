CREATE TABLE client (
  id BIGSERIAL NOT NULL,
  surname VARCHAR(50) NOT NULL,
  name VARCHAR(20) NOT NULL,
  pesel VARCHAR(11) NOT NULL,
  CONSTRAINT pk_client PRIMARY KEY (id)
);
COMMENT ON TABLE client IS 'Klienci banku';
