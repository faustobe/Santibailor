---
title: "Repository"
description: "Descrizione del documento Repository"
author: "Team SantiBailor"
created: "2024-09-24"
last_updated: "2024-09-24"
---

# Repository

## Sommario
- [Introduzione](#introduzione)
- [Contenuto](#contenuto)
- [Conclusione](#conclusione)

## Introduzione
Introduzione al documento Repository.

## Contenuto
# Repository in SantiBailor

## 1. GenericRepository

### Panoramica
`GenericRepository` è una classe generica che fornisce operazioni CRUD di base per le entità del database. Utilizza un approccio asincrono per tutte le operazioni.

### Caratteristiche Principali
- **Genericità**: Utilizza tipi generici per supportare diverse entità e modelli di dominio.
- **Operazioni Asincrone**: Tutte le operazioni sono eseguite su un thread separato.
- **Mapping Flessibile**: Utilizza funzioni lambda per la conversione tra entità e modelli di dominio.

### Metodi Principali
1. **insert**: Inserisce un nuovo oggetto nel database.
2. **update**: Aggiorna un oggetto esistente nel database.
3. **delete**: Elimina un oggetto dal database.

### Implementazione
```java
public class GenericRepository<D, E, C> {
    private final BaseDao<E> dao;
    private final ExecutorService executorService;
    private final Function<C, E> toEntityMapper;
    private final Function<E, C> toCombinedMapper;

    // Costruttore e metodi...
}
```

### Best Practices
- Utilizza un'interfaccia di callback (`OnOperationCompleteListener`) per gestire i risultati delle operazioni asincrone.
- Impiega `ExecutorService` per eseguire operazioni in background.
- Utilizza funzioni lambda per il mapping, permettendo flessibilità nella conversione dei dati.

## 2. RicorrenzaRepository

### Panoramica
`RicorrenzaRepository` è una classe specifica per la gestione delle ricorrenze. Fornisce metodi per accedere, modificare e cercare ricorrenze nel database.

### Caratteristiche Principali
- **Operazioni Specifiche per Ricorrenze**: Fornisce metodi personalizzati per le ricorrenze.
- **Paginazione**: Supporta il caricamento paginato dei dati.
- **Ricerca Avanzata**: Implementa una funzionalità di ricerca avanzata con criteri multipli.
- **Trasformazioni LiveData**: Utilizza `Transformations` per convertire entità in modelli di dominio.

### Metodi Principali
1. **getRicorrenzeDelGiorno**: Ottiene le ricorrenze per una data specifica.
2. **ricercaAvanzata**: Esegue una ricerca avanzata con vari criteri.
3. **updateImageUrl**: Aggiorna l'URL dell'immagine di una ricorrenza.
4. **insert, update, delete**: Operazioni CRUD per le ricorrenze.

### Implementazione
```java
public class RicorrenzaRepository {
    private final RicorrenzaDao ricorrenzaDao;
    private final TipoRicorrenzaDao tipoRicorrenzaDao;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    // Costruttore e metodi...
}
```

### Best Practices
- Utilizza `LiveData` per osservare i cambiamenti nei dati.
- Implementa metodi di ricerca flessibili con query SQL dinamiche.
- Gestisce le operazioni asincrone con `ExecutorService` e `Handler`.
- Utilizza il pattern Repository per centralizzare l'accesso ai dati.

## Interazione tra GenericRepository e RicorrenzaRepository

`RicorrenzaRepository` potrebbe potenzialmente estendere o utilizzare `GenericRepository` per le operazioni di base, mentre aggiunge funzionalità specifiche per le ricorrenze. Tuttavia, nel codice fornito, sembra che `RicorrenzaRepository` implementi le proprie operazioni CRUD direttamente.

## Note sulla Testing

Per testare questi repository:
1. Utilizzare un database in-memory per i test unitari.
2. Mockare `RicorrenzaDao` e `TipoRicorrenzaDao` per isolare i test del repository.
3. Testare le query di ricerca avanzata con vari scenari di input.
4. Verificare la corretta conversione tra entità e modelli di dominio.

## Osservazioni e Promemoria per Miglioramenti Futuri

1. **Integrazione di GenericRepository**: 
   `GenericRepository` offre una base solida per operazioni CRUD generiche, ma attualmente non sembra essere utilizzato da `RicorrenzaRepository`. Considerare una refactorizzazione per sfruttare meglio questa classe generica, potenzialmente estendendo `GenericRepository` in `RicorrenzaRepository` per le operazioni di base.

2. **Astrazione della Logica di Ricerca Avanzata**: 
   La logica di ricerca avanzata in `RicorrenzaRepository` è molto dettagliata e potrebbe beneficiare di un'ulteriore astrazione. Valutare l'implementazione di un pattern di query builder o di una classe separata per gestire la costruzione delle query complesse. Questo potrebbe migliorare la leggibilità e la manutenibilità del codice.

3. **Unificazione della Gestione Asincrona**: 
   Entrambe le classi gestiscono bene le operazioni asincrone, ma utilizzano approcci leggermente diversi. Considerare l'unificazione di questi approcci e potenzialmente la migrazione a Kotlin Coroutines in futuro per semplificare la gestione delle operazioni asincrone e migliorare la leggibilità del codice.

4. **Implementazione di Caching**: 
   Valutare l'implementazione di un meccanismo di caching a livello di repository per migliorare le prestazioni, specialmente per le query frequenti o costose in termini di risorse.

5. **Standardizzazione della Gestione degli Errori**: 
   Sviluppare un approccio coerente per la gestione e la propagazione degli errori attraverso i vari livelli dell'applicazione, partendo dai repository.

Questi promemoria servono come punto di partenza per future discussioni e miglioramenti dell'architettura dei repository in SantiBailor.

## Conclusione
Conclusione del documento Repository.

---
Per ulteriori informazioni, consultare la [documentazione principale](../README.md).
