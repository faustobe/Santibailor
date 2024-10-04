#!/bin/bash

check_file() {
    local file=$1
    local line_count=$(wc -l < "$file")
    local template_markers=0

    # Controlla i marker del template
    grep -q "^title:" "$file" && ((template_markers++))
    grep -q "^description:" "$file" && ((template_markers++))
    grep -q "^author:" "$file" && ((template_markers++))
    grep -q "^created:" "$file" && ((template_markers++))
    grep -q "^last_updated:" "$file" && ((template_markers++))
    grep -q "## Sommario" "$file" && ((template_markers++))
    grep -q "## Introduzione" "$file" && ((template_markers++))
    grep -q "## Contenuto" "$file" && ((template_markers++))
    grep -q "## Conclusione" "$file" && ((template_markers++))
    grep -q "Per ulteriori informazioni, consultare la" "$file" && ((template_markers++))

    # Se il file ha tutti i marker del template e meno di 30 righe, è probabilmente un template non compilato
    if [ $template_markers -ge 10 ] && [ $line_count -lt 30 ]; then
        echo "$file è un template non compilato."
    elif [ $line_count -gt 30 ]; then
        echo "$file contiene contenuto significativo."
    else
        echo "$file potrebbe contenere contenuto. Verifica manualmente."
    fi
}

for file in *.md; do
    check_file "$file"
done

echo -e "\nFile con potenziale contenuto significativo:"
for file in *.md; do
    if check_file "$file" | grep -q "contiene contenuto significativo"; then
        echo "$file"
    fi
done
