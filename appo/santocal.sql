---------tables calendario-------------
BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "giorno_d_settimana" (
	"id"	INTEGER NOT NULL,
	"giorno"	TEXT NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "mese" (
	"id"	INTEGER NOT NULL,
	"nome"	TEXT NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "santi" (
	"id"	INTEGER NOT NULL,
	"id_mesi"	INTEGER NOT NULL,
	"giorno"	INTEGER NOT NULL,
	"santo"	TEXT NOT NULL,
	"bio"	TEXT,
	"img"	TEXT,
	"prefix"	TEXT,
	"suffix"	TEXT,
	"tipo_ricorrenza_id"	INTEGER NOT NULL DEFAULT 1,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "tipo_ricorrenza" (
	"id"	INTEGER NOT NULL,
	"nome"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
----------tables per matrice--------------
CREATE TABLE IF NOT EXISTS "tasks" (
	"id"	INTEGER NOT NULL,
	"description"	TEXT NOT NULL,
	"quadrant"	INTEGER NOT NULL,
	"isCompleted"	INTEGER NOT NULL DEFAULT 0,
	"createdAt"	INTEGER NOT NULL,
	"updatedAt"	INTEGER NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
-----------tables per lista spesa----------
CREATE TABLE IF NOT EXISTS "shopping_lists" (
	"id"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	"createdAt"	INTEGER NOT NULL,
	"updatedAt"	INTEGER NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "shopping_items" (
	"id"	INTEGER NOT NULL,
	"shoppingListId"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	"quantity"	INTEGER NOT NULL,
	"isCompleted"	INTEGER NOT NULL DEFAULT 0,
	"createdAt"	INTEGER NOT NULL,
	"updatedAt"	INTEGER NOT NULL,
	FOREIGN KEY("shoppingListId") REFERENCES "shopping_lists"("id") ON DELETE CASCADE,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE INDEX IF NOT EXISTS "idx_shopping_items_shoppingListId" ON "shopping_items" (
	"shoppingListId"
);
COMMIT;
