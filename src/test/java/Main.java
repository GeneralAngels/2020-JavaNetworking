import com.ga2230.networking.Client;
import com.ga2230.networking.Node;
import com.ga2230.networking.Server;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        Server.begin(new MyNode());
        try {
            Socket socket = new Socket("127.0.0.1", 2230);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write("master log Hi\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class MyNode extends Node {

        protected MyNode() {
            super("mynode");
            addCommand("log", new Command() {
                @Override
                public String execute(String parameter) throws Exception {
                    System.out.println("Log: " + parameter);
                    return "Logged";
                }
            });
        }
    }
}
