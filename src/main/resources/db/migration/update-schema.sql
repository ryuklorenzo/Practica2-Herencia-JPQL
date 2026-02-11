ALTER TABLE viviendas
    ADD "baños" INTEGER DEFAULT 0;

ALTER TABLE viviendas
DROP
COLUMN "baños";