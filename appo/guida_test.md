```markdown
# Guida completa ai test per RicorrenzaViewModel

## Esecuzione dei test

### Da riga di comando

1. Eseguire tutti i test:
   ```
   ./gradlew test
   ```

2. Eseguire test specifici per il modulo app:
   ```
   ./gradlew :app:testDebugUnitTest
   ```

3. Eseguire una specifica classe di test:
   ```
   ./gradlew :app:testDebugUnitTest --tests "it.faustobe.santibailor.viewmodel.RicorrenzaEntityViewModelTest"
   ```

4. Eseguire un metodo di test specifico:
   ```
   ./gradlew :app:testDebugUnitTest --tests "it.faustobe.santibailor.viewmodel.RicorrenzaEntityViewModelTest.testLoadRicorrenzeDelGiorno"
   ```

5. Generare un report di copertura dei test:
   ```
   ./gradlew :app:createDebugUnitTestCoverageReport
   ```

### Da Android Studio

6. Eseguire tutti i test: Tasto destro sulla cartella `test` > Run 'Tests in 'app''

7. Eseguire una classe di test: Apri la classe `RicorrenzaViewModelTest`, tasto destro > Run 'RicorrenzaViewModelTest'

8. Eseguire un singolo metodo di test: Clicca sull'icona play accanto al metodo di test

9. Eseguire i test con copertura: Run > Run 'All Tests' with Coverage

10. Debug di un test: Imposta un breakpoint e usa Debug invece di Run

## Iterare e migliorare

11. Analizza la copertura del codice:
    Dopo aver eseguito i test con copertura, esamina il report per identificare parti del codice non coperte dai test.

12. Aggiungi test per le aree non coperte:
    ```java
    @Test
    public void testResetSearch() {
        viewModel.resetSearch();
        assertEquals(0, getValue(viewModel.getCurrentPage()).intValue());
        assertFalse(getValue(viewModel.getIsLastPage()));
        assertFalse(getValue(viewModel.getIsLoading()));
        assertEquals(0, getValue(viewModel.getTotalSearchResults()).intValue());
        assertTrue(getValue(viewModel.getRisultatiRicercaAvanzata()).isEmpty());
    }
    ```

13. Refactoring dei test esistenti:
    Identifica test simili e crea metodi helper per ridurre la duplicazione del codice.
    ```java
    private void assertSearchState(int expectedPage, boolean expectedIsLastPage, boolean expectedIsLoading, int expectedTotalResults) {
        assertEquals(expectedPage, getValue(viewModel.getCurrentPage()).intValue());
        assertEquals(expectedIsLastPage, getValue(viewModel.getIsLastPage()));
        assertEquals(expectedIsLoading, getValue(viewModel.getIsLoading()));
        assertEquals(expectedTotalResults, getValue(viewModel.getTotalSearchResults()).intValue());
    }
    ```

14. Aggiungi test parametrizzati:
    ```java
    @ParameterizedTest
    @CsvSource({
        "1, 0, 2",
        "31, 11, 5",
        "15, 5, 3"
    })
    public void testLoadRicorrenzeDelGiorno(int giorno, int mese, int expectedSize) {
        // Arrange
        List<RicorrenzaConTipo> expectedRicorrenze = createMockRicorrenze(expectedSize);
        when(ricorrenzaRepository.getRicorrenzeDelGiornoPaginate(giorno, mese, 0, viewModel.getPageSize()))
            .thenReturn(expectedRicorrenze);

        // Act
        viewModel.loadRicorrenzeDelGiorno(giorno, mese);

        // Assert
        List<RicorrenzaConTipo> result = getValue(viewModel.getRicorrenzeDelGiorno());
        assertEquals(expectedSize, result.size());
    }

    private List<RicorrenzaConTipo> createMockRicorrenze(int size) {
        // Implementa la creazione di mock ricorrenze
    }
    ```

## Testare casi limite e scenari di errore

15. Test con input invalidi:
    ```java
    @Test
    public void testLoadRicorrenzeDelGiornoWithInvalidDate() {
        // Arrange
        int invalidGiorno = 32;
        int invalidMese = 13;

        // Act
        viewModel.loadRicorrenzeDelGiorno(invalidGiorno, invalidMese);

        // Assert
        List<RicorrenzaConTipo> result = getValue(viewModel.getRicorrenzeDelGiorno());
        assertTrue(result.isEmpty());
        // Verifica che sia stato loggato un errore o gestito in altro modo
    }
    ```

16. Test di timeout:
    ```java
    @Test
    public void testLoadRicorrenzeDelGiornoTimeout() {
        // Arrange
        when(ricorrenzaRepository.getRicorrenzeDelGiornoPaginate(anyInt(), anyInt(), anyInt(), anyInt()))
            .thenAnswer(invocation -> {
                Thread.sleep(5000); // Simula una lunga operazione
                return Collections.emptyList();
            });

        // Act & Assert
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            viewModel.loadRicorrenzeDelGiorno(1, 1);
            // Verifica che il ViewModel gestisca correttamente il timeout
        });
    }
    ```

17. Test di errori di rete:
    ```java
    @Test
    public void testLoadRicorrenzeDelGiornoNetworkError() {
        // Arrange
        when(ricorrenzaRepository.getRicorrenzeDelGiornoPaginate(anyInt(), anyInt(), anyInt(), anyInt()))
            .thenThrow(new IOException("Network error"));

        // Act
        viewModel.loadRicorrenzeDelGiorno(1, 1);

        // Assert
        // Verifica che il ViewModel gestisca correttamente l'errore di rete
        assertTrue(getValue(viewModel.getErrorState()));
        assertEquals("Network error", getValue(viewModel.getErrorMessage()));
    }
    ```

18. Test di casi limite per la paginazione:
    ```java
    @Test
    public void testLoadNextPageWhenOnLastPage() {
        // Arrange
        viewModel.setIsLastPage(true);

        // Act
        viewModel.loadNextPage();

        // Assert
        verify(ricorrenzaRepository, never()).ricercaAvanzataPaginata(any(), any(), any(), any(), anyInt(), anyInt());
    }
    ```

19. Test di concorrenza:
    ```java
    @Test
    public void testConcurrentSearchRequests() throws InterruptedException {
        // Arrange
        CountDownLatch latch = new CountDownLatch(2);
        
        // Act
        new Thread(() -> {
            viewModel.eseguiRicercaAvanzata("Test1", 1, "01/01", "31/12");
            latch.countDown();
        }).start();
        
        new Thread(() -> {
            viewModel.eseguiRicercaAvanzata("Test2", 2, "01/01", "31/12");
            latch.countDown();
        }).start();
        
        latch.await(5, TimeUnit.SECONDS);

        // Assert
        // Verifica che solo l'ultima ricerca sia stata effettivamente eseguita
        assertEquals("Test2", getValue(viewModel.getLastSearchQuery()));
    }
    ```

20. Test di memoria insufficiente:
    ```java
    @Test
    public void testOutOfMemoryHandling() {
        // Arrange
        when(ricorrenzaRepository.getRicorrenzeDelGiornoPaginate(anyInt(), anyInt(), anyInt(), anyInt()))
            .thenThrow(new OutOfMemoryError("Simulated OOM"));

        // Act & Assert
        assertDoesNotThrow(() -> viewModel.loadRicorrenzeDelGiorno(1, 1));
        assertTrue(getValue(viewModel.getErrorState()));
        assertEquals("Errore di memoria", getValue(viewModel.getErrorMessage()));
    }
    ```

Questi esempi coprono una vasta gamma di scenari di test, inclusi casi limite e gestione degli errori. Ricorda di adattare questi test al tuo specifico ViewModel e alle sue funzionalità. Continua a iterare e migliorare i tuoi test man mano che sviluppi nuove funzionalità o identifichi nuovi casi d'uso.
```
