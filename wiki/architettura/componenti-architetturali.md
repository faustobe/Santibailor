---
title: "Componenti Architetturali"
description: "Descrizione del documento Componenti Architetturali"
author: "Team SantiBailor"
created: "2024-09-24"
last_updated: "2024-09-24"
---

# Componenti Architetturali

## Sommario
- [Introduzione](#introduzione)
- [Contenuto](#contenuto)
- [Conclusione](#conclusione)

## Introduzione
Introduzione al documento Componenti Architetturali.

## Contenuto
# Componenti Principali di SantiBailor

## 1. Domain
- **Model**: Contiene le classi di dominio come `Ricorrenza.java` e `TipoRicorrenza.java`.
- **UseCase**: Implementa la logica di business specifica, inclusi casi d'uso come `DeleteRicorrenzaUseCase`, `InsertRicorrenzaUseCase`, `GetRicorrenzeDelGiornoUseCase`, etc.

## 2. Presentation
- **Features**: Organizza l'UI in base alle funzionalità (ricorrenza, search, notifications, dashboard, main, settings).
- **Common**: Contiene componenti UI condivisi e ViewModel comuni.

## 3. Data
- **Local**: Gestisce la persistenza dei dati locali utilizzando Room.
  - **Dao**: Contiene le interfacce DAO per l'accesso ai dati (es. `RicorrenzaDao`, `TipoRicorrenzaDao`).
  - **Entities**: Definisce le entità del database (es. `RicorrenzaEntity`, `TipoRicorrenzaEntity`).
- **Repository**: Implementa il pattern Repository per l'accesso ai dati (es. `RicorrenzaRepository`).
- **Mapper**: Contiene classi per la mappatura tra entità di database e modelli di dominio.

## 4. DI (Dependency Injection)
- Contiene `AppModule.java` per la configurazione di Hilt.

## 5. Util
- Contiene classi di utilità come `DateUtils`, `ImageLoadingUtil`, `PaginationHelper`, etc.

## 6. Resources (res/)
- **Layout**: Definizioni XML per i layout dell'UI.
- **Values**: Risorse come stringhe, colori, dimensioni e temi.
- **Drawable**: Risorse grafiche e icone.
- **Navigation**: Definizione del grafo di navigazione.

## 7. Assets
- Contiene il database SQLite precaricato (`santocal.db`).

## Conclusione
Conclusione del documento Componenti Architetturali.

---
Per ulteriori informazioni, consultare la [documentazione principale](../README.md).
