package com.ga2230.networking;

import com.ga2230.networking.Dialog;
import com.ga2230.networking.OnReceive;

import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    public static final int PORT = 2230;

    private static boolean listening = true;
    private static ServerSocket server = null;
    private static ArrayList<Dialog> dialogs = null;

    public static void begin(OnReceive onReceive) {
        dialogs = new ArrayList<>();
        try {
            server = new ServerSocket(PORT);
            // Listen
            new Thread(() -> {
                while (listening) {
                    try {
                        dialogs.add(new Dialog(server.accept(), onReceive));
                    } catch (Exception e) {
                        System.out.println("Unable to initialize dialog - " + e.getMessage());
                    }
                }
            }).start();
        } catch (Exception e) {
            System.out.println("Oh No! - No Comm - " + e.getMessage());
        }
    }

    public static void send(String data) {
        for (Dialog dialog : dialogs) dialog.send(data);
    }

    public static void kill(){
        for (Dialog dialog : dialogs) dialog.kill();
    }
}
