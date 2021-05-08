package Act6;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientLlista extends Thread {
    /* CLient TCP que ha endevinar un número pensat per ServidorLlista.java */

    String hostname;
    int port;
    boolean continueConnected;
    List<Integer> llista;
    Llista llistaobj = null;
    Llista recivedList = null;
    ObjectInputStream in;
    ObjectOutputStream out;

    public ClientLlista(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        continueConnected = true;
    }

    public void run() {

        Socket socket;
        //inicialitzem una llista y la pasem al objecte Llista
        llista = new ArrayList<>();
        llista.add(10);
        llista.add(50);
        llista.add(70);
        llista.add(50);
        llista.add(80);

        llistaobj = new Llista("belal", llista);

        System.out.println("Llista avanç d'ordenar");
        llista.forEach(n -> System.out.println(n));
        System.out.println();
        System.out.println("Llista després d'ordenar");
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            //enviem les dades de la llista sense ordenar
            out.writeObject(llistaobj);

            //rebem les dades de la llista ordenada y sense repetir
            recivedList = (Llista) in.readObject();

            getRequest(recivedList);

            close(socket);
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error de connexió indefinit: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //printem les dades rebudes de la llista
    public void getRequest(Llista serverData) {

        System.out.println("El teu nom ---->" + serverData.getNom());
        System.out.println("Llista ordenada sense repetits ");
        serverData.getNumberList().forEach(llistaOrdenada -> System.out.println(llistaOrdenada));

    }


    private void close(Socket socket) {
        //si falla el tancament no podem fer gaire cosa, només enregistrar
        //el problema
        try {
            //tancament de tots els recursos
            if (socket != null && !socket.isClosed()) {
                if (!socket.isInputShutdown()) {
                    socket.shutdownInput();
                }
                if (!socket.isOutputShutdown()) {
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            //enregistrem l'error amb un objecte Logger
            Logger.getLogger(ClientLlista.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
		/*if (args.length != 2) {
            System.err.println(
                "Usage: java ClientLlista <host name> <port number>");
            System.exit(1);
        }*/

        // String hostName = args[0];
        // int portNumber = Integer.parseInt(args[1]);
        ClientLlista clientTcp = new ClientLlista("localhost", 5558);
        clientTcp.start();
    }
}