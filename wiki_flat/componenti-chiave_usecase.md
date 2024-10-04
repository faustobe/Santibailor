---
title: "Usecase"
description: "Descrizione del documento Usecase"
author: "Team SantiBailor"
created: "2024-09-24"
last_updated: "2024-09-24"
---

# Usecase

## Sommario
- [Introduzione](#introduzione)
- [Contenuto](#contenuto)
- [Conclusione](#conclusione)

## Introduzione
Introduzione al documento Usecase.

## Contenuto
# UseCase in SantiBailor

## Panoramica
I UseCase in SantiBailor rappresentano le operazioni di business specifiche dell'applicazione. Implementano la logica di dominio e agiscono come intermediari tra i ViewModel e i Repository, incapsulando le regole di business dell'applicazione.

## Struttura

### BaseUseCase
SantiBailor utilizza un'interfaccia `BaseUseCase` generica per definire la struttura base di tutti i UseCase:

```java
public interface BaseUseCase<I, O> {
    O execute(I input);
}
```

Dove:
- `I` rappresenta il tipo di input del UseCase
- `O` rappresenta il tipo di output del UseCase

Questa interfaccia garantisce che tutti i UseCase abbiano un metodo `execute` che accetta un input e restituisce un output.

## Implementazione Tipica
L'implementazione di un UseCase specifico estende `BaseUseCase` e fornisce la logica di business necessaria. Prendiamo come esempio `GetRicorrenzeDelGiornoUseCase`:

```java
public class GetRicorrenzeDelGiornoUseCase implements BaseUseCase<GetRicorrenzeDelGiornoUseCase.Params, LiveData<List<Ricorrenza>>> {

    private final RicorrenzaRepository repository;

    @Inject
    public GetRicorrenzeDelGiornoUseCase(RicorrenzaRepository repository) {
        this.repository = repository;
    }

    @Override
    public LiveData<List<Ricorrenza>> execute(Params params) {
        return repository.getRicorrenzeDelGiorno(params.giorno, params.mese);
    }

    public List<Ricorrenza> executeSync(Params params) {
        return repository.getRicorrenzeDelGiornoSync(params.giorno, params.mese);
    }

    public static class Params {
        public final int giorno;
        public final int mese;

        public Params(int giorno, int mese) {
            this.giorno = giorno;
            this.mese = mese;
        }
    }
}
```

Caratteristiche chiave:
1. **Dependency Injection**: Utilizza `@Inject` per l'iniezione delle dipendenze (repository).
2. **Params Inner Class**: Definisce una classe interna `Params` per incapsulare i parametri di input.
3. **Execute Method**: Implementa il metodo `execute` definito in `BaseUseCase`.
4. **Async e Sync Operations**: Fornisce metodi sia per operazioni asincrone (`execute` che restituisce `LiveData`) che sincrone (`executeSync`).

## Funzionamento
1. Il ViewModel chiama il UseCase passando i parametri necessari.
2. Il UseCase elabora la richiesta, applicando eventuali regole di business.
3. Il UseCase interagisce con il Repository per recuperare o manipolare i dati.
4. I risultati vengono restituiti al ViewModel, tipicamente come `LiveData` per operazioni asincrone.

## Vantaggi
1. **Separazione delle Responsabilità**: Isola la logica di business dal ViewModel e dal Repository.
2. **Riusabilità**: I UseCase possono essere facilmente riutilizzati in diversi ViewModel.
3. **Testabilità**: Facilita il testing unitario della logica di business.
4. **Flessibilità**: Permette di implementare sia operazioni sincrone che asincrone.

## Best Practices
1. Mantenere i UseCase focalizzati su una singola responsabilità.
2. Utilizzare Params per incapsulare i parametri di input complessi.
3. Considerare l'implementazione di versioni sia sincrone che asincrone per flessibilità.
4. Utilizzare l'iniezione delle dipendenze per i repository e altri servizi necessari.

## Esempi in SantiBailor
- `GetRicorrenzeDelGiornoUseCase`: Recupera le ricorrenze per un giorno specifico.
- `InsertRicorrenzaUseCase`: Gestisce l'inserimento di nuove ricorrenze.
- `DeleteRicorrenzaUseCase`: Gestisce l'eliminazione di ricorrenze esistenti.

## Testing
I UseCase sono ideali per il testing unitario. Si consiglia di scrivere test che:
1. Verifichino il corretto flusso di dati tra UseCase e Repository.
2. Testino scenari di edge case e gestione degli errori.
3. Mockino le dipendenze (come i Repository) per isolare il testing del UseCase.

## Conclusione
Conclusione del documento Usecase.

---
Per ulteriori informazioni, consultare la [documentazione principale](../README.md).
