package ServerRun;

public class Server implements ServerListener{
    boolean isServerWorking;
    private Listener listener;

    public Server(Listener listener) {
        this.listener = listener;
        this.isServerWorking = false;
    }

    public void start() {
        if (!isServerWorking) {
            isServerWorking = true;
            listener.messageRes("Server started\n");
        } else {
            listener.messageRes("Server has been already started\n");
        }

    }

    public void stop() {
        if (isServerWorking) {
            isServerWorking = false;
            listener.messageRes("Server stopped\n");
        } else {
            listener.messageRes("Server has been already stopped\n");
        }

    }


    @Override
    public void serverListener(boolean status) {

        if (status){
            stop();
        } else {
            start();
        }

    }
}
