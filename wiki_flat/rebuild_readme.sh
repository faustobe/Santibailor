#!/bin/bash

# Nome del file di output
output_file="README.md"

# Funzione per pulire il nome del file per l'uso come ancora
clean_filename() {
    echo "$1" | sed 's/\.md$//' | tr '*' '_' | tr '[:upper:]' '[:lower:]' | sed 's/[^a-z0-9_-]/-/g'
}

# Inizia il file di output con il titolo e l'introduzione
cat << EOF > "$output_file"
# SantiBailor - Documentazione del Progetto

Benvenuti nella documentazione ufficiale del progetto SantiBailor, un'applicazione Android per la gestione di ricorrenze, santi, impegni personali e liste della spesa.

## Introduzione al Progetto

SantiBailor è un'applicazione Android che offre agli utenti un modo semplice e intuitivo per gestire ricorrenze, tenere traccia di santi e festività, organizzare impegni personali e creare liste della spesa. L'applicazione è strutturata seguendo le best practices di sviluppo Android, utilizzando l'architettura MVVM (Model-View-ViewModel) e componenti moderni come Room per la persistenza dei dati.

## Indice della Documentazione

EOF

# Aggiungi le sezioni all'indice
echo "### 1. [Architettura](#architettura)" >> "$output_file"
echo "### 2. [Componenti Chiave](#componenti-chiave)" >> "$output_file"
echo "### 3. [Struttura del Progetto](#struttura-del-progetto)" >> "$output_file"
echo "### 4. [Navigazione](#navigazione)" >> "$output_file"
echo "### 5. [Guida allo Sviluppo](#guida-allo-sviluppo)" >> "$output_file"
echo "### 6. [Manutenzione](#manutenzione)" >> "$output_file"
echo "### 7. [Checklist](#checklist)" >> "$output_file"

# Funzione per aggiungere il contenuto di un file
add_file_content() {
    local file=$1
    local title=$2
    if [ -f "$file" ]; then
        echo "" >> "$output_file"
        echo "<a name=\"$(clean_filename "$file")\"></a>" >> "$output_file"
        echo "## $title" >> "$output_file"
        echo "" >> "$output_file"
        sed -e '1,/^---$/d' -e '/^## Sommario/,/^## Introduzione/d' -e '/^Per ulteriori informazioni/d' "$file" >> "$output_file"
        echo "" >> "$output_file"
        echo "---" >> "$output_file"
    fi
}

# Aggiungi il contenuto delle sezioni
echo "" >> "$output_file"
echo "## Architettura" >> "$output_file"
add_file_content "architettura_componenti-architetturali.md" "Componenti Architetturali"
add_file_content "architettura_diagramma-mvvm.md" "Diagramma dell'Architettura MVVM"

echo "" >> "$output_file"
echo "## Componenti Chiave" >> "$output_file"
add_file_content "componenti-chiave_database.md" "Database"
add_file_content "componenti-chiave_home-fragment.md" "Home Fragment"
add_file_content "componenti-chiave_main-activity.md" "Main Activity"
add_file_content "componenti-chiave_my-application.md" "My Application"
add_file_content "componenti-chiave_repository.md" "Repository"
add_file_content "componenti-chiave_settings.md" "Settings"
add_file_content "componenti-chiave_usecase.md" "Use Case"
add_file_content "componenti-chiave_viewmodel.md" "ViewModel"

echo "" >> "$output_file"
echo "## Struttura del Progetto" >> "$output_file"
add_file_content "struttura-progetto_componenti-principali.md" "Componenti Principali"
add_file_content "struttura-progetto_diagramma-struttura.md" "Diagramma della Struttura"

echo "" >> "$output_file"
echo "## Navigazione" >> "$output_file"
add_file_content "navigazione_diagramma-flusso.md" "Diagramma di Flusso"
add_file_content "navigazione_panoramica-navigazione.md" "Panoramica della Navigazione"

echo "" >> "$output_file"
echo "## Guida allo Sviluppo" >> "$output_file"
add_file_content "CONTRIBUTING.md" "Processo di Contribuzione"

echo "" >> "$output_file"
echo "## Manutenzione" >> "$output_file"
add_file_content "aggiornamento-documentazione.md" "Aggiornamento della Documentazione"
add_file_content "processo_manutenzione_documentazione.md" "Processo di Manutenzione della Documentazione"

echo "" >> "$output_file"
echo "## Checklist" >> "$output_file"
add_file_content "checklist.md" "Checklist del Progetto"

# Aggiungi la sezione finale
cat << EOF >> "$output_file"

## Come Utilizzare questa Documentazione

- Inizia dalla sezione Architettura per comprendere la struttura generale dell'applicazione.
- Utilizza l'indice per navigare alle sezioni specifiche di tuo interesse.
- Per sviluppatori che si uniscono al progetto, la Guida allo Sviluppo è un ottimo punto di partenza.
- Consulta regolarmente la sezione Manutenzione per mantenere la documentazione aggiornata.

## Contribuire alla Documentazione

Questa documentazione è un progetto in continua evoluzione. Se trovi aree che necessitano di miglioramenti o vuoi contribuire con nuovi contenuti, per favore segui le linee guida nella sezione Processo di Contribuzione.

---

Ultimo aggiornamento: $(date +"%Y-%m-%d")
EOF

echo "README.md ricreato con successo."
