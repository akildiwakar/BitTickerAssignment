
CREATE SEQUENCE public.ticker_btcusd_ticker_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
	
commit;

CREATE SEQUENCE public.roles_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE public.users_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.ticker_btcusd
(
    ticker_id bigint NOT NULL DEFAULT nextval('ticker_btcusd_ticker_id_seq'::regclass),
    ask numeric,
    ask_size numeric,
    bid numeric,
    bid_size numeric,
    daily_change numeric,
    daily_change_relative numeric,
    high numeric,
    last_price numeric,
    low numeric,
    volume numeric,
    created_ts timestamp without time zone,
    updated_ts timestamp without time zone,
    CONSTRAINT ticker_btcusd_pkey PRIMARY KEY (ticker_id)
)

commit;

	
CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    email character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
    CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username)
)

CREATE TABLE IF NOT EXISTS public.roles
(
    id bigint NOT NULL DEFAULT nextval('roles_id_seq'::regclass),
    name character varying(60) COLLATE pg_catalog."default",
    CONSTRAINT roles_pkey PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS public.user_roles
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
    CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id)
        REFERENCES public.roles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

