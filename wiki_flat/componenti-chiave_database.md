---
title: "Database"
description: "Descrizione del documento Database"
author: "Team SantiBailor"
created: "2024-09-24"
last_updated: "2024-09-24"
---

# Database

## Sommario
- [Introduzione](#introduzione)
- [Contenuto](#contenuto)
- [Conclusione](#conclusione)

## Introduzione
Introduzione al documento Database.

## Contenuto
# Database di SantiBailor

## Panoramica
Il database di SantiBailor è implementato utilizzando Room, la libreria di persistenza di Android Jetpack. Il database gestisce principalmente le ricorrenze (santi e festività) e le loro categorie.

## Struttura del Database

### Tabelle Principali
1. **santi**: Contiene i dati delle ricorrenze.
2. **tipo_ricorrenza**: Definisce i tipi di ricorrenze.
3. **mese**: Elenco dei mesi.
4. **giorno_d_settimana**: Elenco dei giorni della settimana (usato per l'UI).

### Schema della Tabella `santi`
```sql
CREATE TABLE IF NOT EXISTS "santi" (
    "id" INTEGER NOT NULL PRIMARY KEY,
    "id_mese" INTEGER NOT NULL,
    "giorno_del_mese" INTEGER NOT NULL,
    "santo" TEXT NOT NULL,
    "bio" TEXT,
    "image_url" TEXT,
    "prefix" TEXT,
    "suffix" TEXT,
    "id_tipo" INTEGER NOT NULL DEFAULT 1
);
```

### Schema della Tabella `tipo_ricorrenza`
```sql
CREATE TABLE IF NOT EXISTS "tipo_ricorrenza" (
    "id" INTEGER NOT NULL PRIMARY KEY,
    "tipo" TEXT NOT NULL
);
```

## Entità

### RicorrenzaEntity
Rappresenta una ricorrenza nel database.

Campi principali:
- `id`: Chiave primaria
- `idMese`: ID del mese
- `giornoDelMese`: Giorno del mese
- `nome`: Nome della ricorrenza
- `bio`: Biografia o descrizione
- `imageUrl`: URL dell'immagine associata
- `idTipo`: ID del tipo di ricorrenza

### TipoRicorrenzaEntity
Rappresenta un tipo di ricorrenza.

Campi:
- `id`: Chiave primaria
- `tipo`: Nome del tipo di ricorrenza

Costanti:
- `RELIGIOSA = 1`
- `LAICA = 2`

## Data Access Objects (DAO)

### RicorrenzaDao
Fornisce metodi per accedere e manipolare i dati delle ricorrenze.

Metodi principali:
- `getAllRicorrenze()`: Recupera tutte le ricorrenze.
- `getRicorrenzeDelGiorno(int giorno, int mese)`: Recupera le ricorrenze per una data specifica.
- `ricercaAvanzata(...)`: Esegue una ricerca avanzata con vari criteri.
- `ricercaAvanzataPaginata(...)`: Versione paginata della ricerca avanzata.

### TipoRicorrenzaDao
Gestisce l'accesso ai tipi di ricorrenza.

Metodi principali:
- `getAllTipiRicorrenza()`: Recupera tutti i tipi di ricorrenza.
- `getTipoRicorrenzaById(int id)`: Recupera un tipo di ricorrenza specifico.

## Migrazioni del Database
Il database è attualmente alla versione 5. Le migrazioni implementate includono:

1. MIGRATION_1_2: Ristrutturazione della tabella `santi`.
2. MIGRATION_2_3: Aggiornamento della versione senza modifiche strutturali.
3. MIGRATION_3_4: Aggiunta della colonna `image_url` se non presente.
4. MIGRATION_4_5: Verifica della struttura della tabella `santi`.

## Inizializzazione del Database
Il database viene inizializzato con dati preesistenti utilizzando un file di asset `santocal.db`.

```java
INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, DATABASE_NAME)
        .createFromAsset(DATABASE_NAME)
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
        .build();
```

## Considerazioni sulla Performance
- Utilizzo di query ottimizzate per la ricerca e il filtraggio.
- Implementazione di paginazione per gestire grandi set di dati.
- Uso di `LiveData` per osservare i cambiamenti nel database in modo reattivo.

## Best Practices Implementate
1. Separazione delle responsabilità tra entità, DAO e repository.
2. Uso di migrazioni per gestire le modifiche dello schema del database.
3. Implementazione di query complesse direttamente nel DAO per ottimizzare le prestazioni.
4. Utilizzo di `RawQuery` per query dinamiche complesse.

## Considerazioni per il Futuro
1. Implementare indici sulle colonne frequentemente utilizzate nelle query per migliorare le prestazioni.
2. Considerare l'uso di Foreign Keys per garantire l'integrità referenziale tra le tabelle.
3. Valutare l'implementazione di un meccanismo di caching per le query frequenti.
4. Monitorare le prestazioni delle query complesse e ottimizzarle se necessario.

## Conclusione
Conclusione del documento Database.

---
Per ulteriori informazioni, consultare la [documentazione principale](../README.md).
