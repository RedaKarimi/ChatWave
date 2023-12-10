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
            this.nomeDelUtente = dalCliente.readLine();
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
            while (!socketCliente.isClosed()) {
                if (dalCliente.ready()) {
                    String codiceRicevuto = dalCliente.readLine();
                    menu(codiceRicevuto);
                }
            }
        } catch (IOException e) {
            gestisciEccezioneIO(e, "Errore nel metodo run");
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

    private void invioMessaggio(String desinatario,String messaggio) {
        Utente utenteDestinatario = server.listaUtenti.get(desinatario);
        utenteDestinatario.inviaMessaggio("msgDa:"+nomeDelUtente+"|msg:"+messaggio);
    } 

    private void invioListaUtenti() {
        System.out.println("1");
        StringBuilder classifica = new StringBuilder();
        System.out.println("2");
        server.listaUtenti.forEach((nomeUtente, utente) -> classifica.append(String.format("%s|", nomeUtente)));
        System.out.println("3");
        System.out.println(classifica.toString());  
        inviaMessaggio(classifica.toString());
    }

    private void menu(String scelta) throws IOException {
        System.out.println(scelta);
        if (scelta.startsWith("Codice:")) {
            int codice = Integer.parseInt(scelta.substring(7));
            switch (codice) {
                case 0:
                    disconnectUtente();
                    interrupt();
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
        } else if (scelta.startsWith("chatCon:")) {
            String[] parti = scelta.split("\\|");
            String destinatario = parti[0].substring(8);
            String messaggio = parti[1].substring(4);
            invioMessaggio(destinatario, messaggio);
        } 
        
    }

    private void gestisciEccezioneIO(IOException e, String messaggio) {
        e.printStackTrace();
        System.err.println(messaggio);
    }
}
