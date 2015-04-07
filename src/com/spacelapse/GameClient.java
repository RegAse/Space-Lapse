package com.spacelapse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameClient extends Thread{

    public static DataOutputStream send_to_server;
    public static DataInputStream receive_from_server;

    @Override
    public void run()
    {
        System.out.println("Receive from server constantly");
        while(true)
        {
            try
            {
                String srvmessage = receive_from_server.readUTF();

                // Ignore heartbeat messages let TCP take care of it
                if (!srvmessage.equals("Heartbeat"))
                {
                    ProcessServerData(srvmessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void JoinGame(String ip, int port)
    {
        Socket consock;
        try {
            consock = new Socket(ip, port);

            send_to_server = new DataOutputStream(consock.getOutputStream());
            receive_from_server = new DataInputStream(consock.getInputStream());

            // Send hello message
            send_to_server.writeUTF("I Successfuly joined this game.");

            Thread listen = new Thread(this);
            listen.start();

        } catch (IOException e) {
            System.out.println("Server not found.");
        }
    }

    /**
     * Process Data from server
     * */
    public void ProcessServerData(String data)
    {
        System.out.println("Server data: " + data);
    }
}
