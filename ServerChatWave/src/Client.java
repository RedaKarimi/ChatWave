import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Client {

    private String nomeServer = "localhost";
    private int portaServer = 7777;
    private boolean connesso = false;
    private Socket server = null;
    private String nomeDelClient = "";
    private BufferedReader datoRicevuto = null;
    private BufferedWriter datoInviato = null;
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private List<String> nomiUtenti;

    public Client() {
    }

    /**
     * Costruttore vuoto della classe Client.
     */
    public Client(int porta) {
        portaServer = porta;
    }

    /**
     * Costruttore della classe Client con porta specificata.
     *
     * @param porta Porta del server a cui connettersi.
     */
    public Client(String indirizzo) {
        nomeServer = indirizzo;
    }

    /**
     * Costruttore della classe Client con indirizzo specificato.
     *
     * @param indirizzo Indirizzo del server a cui connettersi.
     */
    public Client(String indirizzo, int porta) {
        nomeServer = indirizzo;
        portaServer = porta;
    }

    /**
     * Metodo per ottenere e gestire l'input utente.
     *
     * @param min    Il valore minimo accettato.
     * @param max    Il valore massimo accettato.
     * @param custom Opzioni personalizzate aggiuntive.
     * @return La scelta utente valida.
     */
    private String gestoreInput(int min, int max, String... custom) {
        String scelta = null;
        while (scelta == null) {
            try {
                scelta = input.readLine();
                int sceltaInt = Integer.parseInt(scelta);
                if ((sceltaInt < min || sceltaInt > max)) {
                    // Valore inserito non valido
                    System.out.print("Errore: il valore inserito non è tra " + min + " e " + max);
                    if (custom.length > 0) {
                        System.out.print(", o uno di questi comandi: ");
                        for (String command : custom) {
                            System.out.print(command + " ");
                        }
                    }
                    System.out.println();
                    System.out.print(">: ");
                    scelta = null; // Resetta la scelta per il nuovo input
                }
            } catch (NumberFormatException e) {
                // Se l'input non è un numero
                if ((custom.length > 0 && !Arrays.asList(custom).contains(scelta))) {
                    System.out.print("Errore: il valore inserito non è tra " + min + " e " + max);
                    if (custom.length > 0) {
                        System.out.print(", o uno di questi comandi: ");
                        for (String command : custom) {
                            System.out.print(command + " ");
                        }
                    }
                    System.out.println();
                    System.out.print(">: ");
                    scelta = null; // Resetta la scelta per il nuovo input
                }
            } catch (IOException e) {
                System.out.print("Errore: il valore inserito non è tra " + min + " e " + max);
                if (custom.length > 0) {
                    System.out.print(", o uno di questi comandi: ");
                    for (String command : custom) {
                        System.out.print(command + " ");
                    }
                }
                System.out.println();
                System.out.print(">: ");
                scelta = null; // Resetta la scelta per il nuovo input
            }
        }

        return scelta;
    }

    /**
     * Gestisce la disconnessione del client dal server.
     *
     * @throws IOException Eccezione in caso di errore di input/output.
     */
    private void disconnetti() throws IOException {
        server.close();
        connesso = false;
        pulisciSchermo();
        // menu();
    }

    /**
     * Gestisce l'inserimento dei dati da parte dell'utente.
     */
    private void inserimentoDati() {
        boolean connected = false;

        while (!connected) {
            try {
                System.out.print("Inserisci il tuo nome [default: Utente]: ");
                nomeDelClient = input.readLine();

                if (nomeDelClient.trim().isEmpty()) {
                    Random r = new Random();
                    nomeDelClient = "Utente" + r.nextInt(1000);
                } else if (nomeDelClient.contains(" ")) {
                    System.out.println("Il nome non può contenere spazi. Riprova.");
                    continue;
                }
                System.out.print("Inserisci l'indirizzo del server [default: localhost]: ");
                nomeServer = input.readLine();

                if (nomeServer.trim().isEmpty()) {
                    nomeServer = "localhost";
                }
                System.out.print("Inserisci la porta del server [default: 7777]: ");
                String portaInput = input.readLine();

                if (portaInput.trim().isEmpty()) {
                    portaServer = 7777;
                } else {
                    portaServer = Integer.parseInt(portaInput);
                }
                connected = true;

            } catch (NumberFormatException | IOException e) {
                pulisciSchermo();
                System.out.println("Errore nell'inserimento. Riprova.");
            }
        }
    }

    /**
     * Pulisce lo schermo della console.
     */
    private void pulisciSchermo() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Visualizza il menu principale e gestisce la scelta dell'utente.
     *
     * @throws IOException Eccezione in caso di errore di input/output.
     */
    private void inizio() throws IOException {
        pulisciSchermo();
        inserimentoDati();
        while (!connesso) {
            try {
                connetti();
                comunica(nomeDelClient);
            } catch (ConnectException e) {
                pulisciSchermo();
                System.out.println(e.getMessage());
                System.out.println("Riprova.");
                menu();
            }
        }
        /**
         * Inizia il gioco del client, gestendo la connessione al server.
         *
         * @throws IOException Eccezione in caso di errore di input/output.
         */
        Integer codiceRicevuto = Integer.parseInt(datoRicevuto.readLine());
        if (codiceRicevuto == 1) {
            menuChat();
        } else {
            System.out.println("Errore 0 : " + codiceRicevuto);
        }
    }

    private void menuChat() throws IOException {
        pulisciSchermo();
        System.out.println(
                "   _____ _    _       ___          __             \r\n" + //
                        "  / ____| |  | |     | \\ \\        / /             \r\n" + //
                        " | |    | |__| | __ _| |\\ \\  /\\  / /_ ___   _____ \r\n" + //
                        " | |    |  __  |/ _` | __\\ \\/  \\/ / _` \\ \\ / / _ \\\r\n" + //
                        " | |____| |  | | (_| | |_ \\  /\\  / (_| |\\ V /  __/\r\n" + //
                        "  \\_____|_|  |_|\\__,_|\\__| \\/  \\/ \\__,_| \\_/ \\___|\r\n");
        System.out.println("[1] Chatta con qualcuno");
        System.out.println("[0] Esci dall'app");
        System.out.print(">: ");
        String scelta = gestoreInput(0, 1);
        switch (scelta) {
            case "1":
                comunica("Codice:3");
                list();
                break;
            case "0":
                comunica("Codice:0");
                disconnetti();
                break;
            default:
                System.out.println("Valore non valido.");
                break;

        }
    }

    public void list() throws IOException {
        stampaLista();
        System.out.println("vediamo la lista");
         System.out.println("vediamo la lista");
        if (nomiUtenti.size() > 0) {
            System.out.println("[/reload] Ricarica la lista");
            System.out.println("[/exit] Esci dall'app");
            System.out.print(">: ");
            String scelta = gestoreInput(0, nomiUtenti.size(), "/exit", "/reload");
            if (scelta.equals("/exit")) {
                comunica("Codice:0");
                disconnetti();
            } else if (scelta.equals("/reload")) {
                comunica("Codice:3");
                list();
            } else {
                for (int i = 0; i < nomiUtenti.size(); i++) {
                    if (i == Integer.valueOf(scelta)) {
                        String name = nomiUtenti.get(i);
                        pulisciSchermo();
                        chat(name.substring(name.indexOf("]") + 2));
                    }
                }
            }
        } else {
            System.out.println("[1] Ricarica la lista");
            System.out.println("[0] Esci dall'app");
            System.out.print(">: ");
            String scelta = gestoreInput(0, 1);
            switch (scelta) {
                case "1":
                    comunica("Codice:3");
                    list();
                    break;
                case "0":
                    comunica("Codice:0");
                    disconnetti();
                    break;
                default:
                    System.out.println("Valore non valido.");
                    break;

            }
        }
    }
    
    private void chat(String destinatario) throws IOException {
        pulisciSchermo();
        System.out.println("Chat con " + destinatario + ", Digita /return per uscire.");

        // Avvia un thread separato per ricevere continuamente i messaggi dal server
        new Thread(() -> {
            try {
                while (connesso) {
                    String messaggio = datoRicevuto.readLine();
                    if (messaggio.startsWith("msgDa:")) {
                        String[] parts = messaggio.split("\\|");
                        String mittente = parts[0].substring(6);
                        String msg = parts[1].substring(4);
                        System.out.println("\t\t\t" + msg + " [" + mittente + "]");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Invia i messaggi di chat al server
        while (connesso) {
            String messaggio = input.readLine();
            if (messaggio.equals("/return")) {
                menuChat();
                break;
            } else {
                System.out.print("\033[1A\033[K"); // Move up one line and clear the line
                System.out.println("[" + nomeDelClient + "] " + messaggio);
                comunica("chatCon:" + destinatario + "|msg:" + messaggio);
            }
        }
    }

    private void stampaLista() throws IOException {
        System.out.print("technicamente deve stampare la lista");
        String stringaNomi = datoRicevuto.readLine();
        System.out.println(stringaNomi);
        stringaNomi=stringaNomi.replace(nomeDelClient + "|", "");
        nomiUtenti = new ArrayList<>();

        // Dividi la stringa utilizzando il separatore "|"
        String[] nomi = stringaNomi.split("\\|");

        // Aggiungi i nomi degli utenti alla lista dinamica
        System.out.println("lista degli utenti attivi:");
        for (int i = 0; i < nomi.length; i++) {
            if (!nomi[i].isEmpty()) {
                nomiUtenti.add("[" + i + "] " + nomi[i]);
                System.out.println("[" + i + "] " + nomi[i]);
            } else {
                System.out.println("nessun utente è online in questo momento");
            }
        }
    }

    /**
     * Costruttore della classe Client con indirizzo e porta specificati.
     *
     * @param indirizzo Indirizzo del server a cui connettersi.
     * @param porta     Porta del server a cui connettersi.
     */
    private Socket connetti() throws ConnectException {
        System.out.println("Connessione al server...");
        try {
            server = new Socket(nomeServer, portaServer);
            datoRicevuto = new BufferedReader(new InputStreamReader(server.getInputStream()));
            datoInviato = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
            connesso = true;
        } catch (IOException e) {
            throw new ConnectException("Errore di connessione al server: " + e.getMessage());
        }
        return server;
    }

    /**
     * Comunica con il server inviando una stringa di testo.
     *
     * @param stringaUtente Stringa da inviare al server.
     */
    private void menu() throws IOException {
        System.out.println("[1] Connettiti al server");
        System.out.println("[0] Esci dal programma");
        System.out.print(">: ");
        String scelta = gestoreInput(0, 1);
        switch (scelta) {
            case "1" -> inizio();
            case "0" -> System.exit(0);
            default -> System.out.println("Valore non valido.");
        }
    }

    /**
     * Connette il client al server.
     *
     * @return Socket del server a cui è stato connesso il client.
     * @throws ConnectException Eccezione in caso di errore durante la connessione
     *                          al server.
     */
    private void comunica(String stringaUtente) {
        try {
            datoInviato.write(stringaUtente + '\n');
            datoInviato.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo principale che avvia il client.
     *
     * @param args Argomenti della riga di comando.
     * @throws IOException Eccezione in caso di errore di input/output.
     */
    public static void main(String[] args) throws IOException {
        Client cliente = new Client();
        cliente.pulisciSchermo();
        cliente.menu();
    }
}
