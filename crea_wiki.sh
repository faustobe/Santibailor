#!/bin/bash

# Script per riorganizzare la struttura della wiki del progetto SantiBailor

# Assicuriamoci di essere nella directory corretta
#cd wiki || exit 1

# Creiamo le nuove cartelle
mkdir -p architettura struttura-progetto componenti-chiave funzionalita navigazione guida-sviluppo manutenzione

# Spostiamo i file esistenti nelle nuove posizioni
mv diagramma-mvvm.md componenti-architetturali.md architettura/
mv diagramma-struttura.md componenti-principali.md struttura-progetto/
mv viewmodel.md usecase.md main-activity.md my-application.md home-fragment.md repository.md database.md settings.md componenti-chiave/
mv gestione-ricorrenze.md sistema-ricerca.md impostazioni-app.md funzionalita/
mv diagramma-flusso.md descrizione-flusso.md navigazione/
mv processo-contribuzione.md best-practices.md setup-ambiente.md guida-sviluppo/

# Creiamo i nuovi file overview.md in ogni sottocartella
for dir in architettura struttura-progetto componenti-chiave funzionalita navigazione guida-sviluppo; do
    echo "# Overview of ${dir}" > "${dir}/overview.md"
    echo "This file provides an overview of the ${dir} section." >> "${dir}/overview.md"
done

# Creiamo il nuovo file glossario.md nella radice della wiki
cat << EOF > glossario.md
# Glossario del Progetto SantiBailor

- **Ricorrenza**: Un evento che si ripete periodicamente, come un santo, una festività o un anniversario.
- **MVVM**: Model-View-ViewModel, il pattern architetturale utilizzato nel progetto.
- **ViewModel**: Componente dell'architettura MVVM che gestisce la logica di presentazione e lo stato dell'UI.
- **UseCase**: Classe che implementa una singola azione o flusso di lavoro dell'applicazione.
- **Repository**: Classe che gestisce l'accesso ai dati, astraendo la fonte (database locale, API, ecc.).
EOF

# Creiamo il nuovo file aggiornamento-documentazione.md nella cartella manutenzione
cat << EOF > manutenzione/aggiornamento-documentazione.md
# Processo di Aggiornamento della Documentazione

1. Aggiornare la documentazione contestualmente alle modifiche del codice.
2. Eseguire una revisione della documentazione prima di ogni release importante.
3. Utilizzare il sistema di controllo versione (git) per tracciare le modifiche alla documentazione.
4. Aggiornare i metadati (in particolare la data di ultimo aggiornamento) quando si modifica un documento.
5. Revisionare periodicamente il glossario per assicurarsi che sia aggiornato e completo.
6. Utilizzare le issue di GitHub per segnalare problemi o suggerimenti relativi alla documentazione.
7. Eseguire una validazione automatica dei link interni come parte del processo di CI/CD.
EOF

echo "Riorganizzazione della struttura della wiki completata!"
