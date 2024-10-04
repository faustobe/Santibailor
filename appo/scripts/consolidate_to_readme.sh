#!/bin/bash

# Nome del file di output
output_file="README.md"

# Lista dei file con contenuto significativo
significant_files=(
    "aggiornamento-documentazione.md"
    "architettura_componenti-architetturali.md"
    "architettura_diagramma-mvvm.md"
    "checklist.md"
    "componenti-chiave_database.md"
    "componenti-chiave_home-fragment.md"
    "componenti-chiave_main-activity.md"
    "componenti-chiave_my-application.md"
    "componenti-chiave_repository.md"
    "componenti-chiave_settings.md"
    "componenti-chiave_usecase.md"
    "componenti-chiave_viewmodel.md"
    "CONTRIBUTING.md"
    "glossario.md"
    "navigazione_diagramma-flusso.md"
    "navigazione_panoramica-navigazione.md"
    "processo_manutenzione_documentazione.md"
    "struttura-progetto_componenti-principali.md"
    "struttura-progetto_diagramma-struttura.md"
)

# Funzione per pulire il nome del file per l'uso come ancora
clean_filename() {
    echo "$1" | sed 's/\.md$//' | tr '*' '_' | tr '[:upper:]' '[:lower:]' | sed 's/[^a-z0-9_-]/-/g'
}

# Inizia il file di output con il titolo e la descrizione del progetto
echo "# SantiBailor" > "$output_file"
echo "" >> "$output_file"
echo "Benvenuti nella documentazione ufficiale del progetto SantiBailor, un'applicazione Android per la gestione di ricorrenze, santi, impegni personali e liste della spesa." >> "$output_file"
echo "" >> "$output_file"
echo "## Indice della Documentazione" >> "$output_file"

# Aggiungi le voci dell'indice
for file in "${significant_files[@]}"; do
    if [ "$file" != "README.md" ]; then
        clean_name=$(clean_filename "$file")
        echo "- [$(basename "$file" .md)](#$clean_name)" >> "$output_file"
    fi
done

echo "" >> "$output_file"

# Aggiungi il contenuto di ogni file significativo
for file in "${significant_files[@]}"; do
    if [ "$file" != "README.md" ] && [ -f "$file" ]; then
        clean_name=$(clean_filename "$file")
        echo "" >> "$output_file"
        echo "<a name=\"$clean_name\"></a>" >> "$output_file"
        echo "## $(basename "$file" .md)" >> "$output_file"
        echo "" >> "$output_file"
        cat "$file" >> "$output_file"
        echo "" >> "$output_file"
        echo "---" >> "$output_file"
    elif [ "$file" != "README.md" ]; then
        echo "Attenzione: Il file $file non esiste."
    fi
done

echo "Documentazione consolidata creata in $output_file"
