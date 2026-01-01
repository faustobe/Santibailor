#!/bin/bash
# Script per verificare il database degli impegni

echo "=== Checking Impegni in database ==="
adb shell "sqlite3 /data/data/it.faustobe.santibailor/databases/santibailor_database 'SELECT id, titolo, image_url FROM impegni ORDER BY id DESC LIMIT 5;'" 2>&1
