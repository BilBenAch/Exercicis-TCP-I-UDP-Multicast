package Act6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServidorLlista {
    /* Servidor TCP que genera un número perquè ClientLlista.java jugui a encertar-lo
     * i on la comunicació dels diferents jugador passa per el Thread : ThreadServidorAdivina.java
     * */

    int port;

    public ServidorLlista(int port ) {
        this.port = port;
    }

    public void listen() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            while(true) { //esperar connexió del client i llançar thread
                clientSocket = serverSocket.accept();
                //Llançar Thread per establir la comunicació
                ThreadSevidorLlista FilServidor = new ThreadSevidorLlista(clientSocket);
                Thread client = new Thread(FilServidor);
                client.start();
            }
        } catch ( IOException ex) {
            Logger.getLogger(ServidorLlista.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
		/*if (args.length != 1) {
            System.err.println("Usage: java ServidorLlista <port number>");
            System.exit(1);
        }*/


        //int port = Integer.parseInt(args[0]);
        ServidorLlista srv = new ServidorLlista(5558);
        srv.listen();

    }

}