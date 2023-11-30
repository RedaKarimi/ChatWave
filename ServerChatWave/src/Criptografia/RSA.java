package Criptografia;
import java.math.BigInteger;
import java.util.Random;

public class RSA {

    // Dichiarazione delle variabili
    private BigInteger q, p;
    private BigInteger[][] chiavi;
    private Random random = new Random();

    public RSA(BigInteger q, BigInteger p) {
        this.q = q;
        this.p = p;
    }

    // Genera le chiavi pubbliche e private
    private void generaChiavi() {
        BigInteger n = q.multiply(p); // Calcola il modulo n
        BigInteger phi_n = q
                .subtract(BigInteger.ONE)
                .multiply(p.subtract(BigInteger.ONE)); // Calcola la funzione di Eulero phi(n)
        BigInteger e = new BigInteger(phi_n.bitLength(), random); // Genera un numero casuale e
        e = e.mod(phi_n.subtract(BigInteger.ONE)).add(BigInteger.ONE);

        while (e.gcd(phi_n).compareTo(BigInteger.ONE) != 0) {
            e = new BigInteger(phi_n.bitLength(), random); // Ripeti finché e e phi_n non sono coprimi
            e = e.mod(phi_n.subtract(BigInteger.ONE)).add(BigInteger.ONE);
        }
        BigInteger d = e.modInverse(phi_n); // Calcola l'inverso moltiplicativo di e modulo phi_n
        chiavi = new BigInteger[][] { { e, n }, { d, n } }; // Salva le chiavi pubbliche e private
    }

    // Cripta una stringa di testo
    public BigInteger[] criptare(String testo) {
        generaChiavi();
        BigInteger[] testoCriptato = new BigInteger[testo.length()];
        BigInteger chiavePubblica[] = chiavi[0];
        for (int i = 0; i < testoCriptato.length; i++) {
            BigInteger msg = new BigInteger(String.valueOf((int) testo.charAt(i))); // Converte il carattere in un umero intero
            testoCriptato[i] = msg.modPow(chiavePubblica[0], chiavePubblica[1]); // Esegue l'esponenziazione modulare
        }
        return testoCriptato;
    }

    // Decripta un array di BigInteger in una stringa di testo
    public String decriptare(BigInteger[] testoCriptato) {
        String testoDecriptato = "";
        BigInteger chiavePrivata[] = chiavi[1];

        for (int i = 0; i < testoCriptato.length; i++) {
            BigInteger valoreDecriptato = testoCriptato[i].modPow(chiavePrivata[0], chiavePrivata[1]); // Decripta elemento
            String carattereDecriptato = new String(valoreDecriptato.toByteArray()); // Converte il numero in carattere
            testoDecriptato += carattereDecriptato;
        }

        return testoDecriptato;
    }
}
