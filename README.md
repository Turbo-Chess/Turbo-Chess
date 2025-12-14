# Analisi

- <brando.fabbri@studio.unibo.it>
- <giacomo.fantini5@studio.unibo.it>
- <patrick.battazza@studio.unibo.it>
- <luca.bevilacqua6@studio.unibo.it>

## Obiettivo

Realizzare una versione di scacchi con funzionalità aggiuntive.

## Descrizione

Ci si è posto come obiettivo quello di ricreare il gioco di scacchi con un nuovo tocco moderno, aggiungendo pezzi custom, loadout modificabile pre-partita e effetti randomizzati. Si punta a realizzare anche funzionalità Quality-of-Life come Log della partita e Replay delle mosse/partite.

## "Challenge" principali

- Assicurarsi che le regole generali di scacchi (Arrocco / Patta / Scacco matto) funzionino senza problemi senza e con i pezzi custom
- Fare in modo che, selezionando un pezzo, la griglia faccia vedere in modo comprensivo le mosse possibili con giusti aggiornamenti alla View
- Implementare correttamente la funzionalità di Replay (gestita similarmente a quella di chess.com), assicurandosi che il player non provi a compiere mosse nel mentre
- Creare l'interprete della stringa delle mosse e che sia in grado di leggere e fare il parsing delle stringhe, che verrà usato per il Replay
- Il sistema di salvataggio della partita in caso di interruzione deve funzionare e deve chiedere all'utente se vuole continuare la partita nel momento del riavvio dell'applicazione.
