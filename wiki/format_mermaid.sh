#!/bin/bash

# Funzione per aggiungere i delimitatori mermaid
format_mermaid() {
    awk '
    BEGIN { in_mermaid = 0; mermaid_content = "" }
    /^```mermaid$/ { in_mermaid = 1; print; next }
    /^```$/ && in_mermaid { in_mermaid = 0; print; next }
    in_mermaid { print; next }
    /^graph |^sequenceDiagram |^gantt |^classDiagram |^stateDiagram |^pie |^flowchart |^erDiagram/ {
        if (!in_mermaid) {
            print "```mermaid"
            in_mermaid = 1
        }
    }
    { print }
    END {
        if (in_mermaid) {
            print "```"
        }
    }
    ' "$1" > "$1.tmp" && mv "$1.tmp" "$1"
}

# Trova tutti i file .md e applica la formattazione
find . -name "*.md" | while read file; do
    format_mermaid "$file"
    echo "Formattato $file"
done

echo "Formattazione dei diagrammi Mermaid completata!"
