#!/bin/bash

# Funzione per aggiornare i link in un file
update_links() {
    local file=$1
    local dir=$(dirname "$file")
    
    # Aggiorna i link relativi
    sed -i 's|\]\(\.\/|\](|g' "$file"
    sed -i 's|\]\(\.\.\/|\](../|g' "$file"
    
    # Aggiorna i link assoluti
    sed -i 's|\](/architettura/|\](../architettura/|g' "$file"
    sed -i 's|\](/struttura-progetto/|\](../struttura-progetto/|g' "$file"
    sed -i 's|\](/componenti-chiave/|\](../componenti-chiave/|g' "$file"
    sed -i 's|\](/funzionalita/|\](../funzionalita/|g' "$file"
    sed -i 's|\](/navigazione/|\](../navigazione/|g' "$file"
    sed -i 's|\](/guida-sviluppo/|\](../guida-sviluppo/|g' "$file"
    sed -i 's|\](/manutenzione/|\](../manutenzione/|g' "$file"
    
    # Aggiorna il link alla documentazione principale
    sed -i 's|\](/README\.md|\](../README.md|g' "$file"
    
    echo "Aggiornati i link in $file"
}

# Trova tutti i file .md e aggiorna i link
find . -name "*.md" | while read file; do
    update_links "$file"
done

echo "Aggiornamento dei link interni completato!"
