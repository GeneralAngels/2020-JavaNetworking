package com.ga2230.networking;

import java.io.*;
import java.net.Socket;

import static com.ga2230.networking.Server.PORT;

public class Dialog {

    private boolean running = true;

    private BufferedReader reader;
    private BufferedWriter writer;

    public static Dialog connect(String ip, OnReceive onReceive) {
        try {
            return new Dialog(new Socket(ip, PORT), onReceive);
        } catch (Exception e) {
            System.out.println("Unable to connect to server");
            return null;
        }
    }

    public Dialog(Socket socket, OnReceive onReceive) {
        // Setup I/O
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            reader = null;
            writer = null;
            System.out.println("Unable to connect to client");
        } finally {
            if (reader != null && writer != null) {
                new Thread(() -> {
                    try {
                        // Begin listening
                        while (running) {
                            try {
                                if (reader.ready()) {
                                    onReceive.receive(reader.readLine(),this);
                                }
                            } catch (IOException ignored) {
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Unrecoverable exception: " + e.toString());
                    }
                    try {
                        socket.close();
                        System.out.println("Client freed");
                    } catch (Exception e) {
                        System.out.println("Failed freeing client");
                    }
                }).start();
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void send(String output) {
        try {
            writer.write(output);
            writer.write("\n");
            writer.flush();
        } catch (Exception ignored) {
        }
    }

    public void kill() {
        running = false;
    }
}
