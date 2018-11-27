CREATE TABLE client (
  id BIGSERIAL NOT NULL,
  surname VARCHAR(50) NOT NULL,
  name VARCHAR(20) NOT NULL,
  pesel VARCHAR(11) NOT NULL,
  second_name VARCHAR(20),
  vip BOOLEAN NOT NULL,
  foreigner BOOLEAN NOT NULL,
  birth_date DATE NOT NULL,
  version INT8 DEFAULT 0,
  CONSTRAINT pk_client PRIMARY KEY (id)
);
COMMENT ON TABLE client IS 'Klienci banku';

ALTER TABLE client ADD deleted BOOLEAN NOT NULL DEFAULT FALSE;

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

ALTER TABLE account ADD deleted BOOLEAN NOT NULL DEFAULT FALSE;

CREATE TABLE card (
  id BIGSERIAL NOT NULL,
  card_type_code VARCHAR(4) NOT NULL,
  card_number VARCHAR(16) NOT NULL,
  cvv_code VARCHAR(3) NOT NULL,
  account_id INT8 NOT NULL,
  create_date DATE NOT NULL,
  valid_thru INT8 NOT NULL,
  CONSTRAINT pk_card PRIMARY KEY (id)
);

ALTER TABLE card ADD CONSTRAINT fk_card_type FOREIGN KEY (card_type_code) REFERENCES card_type(code);

CREATE TABLE card_type (
  code VARCHAR(4) NOT NULL,
  label VARCHAR(50) NOT NULL,
  CONSTRAINT pk_card_type PRIMARY KEY (id)
);

ALTER TABLE card DROP COLUMN valid_thru;
ALTER TABLE card ADD valid_thru_date DATE NOT NULL;
ALTER TABLE card ADD active BOOLEAN NOT NULL DEFAULT TRUE;

<!--Zadanie 1 - zajęcia 4 -->
select acc.account_number , count(acc.id = oper.source_account_id or acc.account_number = oper.destination_account_number)
from account acc , operation oper
where (acc.id = oper.source_account_id or acc.account_number = oper.destination_account_number)
group by acc.id
having count(acc.id = oper.source_account_id or acc.account_number = oper.destination_account_number) <7

<!--Zadanie 2 - zajęcia 4 -->
select c.id, sum(a.balance)
from client c
join account a on c.id = a.client_id
group by c.id
order by 2 desc
limit 5

<!--Zadanie 3 - zajęcia 4 -->
select a.id, max(a.balance)
from client c
join account a on c.id = a.client_id
group by a.id
order by 2 desc, a.id asc
offset 1
limit 1

<!--Zadanie 3 (lepsza wersja, ale trzeba jeszcze opakować) - zajęcia 4 -->
select
row_number() over(partition by suma order by suma) as lp,
*
from (select a.id, max(a.balance) as suma
from client c
join account a on c.id = a.client_id
group by a.id) as dane
order by 3 desc

