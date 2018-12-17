-- Table: public.continent

-- DROP TABLE public.continent;

CREATE TABLE public.continent
(
    id bigint NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT continent_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


-- Table: public.country

-- DROP TABLE public.country;

CREATE TABLE public.country
(
    id bigint NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    flag text COLLATE pg_catalog."default" NOT NULL,
    continent_id bigint NOT NULL,
    CONSTRAINT fk_continent_id FOREIGN KEY (continent_id)
        REFERENCES public.continent (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

-- Index: fki_fk_continent_id

-- DROP INDEX public.fki_fk_continent_id;

CREATE INDEX fki_fk_continent_id
    ON public.country USING btree
    (continent_id)
    TABLESPACE pg_default;


