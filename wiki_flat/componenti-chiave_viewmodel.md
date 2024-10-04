---
title: "Viewmodel"
description: "Descrizione del documento Viewmodel"
author: "Team SantiBailor"
created: "2024-09-24"
last_updated: "2024-09-24"
---

# Viewmodel

## Sommario
- [Introduzione](#introduzione)
- [Contenuto](#contenuto)
- [Conclusione](#conclusione)

## Introduzione
Introduzione al documento Viewmodel.

## Contenuto
# ViewModel in SantiBailor: RicorrenzaViewModel

## Panoramica
Il `RicorrenzaViewModel` in SantiBailor è un componente centrale che gestisce la logica di presentazione per le ricorrenze. Estende `AndroidViewModel` e utilizza Hilt per l'iniezione delle dipendenze.

## Caratteristiche Principali

1. **Gestione dello Stato**: Utilizza `LiveData` per mantenere e esporre lo stato dell'UI in modo reattivo.
2. **Multithreading**: Impiega un `ExecutorService` per operazioni asincrone e un `Handler` per aggiornamenti sull'UI thread.
3. **Caching**: Implementa un meccanismo di caching per i tipi di ricorrenza.
4. **Paginazione**: Utilizza `PaginationHelper` per gestire il caricamento paginato dei dati.
5. **Gestione Immagini**: Integra `ImageManager` per il caricamento delle immagini.
6. **Ricerca Avanzata**: Supporta funzionalità di ricerca avanzata con criteri multipli.
7. **Persistenza dello Stato**: Utilizza `SavedStateHandle` per salvare e ripristinare lo stato durante i cambi di configurazione.

## Componenti Chiave

### LiveData
- `ricorrenzeDelGiorno`, `ricorrenzeReligiose`, `ricorrenzeLaiche`: Liste di ricorrenze filtrate.
- `risultatiRicercaAvanzata`: Risultati della ricerca avanzata.
- `isLastPage`, `isLoading`, `currentPage`: Stato della paginazione.
- `selectedDate`, `selectedTipo`: Selezioni dell'utente.

### UseCase
Utilizza vari UseCase per operazioni specifiche:
- `GetRicorrenzeDelGiornoUseCase`
- `RicercaAvanzataUseCase`
- `UpdateRicorrenzaUseCase`
- `DeleteRicorrenzaUseCase`
- `InsertRicorrenzaUseCase`
- E altri...

### Repository
Interagisce con `RicorrenzaRepository` e `GenericRepository` per operazioni sui dati.

### PaginationHelper
Gestisce il caricamento paginato dei dati per varie liste di ricorrenze.

## Funzionalità Principali

1. **Caricamento Ricorrenze**:
   ```java
   public void loadRicorrenzeDelGiorno(int giorno, int mese) {
       executorService.execute(() -> {
           GetRicorrenzeDelGiornoUseCase.Params params = new GetRicorrenzeDelGiornoUseCase.Params(giorno, mese);
           List<Ricorrenza> ricorrenze = getRicorrenzeDelGiornoUseCase.executeSync(params);
           mainHandler.post(() -> {
               List<Ricorrenza> paginatedRicorrenze = ricorrenze.subList(0, Math.min(ricorrenze.size(), PAGE_SIZE));
               ricorrenzeDelGiornoPaginate.setValue(paginatedRicorrenze);
           });
       });
   }
   ```

2. **Ricerca Avanzata**:
   ```java
   public void eseguiRicercaAvanzata(String nome, Integer tipo, String dataInizio, String dataFine) {
       isLoading.setValue(true);
       lastSearchParams = new SearchParams(nome, tipo, dataInizio, dataFine);
       paginationHelper.reset();
       currentPage.setValue(0);
       executorService.execute(() -> {
           // Implementazione della ricerca
       });
   }
   ```

3. **Gestione Paginazione**:
   ```java
   public void loadNextPage() {
       if (isLoading.getValue() != null && isLoading.getValue()) return;
       if (isLastPage.getValue() != null && isLastPage.getValue()) return;

       isLoading.setValue(true);

       executorService.execute(() -> {
           // Implementazione del caricamento della pagina successiva
       });
   }
   ```

4. **Aggiornamento Ricorrenza**:
   ```java
   public void update(Ricorrenza ricorrenza) {
       executorService.execute(() -> {
           try {
               updateRicorrenzaUseCase.execute(ricorrenza);
               mainHandler.post(() -> updateResult.setValue(true));
           } catch (Exception e) {
               mainHandler.post(() -> updateResult.setValue(false));
           }
       });
   }
   ```

## Best Practices Implementate

1. **Separazione delle Responsabilità**: Utilizza UseCase per operazioni di business specifiche.
2. **Gestione Asincrona**: Impiega `ExecutorService` per operazioni in background e `Handler` per aggiornamenti UI.
3. **Reattività**: Usa `LiveData` per esporre i dati in modo reattivo alla View.
4. **Caching**: Implementa un meccanismo di caching per ottimizzare le prestazioni.
5. **Paginazione**: Utilizza `PaginationHelper` per gestire efficientemente grandi set di dati.

## Note sulla Testing

Il `RicorrenzaViewModel` è progettato per essere testabile:
- L'uso di interfacce e dependency injection facilita il mocking dei componenti.
- La separazione della logica di business in UseCase permette di testare il ViewModel in isolamento.
- L'uso di `LiveData` permette di osservare e verificare facilmente i cambiamenti di stato.

Per testare questo ViewModel, si consiglia di:
1. Mockare le dipendenze (UseCase, Repository, etc.).
2. Utilizzare `InstantTaskExecutorRule` per i test con `LiveData`.
3. Testare singolarmente ciascuna funzionalità principale (caricamento, ricerca, paginazione, etc.).
4. Verificare la corretta gestione degli errori e degli stati di caricamento.

## Conclusione
Conclusione del documento Viewmodel.

---
Per ulteriori informazioni, consultare la [documentazione principale](../README.md).
