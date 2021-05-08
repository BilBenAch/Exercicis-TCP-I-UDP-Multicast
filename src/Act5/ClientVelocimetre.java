package Act5;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class ClientVelocimetre {
    private int portDesti;
    private String ipSrv;
    private int mitjana;
    private int aux;
    private int contador;
    private int dades;

    private InetAddress adrecaDesti;

    private MulticastSocket multisocket;
    private InetAddress multicastIP;
    boolean continueRunning = true;

    InetSocketAddress groupMulticast;
    NetworkInterface netIf;

    public ClientVelocimetre(String ip, int port) {
        this.portDesti = port;
        ipSrv = ip;

        try {
            multisocket = new MulticastSocket(5557);
            multicastIP = InetAddress.getByName("224.0.0.70");
            groupMulticast = new InetSocketAddress(multicastIP, 5557);
            netIf = NetworkInterface.getByName("wlp0s20f3");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            adrecaDesti = InetAddress.getByName(ipSrv);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void runClient() throws IOException {
        byte[] receivedData = new byte[1024];

        DatagramPacket packet;
        DatagramSocket datagramSocket = new DatagramSocket();
        multisocket.joinGroup(groupMulticast, netIf);

        while (continueRunning) {

            packet = new DatagramPacket(receivedData, 1024);
            datagramSocket.setSoTimeout(5000);

            try {
                //processament de les dades rebudes i obtenció de la resposta
                getData(packet.getData(), packet.getLength());

                multisocket.receive(packet);
            } catch (SocketTimeoutException e) {
                System.out.println("El servidor no respòn: " + e.getMessage());
                continueRunning = false;

            }

        }

        multisocket.leaveGroup(groupMulticast, netIf);


    }

    public void getData(byte[] data, int length) {
        dades = ByteBuffer.wrap(data).getInt();
        aux =+dades;
        contador++;
        //contamos dígitos
        //si vale 5 hacemos la media y la mostramos
        if (contador == 5) {
            mitjana = aux / 5;
            //mostramos la media
            System.out.println(mitjana);
            aux = 0;
            contador = 0;
        }
    }


    public static void main(String[] args) {
        try {
            ClientVelocimetre clientVelocimetre = new ClientVelocimetre("224.0.0.70", 5557);
            clientVelocimetre.runClient();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}
