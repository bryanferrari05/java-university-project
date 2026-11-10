-- TheKnife - Laboratorio Interdisciplinare B
-- Schema iniziale PostgreSQL per un database dbTK vuoto.
-- Eseguire UNA SOLA VOLTA sul database dbTK appena creato.
-- Non eseguire sul database di sviluppo se le tabelle esistono gia'.

BEGIN;

CREATE TABLE ruoli (
    id_ruolo INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_ruolo VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE utenti (
    id_utente INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    residenza VARCHAR(150),
    data_nascita DATE,
    id_ruolo INTEGER NOT NULL,
    CONSTRAINT fk_utenti_ruoli FOREIGN KEY (id_ruolo) REFERENCES ruoli(id_ruolo)
);

CREATE TABLE ristoranti (
    id_ristorante INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    nazione VARCHAR(100),
    citta VARCHAR(150) NOT NULL,
    indirizzo TEXT,
    latitudine DOUBLE PRECISION,
    longitudine DOUBLE PRECISION,
    fascia_prezzo VARCHAR(20),
    prezzo_medio NUMERIC(10,2),
    tipo_cucina VARCHAR(255),
    delivery BOOLEAN NOT NULL DEFAULT FALSE,
    prenotazione BOOLEAN NOT NULL DEFAULT FALSE,
    telefono VARCHAR(50),
    url TEXT,
    sito_web TEXT,
    riconoscimento VARCHAR(100),
    green_star VARCHAR(20),
    servizi TEXT,
    descrizione TEXT,
    id_gestore INTEGER,
    CONSTRAINT fk_ristoranti_gestore FOREIGN KEY (id_gestore) REFERENCES utenti(id_utente),
    CONSTRAINT chk_prezzo_medio CHECK (prezzo_medio >= 0),
    CONSTRAINT chk_latitudine CHECK (latitudine BETWEEN -90 AND 90),
    CONSTRAINT chk_longitudine CHECK (longitudine BETWEEN -180 AND 180)
);

CREATE TABLE recensioni (
    id_recensione INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_utente INTEGER NOT NULL,
    id_ristorante INTEGER NOT NULL,
    stelle INTEGER NOT NULL,
    testo TEXT NOT NULL,
    risposta_ristoratore TEXT,
    CONSTRAINT fk_recensioni_utente FOREIGN KEY (id_utente) REFERENCES utenti(id_utente),
    CONSTRAINT fk_recensioni_ristorante FOREIGN KEY (id_ristorante) REFERENCES ristoranti(id_ristorante),
    CONSTRAINT chk_stelle CHECK (stelle BETWEEN 1 AND 5)
);

CREATE TABLE preferiti (
    id_utente INTEGER NOT NULL,
    id_ristorante INTEGER NOT NULL,
    CONSTRAINT pk_preferiti PRIMARY KEY (id_utente, id_ristorante),
    CONSTRAINT fk_preferiti_utente FOREIGN KEY (id_utente) REFERENCES utenti(id_utente),
    CONSTRAINT fk_preferiti_ristorante FOREIGN KEY (id_ristorante) REFERENCES ristoranti(id_ristorante)
);

COMMIT;
