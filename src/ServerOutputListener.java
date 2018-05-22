import java.io.BufferedReader;
import java.io.IOException;

public class ServerOutputListener extends  Thread {

    private boolean continueListening = true;
    private BufferedReader in;


    public ServerOutputListener(BufferedReader in) {
        this.in = in;
    }
    public void run() {
        String inputLine;
        while(continueListening) {
            try {
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void disable() {
        continueListening = false;
    }

}
