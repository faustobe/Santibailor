BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "mese" (
	"id"	INTEGER NOT NULL,
	"mese"	TEXT NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "santi" (
	"id"	INTEGER NOT NULL,
	"id_mese"	INTEGER NOT NULL,
	"giorno_del_mese"	INTEGER NOT NULL,
	"santo"	TEXT NOT NULL,
	"bio"	TEXT,
	"image_url"	TEXT,
	"prefix"	TEXT,
	"suffix"	TEXT,
	"id_tipo"	INTEGER NOT NULL DEFAULT 1,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "tipo_ricorrenza" (
	"id"	INTEGER NOT NULL,
	"tipo"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "giorno_d_settimana" (
	"id"	INTEGER,
	"giorno"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
COMMIT;
