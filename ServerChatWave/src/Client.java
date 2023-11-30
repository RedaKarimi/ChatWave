import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
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
     * Gestisce l'input dell'utente.
     *
     * @param min Valore minimo accettabile.
     * @param max Valore massimo accettabile.
     * @return Scelta dell'utente.
     */
    private int gestoreInput(int min, int max) {
        Integer scelta = null;
        while (scelta == null) {
            try {
                scelta = Integer.parseInt(input.readLine());
                if (scelta < min || scelta > max) {
                    System.out.println("Errore: il valore inserito non è tra " + min + " e " + max);
                    System.out.print(">: ");
                    scelta = null;
                }
            } catch (NumberFormatException | IOException e) {
                System.out.println("Errore: inserisci un valore numerico.");
                System.out.print(">: ");
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

    private void menuChat() throws IOException{
        System.out.println(
                "   _____ _    _       ___          __             \r\n" + //
                "  / ____| |  | |     | \\ \\        / /             \r\n" + //
                " | |    | |__| | __ _| |\\ \\  /\\  / /_ ___   _____ \r\n" + //
                " | |    |  __  |/ _` | __\\ \\/  \\/ / _` \\ \\ / / _ \\\r\n" + //
                " | |____| |  | | (_| | |_ \\  /\\  / (_| |\\ V /  __/\r\n" + //
                "  \\_____|_|  |_|\\__,_|\\__| \\/  \\/ \\__,_| \\_/ \\___|\r\n" );
        System.out.println("[1] Chatta con qualcuno");
        System.out.println("[0] Esci dall'app");
        System.out.print(">: ");
        int scelta = gestoreInput(0, 1);
        switch (scelta) {
            case 1:
                comunica("Codice:1");
                stampaLista();
                break;
            case 0:
                comunica("Codice:0");
                disconnetti();
                break;
            default:
                System.out.println("Valore non valido.");
                break;

        }
    }

    private void stampaLista() throws IOException{
      /*   System.out.println(
                "   _____ _    _       ___          __             \r\n" + //
                "  / ____| |  | |     | \\ \\        / /             \r\n" + //
                " | |    | |__| | __ _| |\\ \\  /\\  / /_ ___   _____ \r\n" + //
                " | |    |  __  |/ _` | __\\ \\/  \\/ / _` \\ \\ / / _ \\\r\n" + //
                " | |____| |  | | (_| | |_ \\  /\\  / (_| |\\ V /  __/\r\n" + //
                "  \\_____|_|  |_|\\__,_|\\__| \\/  \\/ \\__,_| \\_/ \\___|\r\n" );
        System.out.println("[1] Chatta con qualcuno");
        System.out.println("[0] Esci dall'app");
        System.out.print(">: ");
        int scelta = gestoreInput(0, 1);
        switch (scelta) {
            case 1:
                comunica("Codice:1");
                //gioca();
                break;
            case 0:
                comunica("Codice:0");
                disconnetti();
                break;
            default:
                System.out.println("Valore non valido.");
                break;

        } */
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
        int scelta = gestoreInput(0, 1);
        switch (scelta) {
            case 1 -> inizio();
            case 0 -> System.exit(0);
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
        cliente.menuChat();
    }
} 
