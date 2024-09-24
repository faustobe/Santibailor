# Project Knowledge: SantiBailor App

## 1. Panoramica del Progetto
- **Nome**: SantiBailor
- **Descrizione**: Un'app calendario che mostra ricorrenze (religiose o laiche), impegni personali/scadenze e liste per la spesa.
- **Piattaforma**: Android
- **Linguaggio principale**: Java
- **Architettura**: MVVM (Model-View-ViewModel)

## 2. Funzionalità Principali
- Visualizzazione delle ricorrenze del giorno
- Gestione degli impegni personali e scadenze
- Creazione e gestione di liste per la spesa
- Calendario integrato

## 3. Struttura del Progetto
### 3.1 Struttura delle Cartelle
- `main/`
  - `java/it/faustobe/santibailor/`
    - `domain/`: Classi del dominio e casi d'uso
    - `presentation/`: ViewModel, Fragment e interfacce utente
    - `di/`: Dependency Injection
    - `data/`: Gestione dati e persistenza
    - `util/`: Classi di utilità

### 3.2 Componenti Principali
- **Activities**: 
  - `MainActivity`
- **Fragments**:
  - `HomeFragment`
  - `DashboardFragment`
  - `NotificationsFragment`
  - `SettingsFragment`
  - `SearchFragment`
  - `AddItemFragment`
- **ViewModels**:
  - `HomeViewModel`
  - `DashboardViewModel`
  - `NotificationsViewModel`
  - `SettingsViewModel`
  - `RicorrenzaViewModel`
- **Modelli di dati**:
  - `Ricorrenza`
  - `TipoRicorrenza`
  - `RicorrenzaConTipo`
- **Database**:
  - `AppDatabase`
  - DAOs: `RicorrenzaDao`, `TipoRicorrenzaDao`
  - Entities: `RicorrenzaEntity`, `TipoRicorrenzaEntity`
- **Repository**:
  - `RicorrenzaRepository`
  - `GenericRepository`
- **Adapters**:
  - `RicorrenzaAdapter`
  - `CategorySettingsAdapter`
- **Utilities**:
  - `DateUtils`
  - `ImageLoadingUtil`
  - `PaginationHelper`

## 4. Flusso dell'Applicazione
1. L'utente accede alla Home (fragment principale)
2. Dalla Home, l'utente può navigare verso:
   - Dashboard 
   - Notifiche (liste della spesa)
   - Impostazioni
3. Il fragment Impostazioni permette di gestire le configurazioni dell'app

## 5. Persistenza dei Dati
### 5.1 Database
- Tipo: SQLite
- ORM: Room

### 5.2 Struttura delle Tabelle
1. **santi**
   - Campi: id, id_mesi, giorno, santo, bio, img, prefix, suffix, tipo_ricorrenza_id
   - Chiave primaria: id

2. **tipo_ricorrenza**
   - Campi: id, nome
   - Chiave primaria: id

3. **giorno_d_settimana**
   - Campi: id, giorno
   - Chiave primaria: id (auto-increment)

4. **mese**
   - Campi: id, nome
   - Chiave primaria: id (auto-increment)

## 6. Dipendenze e Librerie
- Glide: Caricamento e caching delle immagini
- Gson: Parsing JSON
- JUnit: Testing unitario
- Mockito (core e inline): Mocking per i test
- Robolectric: Testing dell'ambiente Android

## 7. Testing
- Framework: JUnit, Mockito, Robolectric
- Stato attuale: Suite di test da implementare dopo la risoluzione dei problemi di compilazione
- Piano di implementazione:
  1. Risolvere i problemi di compilazione
  2. Implementare test unitari per le classi di dominio e i ViewModels
  3. Implementare test di integrazione per i Repository e i DAOs
  4. Implementare test UI utilizzando Espresso
  5. Configurare CI/CD per eseguire automaticamente i test ad ogni commit

## 8. API Esterne
- Nessuna API esterna utilizzata al momento

## 9. Problemi Noti
- Errori di compilazione in seguito al recente refactoring
- Priorità: Risolvere questi problemi prima di procedere con lo sviluppo e l'implementazione dei test

## 10. Piani Futuri
1. Risolvere i problemi di compilazione
2. Implementare la suite di testing completa
3. Completare la sezione calendario/ricorrenze
4. Passare allo sviluppo di un'altra sezione principale (da definire)
5. Migliorare la copertura dei test e implementare CI/CD

## 11. Processo di Build e Deploy
- Build tool: Gradle
- Nota: Attualmente ci sono problemi con il processo di build che devono essere risolti

## 12. Note Aggiuntive
- L'app è attualmente in fase di refactoring
- La Dependency Injection è gestita tramite una cartella dedicata (`di/`), suggerendo l'uso di un framework di DI (da confermare quale specifico framework viene utilizzato)
- Una volta risolti i problemi di compilazione, la priorità sarà l'implementazione di una robusta suite di test per garantire la stabilità dell'applicazione durante lo sviluppo futuro

## 13. Resources
- Layouts
- Drawables
- Menu
- Navigation
- Values (strings, colors, themes)

## 14. Assets
- Database files: `santocal.db`
