# Santibailor - Calendario Santi

## Tech Stack
- Linguaggio: **Java**
- Build: Gradle CLI
- Testing: Dispositivo fisico via ADB (no emulatore - 8GB RAM)

## Workflow Build
```bash
# Build rapido + install
./build-install.sh

# Solo build
./gradlew assembleDebug

# Solo install
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Vedere log app
./show-logs.sh
```

## Funzionalità Principali
- Calendario santi personalizzabile
- Lista impegni  
- Tracker spese
- [altre funzioni da documentare]

## Package
it.faustobe.santibailor
