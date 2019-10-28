import com.ga2230.networking.Server;

public class Main {
    public static void main(String[] args){
        Server.begin((data, dialog) -> {
            System.out.println("Received \""+data+"\", echoing back");
            dialog.send(data);
        });
    }
}
