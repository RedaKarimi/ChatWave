import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * La classe Server rappresenta il server del gioco dell'indovinello.
 * @author Reda Karimi
 */
public class Server {

    private static final int DEFAULT_PORT = 7777;
    private ServerSocket server;
    private int porta;

    public Map<String, Utente> listaUtenti;

    /**
     * Costruttore di default che inizializza il server sulla porta predefinita.
     *
     * @throws IOException Eccezione in caso di errore durante l'apertura del ServerSocket.
     */
    public Server() throws IOException {
        this(DEFAULT_PORT);
    }

    /**
     * Costruttore che inizializza il server sulla porta specificata e inizializza il gioco.
     *
     * @param porta Porta su cui avviare il server.
     * @throws IOException Eccezione in caso di errore durante l'apertura del ServerSocket.
     */
    public Server(int porta) throws IOException {
        this.porta = porta;
        server = new ServerSocket(porta);
        listaUtenti = new HashMap<>();
    }

    /**
     * Metodo privato che gestisce l'accettazione di nuovi Utenti.
     *
     * @throws IOException Eccezione in caso di errore durante l'accettazione della connessione.
     */
    private void accetta() throws IOException {
        Socket client = server.accept();
        Utente utente = new Utente(client, this);
        utente.start();
        System.out.printf("<%s> Si è connesso %n", utente.getNomeDelUtente());
        listaUtenti.put(utente.getNomeDelUtente(), utente);
    }

    /**
     * Metodo privato che avvia il server in attesa di connessioni.
     */
    private void avvia() {
        try {
            while (true) {
                accetta();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rimuove un utente dal gioco.
     *
     * @param utente utente da rimuovere.
     */
    public void rimuoviUtente(Utente utente) {
        listaUtenti.remove(utente.getNomeDelUtente());
        System.out.printf("<%s> Si è disconnesso%n", utente.getNomeDelUtente());
    }

    /**
     * Punto di ingresso principale per l'avvio del server.
     *
     * @param args Argomenti della riga di comando (non utilizzati in questo caso).
     */
    public static void main(String[] args) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        try {
            Server servente = new Server();
            System.out.printf("Server avviato sulla porta [%d].%n", servente.porta);
            servente.avvia();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
