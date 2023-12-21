<?php

/**
 * Italian strings for lamslesson
 *
 * You can have a rather longer description of the file as well,
 * if you like, and it can span multiple lines.
 *
 * @package   mod_lamslesson
 * @copyright 2023 LAMS Foundation - Ernie Ghiglione (ernieg@lamsfoundation.org)
 * @license  http://www.gnu.org/licenses/gpl-2.0.html GNU GPL v2
 */

defined('MOODLE_INTERNAL') || die();

$string["modulename"] = "Lezione LAMS";
$string["modulenameplural"] = "Lezioni LAMS";
$string["modulename_help"] = "Il modulo LAMS Lesson consente agli insegnanti di creare lezioni LAMS all'interno di Moodle.

 LAMS fornisce agli insegnanti un ambiente intuitivo di authoring visivo per la creazione di progetti di attività di apprendimento. Queste attività possono includere una serie di attività individuali, lavori in piccoli gruppi e attività di tutta la classe basate sia sui contenuti che sulla collaborazione.

 Una volta creata una sequenza, può essere riutilizzata in uno o più corsi.

 Inoltre, LAMS fornisce un'interfaccia di follow-up e monitoraggio in tempo reale in cui gli insegnanti possono interagire con gli studenti durante le attività di apprendimento.

 Per ulteriori informazioni visitare: lamsfoundation.org.";
$string["modulename_link"] = "lamslesson";
$string["lamslessonfieldset"] = "Set di campi di esempio personalizzato";
$string["lamslessonname"] = "Nome della lezione";
$string["lamslessonname_help"] = "Questo è il contenuto del tooltip della guida associato al campo lamslessonname. La sintassi Markdown è supportata.";
$string["lamslesson"] = "Lezione LAMS";
$string["pluginadministration"] = "LAMS Amministrazione delle lezioni";
$string["pluginname"] = "Lezione LAMS";
$string["selectsequence"] = "Seleziona il disegno";
$string["displaydesign"] = "Visualizzare il design dell'immagine?";
$string["displaydesign_help"] = "Se abilitato, quando la lezione viene mostrata agli studenti, mostrerà il diagramma di progettazione dell'apprendimento.";
$string["allowlearnerrestart"] = "Gli studenti possono ricominciare la lezione?";
$string["allowlearnerrestart_help"] = "Se abilitato, gli studenti potranno ricominciare la lezione e ricominciare dall'inizio in qualsiasi momento. Ad ogni riavvio, i progressi precedenti vengono eliminati.";
$string["availablesequences"] = "Disegni";
$string["openauthor"] = "Autore nuove lezioni LAMS";
$string["refresh"] = "Aggiornare";
$string["lamslesson:manage"] = "Gestisci le lezioni";
$string["lamslesson:participate"] = "Partecipa alle lezioni";
$string["adminheader"] = "Configurazione del server LAMS";
$string["admindescription"] = "Configura le impostazioni del tuo server LAMS. Assicurati <strong> che </strong> i valori che inserisci qui corrispondano a quelli che hai già inserito nel tuo server LAMS. Altrimenti l'integrazione potrebbe non funzionare.";
$string["serverurl"] = "URL del server LAMS:";
$string["serverurlinfo"] = "Qui devi inserire l'URL del tuo server LAMS. ad esempio: http://localhost:8080/lams/.";
$string["serverid"] = "ID server:";
$string["serveridinfo"] = "Qual è l'ID server che hai inserito nel tuo server LAMS?";
$string["serverkey"] = "Chiave del server:";
$string["serverkeyinfo"] = "Qual è la chiave del server che hai inserito nel tuo server LAMS?";
$string["validationbutton"] = "Convalida le impostazioni";
$string["validationheader"] = "Convalida delle impostazioni";
$string["validationinfo"] = "Prima di salvare le impostazioni, premere il pulsante per convalidarle con il server LAMS. Se la convalida è corretta, salva queste impostazioni. In caso contrario, verificare che le impostazioni immesse corrispondano ai valori nel server LAMS";
$string["validationhelp"] = "Ho bisogno di aiuto? controlla il";
$string["offsetbutton"] = "Calcola offset";
$string["offsetinfo"] = "Se stai applicando il limite di durata della richiesta di accesso per vivere, è importante impostare la differenza di orario (in minuti) tra LAMS e il tuo server Moodle. Fai clic su Calcola offset per vedere se c'è una differenza di fuso orario tra il tuo LAMS e il server Moodle. Prendi l'orario di offset visualizzato e aggiungilo all'impostazione \"Offset fuso orario\".";
$string["servertimeoffset"] = "Differenza oraria (minuti)";
$string["servertimeoffsetinfo"] = "Questa è la differenza di orario (o differenza di orario) tra il LAMS e il server Moodle.";
$string["offsetheader"] = "Differenza di fuso orario";
$string["lamsmoodlehelp"] = "Esercitazione sull'integrazione LAMS-Moodle";
$string["validationsuccessful"] = "Convalida riuscita! Ora puoi salvare le tue impostazioni e iniziare a utilizzare LAMS all'interno di Moodle.";
$string["validationfailed"] = "Convalida non riuscita: verificare che le impostazioni immesse corrispondano a quelle in LAMS";
$string["restcallfail"] = "Chiamata a LAMS non riuscita: non è stata ricevuta risposta o la connessione è stata rifiutata. Verifica di avere l'URL del server LAMS corretto e che sia online.";
$string["sequencenotselected"] = "È necessario selezionare un design per procedere.";
$string["previewthislesson"] = "Anteprima lezione";
$string["updatewarning"] = "Attenzione: la selezione di un disegno diverso da quello attuale creerà una nuova lezione per gli studenti. Ciò potrebbe causare alcuni studenti leggermente confusi";
$string["currentsequence"] = "Disegno attuale:";
$string["nolessons"] = "Non ci sono ancora lezioni LAMS in questo caso.";
$string["lessonname"] = "Nome della lezione";
$string["links"] = "Collegamenti";
$string["introduction"] = "introduzione";
$string["openmonitor"] = "Monitora questa lezione";
$string["lastmodified"] = "Ultima modifica";
$string["openlesson"] = "Apri la lezione";
$string["empty"] = "vuoto";
$string["completionfinish"] = "Mostra come completo quando l'utente ha terminato la lezione";
$string["yourprogress"] = "I progressi della tua lezione";
$string["youhavecompleted"] = "Hai completato:";
$string["outof"] = "su circa";
$string["lessonincompleted"] = "La lezione non è ancora terminata";
$string["lessoncompleted"] = "Hai completato questa lezione";
$string["activities"] = "attività";
$string["ymmv"] = "Il totale delle attività dipende dal vostro percorso di apprendimento.";
$string["yourmarkis"] = "Il voto finale è:";
$string["outofmark"] = "fuori da";
$string["lamslesson:addinstance"] = "Aggiungere lezioni LAMS";
