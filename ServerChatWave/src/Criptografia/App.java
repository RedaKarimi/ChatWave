package Criptografia;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class App {

    // Legge un numero primo casuale da il file
    public static BigInteger random_NumPrimo_dalfile() throws IOException {
        String fileName = "src\\NumeriPrimiTrovati.txt"; // Nome del file contenente dove contiene i numeri primi
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = "";
        BigInteger numero = null;
        int lineCount = 0;
        Random random = new Random();

        // Legge casualmente una riga dal file
        while ((line = reader.readLine()) != null) {
            lineCount++;
            if (random.nextInt(lineCount) == 0) {
                numero = new BigInteger(line); // Converte la riga letta in un BigInteger
            }
        }
        reader.close();
        return numero; // Restituisce il numero primo casuale
    }

    public static void main(String[] args) throws Exception {
        System.out.print("\033[H\033[2J"); // Pulisce il terminale
        System.out.flush();

        // Crea un oggetto RSA con due numeri primi casuali
        RSA rsa = new RSA(random_NumPrimo_dalfile(), random_NumPrimo_dalfile());

        // Legge un messaggio da tastiera
        BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Inserisci il messaggio da criptare: ");
        String msg = tastiera.readLine();

        // Cripta il messaggio
        BigInteger[] msg_criptato = rsa.criptare(msg);

        // Decripta il messaggio criptato
        String msg_decriptato = rsa.decriptare(msg_criptato);

        // Stampa il messaggio originale, il messaggio criptato e il messaggio decriptato
        System.out.println("\nmessaggio: " + msg);
        System.out.println("messaggio criptato: " + Arrays.toString(msg_criptato));
        System.out.println("messaggio decriptato: " + msg_decriptato);
    }
}
