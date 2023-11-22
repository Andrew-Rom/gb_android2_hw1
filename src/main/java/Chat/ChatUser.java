package Chat;

public class ChatUser {
    public String userLogin;
    public String userPassword;
    public boolean isConnected = false;

    public ChatUser(String userLogin, String userPassword) {
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }
}
