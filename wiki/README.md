# SantiBailor - Documentazione del Progetto

Benvenuti nella documentazione ufficiale del progetto SantiBailor, un'applicazione Android per la gestione di ricorrenze, santi, impegni personali e liste della spesa.

## Introduzione al Progetto

SantiBailor è un'applicazione Android che offre agli utenti un modo semplice e intuitivo per gestire ricorrenze, tenere traccia di santi e festività, organizzare impegni personali e creare liste della spesa. L'applicazione è strutturata seguendo le best practices di sviluppo Android, utilizzando l'architettura MVVM (Model-View-ViewModel) e componenti moderni come Room per la persistenza dei dati.

## Indice della Documentazione

### 1. [Panoramica del Progetto](./panoramica-progetto.md)
Una visione d'insieme dell'applicazione, dei suoi obiettivi e delle principali funzionalità.

### 2. [Architettura](./architettura/overview.md)
- [Diagramma dell'Architettura MVVM](./architettura/diagramma-mvvm.md)
- [Componenti Architetturali](./architettura/componenti-architetturali.md)

### 3. [Componenti Chiave](./componenti-chiave/overview.md)
- [ViewModel](./componenti-chiave/viewmodel.md)
- [Repository](./componenti-chiave/repository.md)
- [UseCase](./componenti-chiave/usecase.md)
- [Database](./componenti-chiave/database.md)

### 4. [Struttura del Progetto](./struttura-progetto/overview.md)
- [Diagramma della Struttura](./struttura-progetto/diagramma-struttura.md)
- [Componenti Principali](./struttura-progetto/componenti-principali.md)

### 5. [Funzionalità](./funzionalita/overview.md)
- [Gestione delle Ricorrenze](./funzionalita/gestione-ricorrenze.md)
- [Sistema di Ricerca](./funzionalita/sistema-ricerca.md)
- [Impostazioni dell'Applicazione](./funzionalita/impostazioni-app.md)

### 6. [Navigazione](./navigazione/overview.md)
- [Diagramma di Flusso](./navigazione/diagramma-flusso.md)
- [Descrizione del Flusso](./navigazione/descrizione-flusso.md)

### 7. [Guida allo Sviluppo](./guida-sviluppo/overview.md)
- [Setup dell'Ambiente di Sviluppo](./guida-sviluppo/setup-ambiente.md)
- [Best Practices](./guida-sviluppo/best-practices.md)
- [Processo di Contribuzione](./guida-sviluppo/processo-contribuzione.md)

### 8. [Manutenzione](./manutenzione/overview.md)
- [Come mantenere e aggiornare questa documentazione del progetto](./manutenzione/aggiornamento-documentazione.md)

### 9. [Glossario](./glossario.md)
Definizioni dei termini chiave utilizzati nel progetto.

## Come Utilizzare questa Documentazione

- Inizia dalla [Panoramica del Progetto](./panoramica-progetto.md) per una comprensione generale dell'applicazione.
- Utilizza l'indice per navigare alle sezioni specifiche di tuo interesse.
- Per sviluppatori che si uniscono al progetto, la [Guida allo Sviluppo](./guida-sviluppo/overview.md) è un ottimo punto di partenza.
- Consulta regolarmente il [Processo di Aggiornamento della Documentazione](./manutenzione/aggiornamento-documentazione.md) per mantenere la documentazione aggiornata.

## Navigazione della Documentazione

Per navigare e visualizzare la documentazione, inclusi i diagrammi, utilizziamo Docsify. Segui questi passaggi:

1. Installa Docsify globalmente (richiede Node.js):
   ```
   npm i docsify-cli -g
   ```

2. Naviga alla directory principale della wiki:
   ```
   cd percorso/alla/tua/wiki
   ```

3. Avvia il server Docsify:
   ```
   docsify serve .
   ```

4. Apri il tuo browser e vai all'indirizzo:
   ```
   http://localhost:3000
   ```

Ora puoi navigare la documentazione come un sito web interattivo, con supporto completo per i diagrammi e una barra di navigazione laterale.

Per terminare il server Docsify, premi `Ctrl+C` nel terminale dove l'hai avviato.

## Contribuire alla Documentazione

Questa documentazione è un progetto in continua evoluzione. Se trovi aree che necessitano di miglioramenti o vuoi contribuire con nuovi contenuti, per favore segui le linee guida nel file [CONTRIBUTING.md](./CONTRIBUTING.md).

---

Ultimo aggiornamento: [DATA]
