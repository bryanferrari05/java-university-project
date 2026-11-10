-- TheKnife - Ruoli iniziali, eseguibile piu' volte senza duplicarli.
INSERT INTO ruoli (nome_ruolo)
VALUES ('cliente'), ('ristoratore')
ON CONFLICT (nome_ruolo) DO NOTHING;
