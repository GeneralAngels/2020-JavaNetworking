package com.ga2230.networking;

import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * The server class handles client connections and kill management.
 */
public class Server {
    /**
     * The PORT variable is the selected port for dialogs and for the server.
     */
    public static final int PORT = 2230;

    /**
     * The listening variable is used to indicate to the thread whether it should stop.
     */
    private static boolean listening = true;

    /**
     * The server object is used to accept clients, and is initialized with PORT.
     */
    private static ServerSocket server = null;

    /**
     * The dialog array is used to keep track of all dialogs.
     */
    private static ArrayList<Dialog> dialogs = null;

    /**
     * The begin function initializes dialogs, server and begins listening for clients.
     *
     * @param onReceive Action to be ran when new input is received.
     */
    public static void begin(OnReceive onReceive) {
        dialogs = new ArrayList<>();
        try {
            server = new ServerSocket(PORT);
            // Listen
            new Thread(() -> {
                while (listening) {
                    try {
                        dialogs.add(new Dialog(server.accept(), onReceive, null));
                    } catch (Exception e) {
                        System.out.println("Unable to initialize dialog - " + e.getMessage());
                    }
                }
            }).start();
        } catch (Exception e) {
            System.out.println("Oh No! - No Comm - " + e.getMessage());
        }
    }

    /**
     * The send function is used to send global data to the dialogs, like a broadcast.
     *
     * @param data Data to send.
     */
    public static void send(String data) {
        for (Dialog dialog : dialogs) {
            if (dialog.isRunning())
                dialog.send(data);
            else
                dialogs.remove(dialog);
        }
    }

    /**
     * The kill function is used to stop all dialogs.
     */
    public static void kill() {
        for (Dialog dialog : dialogs)
            dialog.kill();
        dialogs.clear();
    }
}
