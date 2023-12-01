package ServerRun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerRun extends JFrame implements Listener{
    private static final int WINDOW_HEIGHT = 400;
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_POSX = 800;
    private static final int WINDOW_POSY = 300;
    JPanel serverPanel = new JPanel();
    JButton btnStart = new JButton("Start Server");
    JButton btnStop = new JButton("Stop Server");
    JTextArea serverStatus = new JTextArea();
    ServerListener server = new Server(this);

    ServerRun() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(WINDOW_POSX, WINDOW_POSY);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Server Controller");
        setResizable(false);

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.serverListener(true);
            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               server.serverListener(false);
            }
        });
        JPanel serverbuttonsPanel = new JPanel(new GridLayout(1, 2));
        serverbuttonsPanel.add(btnStart);
        serverbuttonsPanel.add(btnStop);
        serverPanel.setLayout(new BorderLayout());
        serverPanel.add(serverbuttonsPanel, BorderLayout.NORTH);
        serverPanel.add(serverStatus, BorderLayout.SOUTH);
        add(serverPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ServerRun();
    }

    @Override
    public void messageRes(String text) {
        serverStatus.append(text);
    }
}