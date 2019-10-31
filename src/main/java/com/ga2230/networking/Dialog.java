package com.ga2230.networking;

import java.io.*;
import java.net.Socket;

/**
 * The dialog class is used for the server to communicate with clients and vice-versa.
 */
public class Dialog {

    /**
     * The running boolean is used to flag to the dialog whether it should still run.
     */
    private boolean running = true;

    /**
     * The reader is used to read from the input buffer.
     */
    private BufferedReader reader;

    /**
     * The writer is used to write to the output buffer.
     */
    private BufferedWriter writer;

    /**
     * The connect function assembles a dialog for a client program to use.
     *
     * @param ip        The IP address of the server.
     * @param onReceive The receiver callback.
     * @return The constructed dialog.
     */
    public static Dialog connect(String ip, OnReceive onReceive, OnConnect onConnect) {
        try {
            return new Dialog(new Socket(ip, Server.PORT), onReceive, onConnect);
        } catch (Exception e) {
            System.out.println("Unable to connect to server");
            return null;
        }
    }

    /**
     * This is the main constructor for a dialog.
     *
     * @param socket    The socker.
     * @param onReceive The receiver callback.
     */
    public Dialog(Socket socket, OnReceive onReceive, OnConnect onConnect) {
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
                        // Trigger on connect
                        if (onConnect != null)
                            onConnect.onConnect(this);
                        // Begin listening
                        while (running) {
                            if (reader.ready()) {
                                String received = reader.readLine();
                                try {
                                    onReceive.receive(received, this);
                                } catch (Exception ignored) {
                                }
                            }
                            Thread.sleep(10);
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
                    if (onConnect != null)
                        onConnect.onDisonnect(this);
                }).start();
            } else {
                if (onConnect != null)
                    onConnect.onDisonnect(this);
            }

        }
    }

    /**
     * This function returns whether the dialog is running.
     *
     * @return State
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * This function is used to send data over the socket.
     *
     * @param output Data
     */
    public void send(String output) {
        try {
            writer.write(output);
            writer.write("\n");
            writer.flush();
        } catch (Exception ignored) {
            kill();
        }
    }

    /**
     * This function is used to kill the dialog.
     */
    public void kill() {
        running = false;
    }
}
