import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Utente extends Thread {
    private Socket socketCliente;
    private BufferedReader dalCliente;
    private PrintWriter alCliente;
    private Server server;
    private String nomeDelUtente;

    public Utente(Socket socketCliente, Server server) {
        this.socketCliente = socketCliente;
        this.server = server;
        try {
            this.dalCliente = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            this.alCliente = new PrintWriter(socketCliente.getOutputStream(), true);
        } catch (IOException e) {
            gestisciEccezioneIO(e, "Errore nella creazione di BufferedReader/PrintWriter");
        }
    }

    public String getNomeDelUtente() {
        return nomeDelUtente;
    }

    @Override
    public void run() {
        try {
            inviaMessaggio("1");
            System.out.println(dalCliente.readLine());
            while (!socketCliente.isClosed()) {
                if (dalCliente.ready()) {
                    String codiceRicevuto = dalCliente.readLine();
                    menu(codiceRicevuto);
                }
            }
        } catch (IOException e) {
            gestisciEccezioneIO(e, "Errore nel metodo run");
        } finally {
            disconnectUtente();
        }
    }

    private void inviaMessaggio(String messaggio) {
        synchronized (alCliente) {
            alCliente.println(messaggio);
        }
    }

    private void disconnectUtente() {
        try {
            server.rimuoviUtente(this);
            socketCliente.close();
        } catch (IOException e) {
            gestisciEccezioneIO(e, "Errore nella disconnessione del Utente");
        }
    }

    private void chat() {
        boolean giocoFinito = false;
        while (!giocoFinito) {
            inviaMessaggio(String.valueOf(server.gioco.getNumber(numeriIndovinati)));
            if (numeriIndovinati + 1 < server.gioco.getSize()) {
                inviaMessaggio("1");
                numeriIndovinati++;
            } else {
                inviaMessaggio("0");
                giocoFinito = true;
            }
            server.listaGiocatori.replace(nomeDelGiocatore, String.valueOf(nomeDelGiocatore));
        }
    } 

    private void invioListaUtenti() {
        StringBuilder classifica = new StringBuilder();
        server.listaUtenti.forEach((giocatore, punti) -> classifica.append(String.format("%s|", giocatore)));
        inviaMessaggio(classifica.toString());
    }

    private void menu(String scelta) throws IOException {
        if (scelta.startsWith("Codice:")) {
            int codice = Integer.parseInt(scelta.substring(7));
            switch (codice) {
                case 0:
                    disconnectUtente();
                    interrupt();
                    break;
                case 1:
                    //chat();
                    break;
                case 3:
                    invioListaUtenti();
                    break;
                default:
                    System.out.println("Valore non valido.");
                    break;
            }
        } else if (scelta.startsWith("Nome:")) {
            this.nomeDelUtente = scelta.substring(5);
        }
    }

    private void gestisciEccezioneIO(IOException e, String messaggio) {
        e.printStackTrace();
        System.err.println(messaggio);
    }
}
