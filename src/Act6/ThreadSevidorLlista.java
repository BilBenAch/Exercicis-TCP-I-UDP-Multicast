package Act6;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class ThreadSevidorLlista implements Runnable {
    /* Thread que gestiona la comunicació de SrvTcPAdivina.java i un cllient ClientLlista.java */

    Socket clientSocket = null;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    Llista msgSortint;
    Llista msgEntrant;

    boolean acabat;

    public ThreadSevidorLlista(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        acabat = false;

    }

    @Override
    public void run() {
        try {

            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());

            while (!acabat) {
                //data entrada
                msgEntrant = (Llista) in.readObject();

                //data sortida, ara contendrà les dades de la llista ordenades
                msgSortint = generaResposta(msgEntrant);

                //enviem les dades de la llista ordenada y sense repetir
                out.writeObject(msgSortint);


            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        }
        try {
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //mètode per no colpsar el bucle de execució, tree set perque ordeni y torni no repetits
    public Llista generaResposta(Llista en) {
        Set<Integer> sortedAndDistinct = new TreeSet<>(en.getNumberList());

        //creem una nova llista y pasem els valors del tree set si no no funciona
        List<Integer> arrayList = new ArrayList<>(sortedAndDistinct);

        //objecte ordenat y sense repetir
        Llista ret = new Llista(en.getNom(), arrayList);
        acabat = false;

        return ret;
    }

}
