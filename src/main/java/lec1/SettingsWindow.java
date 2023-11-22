package lec1;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 230;
    private static final int WINDOW_WIDTH = 350;

    JLabel gameModeLabel = new JLabel("Game mode");
    JRadioButton gameHumans = new JRadioButton("Human vs. Human");
    JRadioButton gameAi = new JRadioButton("Human vs. AI");
    ButtonGroup gameModeButtons = new ButtonGroup();

    JLabel fieldSizeLabel = new JLabel("Select game field size");
    JSlider fieldSize = new JSlider(3, 10, 3);

    JLabel winSizeLabel = new JLabel("Select victory criteria");
    JSlider winSize = new JSlider(3, 10, 3);

    JButton btnStart = new JButton("Start new game");

    SettingsWindow(GameWindow gameWindow) {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);

        gameModeButtons.add(gameHumans);
        gameModeButtons.add(gameAi);
        gameAi.setSelected(true);

        JPanel gameModePanel = new JPanel(new GridLayout(3, 1));
        gameModePanel.add(" ", gameModeLabel);
        gameModePanel.add(gameHumans);
        gameModePanel.add(gameAi);

        JPanel gameFieldPanel = new JPanel(new GridLayout(4, 1));
        gameFieldPanel.add(fieldSizeLabel);
        gameFieldPanel.add(fieldSize);
        gameFieldPanel.add(winSizeLabel);
        gameFieldPanel.add(winSize);

        JPanel startButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startButtonPanel.add(btnStart);

        Container settingsPanel = getContentPane();
        settingsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        settingsPanel.add(gameModePanel);
        settingsPanel.add(gameFieldPanel);
        settingsPanel.add(startButtonPanel);

        fieldSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                fieldSizeLabel.setText("Game field size: " + fieldSize.getValue());
            }
        });

        winSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                winSize.setValue(Math.min(winSize.getValue(), fieldSize.getValue()));
                winSizeLabel.setText("Dots for win: " + winSize.getValue());
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameWindow.startNewGame(
                        gameAi.isSelected(),
                        fieldSize.getValue(),
                        fieldSize.getValue(),
                        winSize.getValue());
                setVisible(false);
            }
        });
        setLocationRelativeTo(gameWindow);
    }
}
