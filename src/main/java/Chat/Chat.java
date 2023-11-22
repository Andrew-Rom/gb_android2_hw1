package Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Chat extends JFrame {

    ArrayList<ChatUser> chatUsers = new ArrayList<>();
    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_WIDTH = 800;

    JPanel mainPanel = new JPanel();

    JPanel commonPanel = new JPanel();

    JPanel connectionPanel = new JPanel();
    JPanel connectionInfoPanel = new JPanel();
    JLabel loginLabel = new JLabel("Login");
    JTextField loginField = new JTextField();
    JLabel passwordLabel = new JLabel("Password");
    JTextField passwordField = new JPasswordField();
    JLabel serverIpLabel = new JLabel("Server IP address");
    JTextField serverIpField = new JTextField();
    JLabel serverPortLabel = new JLabel("Server port");
    JTextField serverPortField = new JTextField();
    JButton btnConnect = new JButton("Connect");

    JPanel messagePanel = new JPanel();
    JTextField messageField = new JTextField();
    JButton btnSend = new JButton("SEND");

    JTextArea logInfo = new JTextArea();

    JPanel userListPanel = new JPanel();
    JLabel userListLabel = new JLabel("User list");
    JList<String> userList = new JList<>();

    String serverIp;
    int serverPort;
    String message;

    Chat() {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("ChatWindow");
        setResizable(false);

        connectionInfoPanel.setLayout(new GridLayout(4, 2));
        connectionInfoPanel.add(loginLabel);
        connectionInfoPanel.add(loginField);
        connectionInfoPanel.add(passwordLabel);
        connectionInfoPanel.add(passwordField);
        connectionInfoPanel.add(serverIpLabel);
        connectionInfoPanel.add(serverIpField);
        connectionInfoPanel.add(serverPortLabel);
        connectionInfoPanel.add(serverPortField);

        connectionPanel.setLayout(new BorderLayout());
        connectionPanel.add(connectionInfoPanel);
        connectionPanel.add(btnConnect, BorderLayout.EAST);
        btnConnect.setPreferredSize(new Dimension(WINDOW_WIDTH / 5, connectionPanel.getHeight()));

        messagePanel.setLayout(new BorderLayout());
        messagePanel.add(messageField);
        messagePanel.add(btnSend, BorderLayout.EAST);

        commonPanel.setLayout(new BorderLayout());
        logInfo.setBackground(Color.LIGHT_GRAY);
        commonPanel.add(connectionPanel, BorderLayout.NORTH);
        commonPanel.add(new JScrollPane(logInfo));
        commonPanel.add(messagePanel, BorderLayout.SOUTH);

        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = new Date();
                ChatUser user;
                if (loginField.getText().length() > 0
                        && passwordField.getText().length() > 0
                        && userList.getSelectedIndex() < 0) {
                    user = auth(loginField.getText(), passwordField.getText());
                    if (!user.isConnected) {
                        user.isConnected = true;
                        chatUsers.add(user);
                        message = date + "\t" +
                                loginField.getText() + " connected\n\n";

                        String[] userLst = chatUsers.stream()
                                .map(chatUser -> chatUser.userLogin)
                                .toList().toArray(new String[0]);
                        userList.setListData(userLst);
                        readChatLog();
                        logInfo.append(message);
                        System.out.print(message);
                        try {
                            saveChatLog(message);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                if (userList.getSelectedIndex() >= 0) {
                    user = getCurrentUser(userList.getSelectedValue());
                    if (!(user == null)) {
                        chatUsers.remove(user);
                        message = date + "\t" + userList.getSelectedValue() + " disconnected\n\n";
                        String[] userLst = chatUsers.stream()
                                .map(chatUser -> chatUser.userLogin)
                                .toList().toArray(new String[0]);
                        userList.setListData(userLst);
                        logInfo.append(message);
                        System.out.print(message);
                        try {
                            saveChatLog(message);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                btnConnect.setText("Connect");
            }
        });

        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    btnConnect.setText("Disconnect");
                }
            }
        });

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = new Date();
                if (loginField.getText().length() > 0 && passwordField.getText().length() > 0) {
                    ChatUser user = auth(loginField.getText(), passwordField.getText());
                    if (user.isConnected) {
                        message = date.toString() + "\t" +
                                loginField.getText() + " > " +
                                messageField.getText() + "\n\n";
                        logInfo.append(message);
                        System.out.print(message);
                        try {
                            saveChatLog(message);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });

        userListPanel.setLayout(new BorderLayout());
        userListPanel.add(userListLabel, BorderLayout.NORTH);
        userList.setPreferredSize(new Dimension(WINDOW_WIDTH / 4, WINDOW_HEIGHT));
        userList.setBackground(Color.BLACK);
        userList.setForeground(Color.LIGHT_GRAY);
        userListPanel.add(userList);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(commonPanel);
        mainPanel.add(userListPanel, BorderLayout.EAST);
        add(mainPanel);

        if (loginField.isCursorSet()) {
            ChatUser checkUser = auth(loginField.getText(), passwordField.getText());
            if (checkUser.isConnected) {
                btnConnect.setText("Disconnect");
            } else {
                btnConnect.setText("Connect");
            }
        }

        setVisible(true);
        setLocationRelativeTo(null);
    }

    private ChatUser auth(String login, String password) {
        for (ChatUser user : chatUsers) {
            if (user.userLogin.equals(login) && user.userPassword.equals(password)) {
                return user;
            }
        }
        return new ChatUser(login, password);
    }

    private ChatUser getCurrentUser(String login) {
        for (ChatUser user : chatUsers) {
            if (user.userLogin.equals(login)) {
                return user;
            }
        }
        return null;
    }

    private void saveChatLog(String text) throws IOException {
        try (FileWriter writer = new FileWriter("chat.log", true)) {
            writer.write(text);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void readChatLog() {
        try {
            Scanner scanner = new Scanner(new File("chat.log"));
            while (scanner.hasNextLine()) {
                logInfo.append(scanner.nextLine() + "\n");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new Chat();
    }
}
