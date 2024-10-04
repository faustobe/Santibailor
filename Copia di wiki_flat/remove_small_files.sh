#!/bin/bash

# Dimensione minima in byte (1 KiB = 1024 byte)
min_size=1024

# Conta i file rimossi
removed_count=0

# Itera su tutti i file .md nella directory corrente
for file in *.md; do
    # Controlla se il file esiste (per evitare errori se non ci sono file .md)
    if [ -f "$file" ]; then
        # Ottieni la dimensione del file in byte
        size=$(stat -f%z "$file")
        
        # Se il file è più piccolo di min_size, rimuovilo
        if [ $size -lt $min_size ]; then
            rm "$file"
            echo "Rimosso: $file ($(($size)) byte)"
            ((removed_count++))
        fi
    fi
done

echo "Operazione completata. $removed_count file rimossi."
