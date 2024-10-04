#!/bin/bash

# Nome del file di input e output
input_file="README.md"
temp_file="README_temp.md"

# Mantieni l'intestazione originale e l'indice
sed -n '1,/^## Indice della Documentazione/p' "$input_file" > "$temp_file"
echo "" >> "$temp_file"

# Funzione per pulire una sezione
clean_section() {
    sed -e '/^---$/,/^---$/d' \
        -e '/^## Sommario/,/^## Introduzione/d' \
        -e '/^Per ulteriori informazioni/d' \
        -e '/^$/N;/^\n$/D' \
        -e 's/^# .*$//'
}

# Processa il resto del file, sezione per sezione
processing_section=false
while IFS= read -r line; do
    if [[ $line == \<a\ name=* ]]; then
        # Inizio di una nuova sezione
        echo "$line" >> "$temp_file"
        processing_section=true
    elif [[ $line == ---* && $processing_section == true ]]; then
        # Fine della sezione
        echo "" >> "$temp_file"
        echo "---" >> "$temp_file"
        echo "" >> "$temp_file"
        processing_section=false
    elif [ $processing_section == true ]; then
        # Contenuto della sezione
        echo "$line" | clean_section >> "$temp_file"
    fi
done < <(sed -n '/^<a name=/,$p' "$input_file")

# Sostituisci il file originale con quello pulito
mv "$temp_file" "$input_file"

echo "README.md è stato pulito e aggiornato."
