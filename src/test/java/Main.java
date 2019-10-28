import com.ga2230.networking.Dialog;
import com.ga2230.networking.Server;

public class Main {
    public static void main(String[] args) {
        Server.begin((data, dialog) -> {
            System.out.println("Server - Received \"" + data + "\", echoing back");
            dialog.send(data);
        });
        Dialog.connect("127.0.0.1", (data, dialog) -> {
            System.out.println("Client - received \"" + data + "\"");
        });
    }
}
