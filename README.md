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

## Suddivisione dei compiti

- Giacomo Fantini:
Implementazione della griglia della scacchiera base e il posizionamento dei pezzi standard. Gestione degli eventi di click per la selezione del pezzo. Logica dei movimenti dei pezzi (Alfiere, Torre, Regina, Cavallo, Re).  Extra: Implementazione degli effetti casuali.

- Patrick Battazza:
Implementazione visiva di Scacco Matto, Patta, e Scacco (es: colore del Re in pericolo o finestre di notifica). Regole Avanzate (Arrocco, En Passant, Scacco Matto, Patta). Extra: Implementazione logica dei pezzi custom.

- Brando Fabbri:
Implementazione del bottone "Surrender" (termina la partita senza che sia avvenuto uno Scacco Matto o una Patta). Implementazione dell'Handler dei Turni e del Timer. Implementazione del sistema di promozione (finestra di selezione del pezzo). Extra: Sviluppo della view di selezione dei Loadout (interfaccia per scambiare pezzi standard/custom).

- Luca Bevilacqua:
Sviluppo di tutte le scene di navigazione (Menu Principale, Impostazioni). Salva/Carica partita. Interprete delle stringhe delle mosse implementazione della View del Log delle Mosse. Sistema di punteggio. Extra: Implementazione del Replay e dei suoi controlli.
