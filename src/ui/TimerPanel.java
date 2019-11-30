package ui;

import util.Core;
import util.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.Observable;
import java.util.Observer;

import static util.Preferences.TimerMode.COUNTDOWN;

public class TimerPanel extends JPanel implements Observer {

    private GameModel gameModel;
    private Time whiteTime;
    private Time blackTime;

    private JPanel displayPanel;
    private JPanel whiteTimerPanel;
    private JPanel whiteTimerDigitsPanel;
    private JLabel whiteTimerDigitsLabel;
    private JPanel whiteTimerStatusPanel;
    private JPanel blackTimerPanel;
    private JPanel blackTimerDigitsPanel;
    private JLabel blackTimerDigitsLabel;
    private JPanel blackTimerStatusPanel;

    private boolean isCountDownMode = Core.getPreferences().getTimerMode().equals(COUNTDOWN);

    public TimerPanel(GameModel gameModel) {
        super(new BorderLayout());
        this.gameModel = gameModel;

        if (isCountDownMode) {
            Integer minute = Core.getPreferences().getTimeLimit();
            Integer hour = (minute / 60) % 100;  // ignore more than 100
            String timeLimit = hour.toString() + ":" + minute + ":00";
            whiteTime = Time.valueOf(timeLimit);
            blackTime = Time.valueOf(timeLimit);
        } else {
            whiteTime = Time.valueOf("00:00:00");
            blackTime = Time.valueOf("00:00:00");
        }

        initialize();
        gameModel.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public Time whiteTimerTikTok() {
        /*
        TODO-timer
            Update whiteTime
            Update whiteDigitsLabel
            Show whiteTimerStatusPanel
            Blind blackTimerStatusPanel
         */
        whiteTime.setTime(whiteTime.getTime() + (isCountDownMode ? -1000 : 1000));

        whiteTimerDigitsLabel.setText(whiteTime.toString());
        whiteTimerStatusPanel.setVisible(true);
        blackTimerStatusPanel.setVisible(false);

        return whiteTime;
    }

    public Time blackTimerTikTok() {
        // TODO-timer: same with whiteTimerTikTok
        blackTime.setTime(blackTime.getTime() + (isCountDownMode ? -1000 : 1000));

        blackTimerDigitsLabel.setText(blackTime.toString());
        blackTimerStatusPanel.setVisible(true);
        whiteTimerStatusPanel.setVisible(false);

        return blackTime;
    }

    private void initialize() {
        whiteTimerDigitsLabel = new JLabel(whiteTime.toString());
        whiteTimerDigitsLabel.setFont(whiteTimerDigitsLabel.getFont().deriveFont(48f));
        whiteTimerDigitsPanel = new JPanel();
        whiteTimerDigitsPanel.add(whiteTimerDigitsLabel);
        whiteTimerStatusPanel = new JPanel();
        whiteTimerStatusPanel.setBackground(Color.WHITE);
        whiteTimerPanel = new JPanel(new BorderLayout());
        whiteTimerPanel.add(whiteTimerDigitsPanel, BorderLayout.LINE_START);
        whiteTimerPanel.add(whiteTimerStatusPanel, BorderLayout.CENTER);
        whiteTimerPanel.setBorder(BorderFactory.createTitledBorder("White"));

        blackTimerDigitsLabel = new JLabel(blackTime.toString());
        blackTimerDigitsLabel.setFont(blackTimerDigitsLabel.getFont().deriveFont(48f));
        blackTimerDigitsPanel = new JPanel();
        blackTimerDigitsPanel.add(blackTimerDigitsLabel);
        blackTimerStatusPanel = new JPanel();
        blackTimerStatusPanel.setBackground(Color.BLACK);
        blackTimerPanel = new JPanel(new BorderLayout());
        blackTimerPanel.add(blackTimerDigitsPanel, BorderLayout.LINE_START);
        blackTimerPanel.add(blackTimerStatusPanel, BorderLayout.CENTER);
        blackTimerPanel.setBorder(BorderFactory.createTitledBorder("Black"));

        displayPanel = new JPanel(new GridLayout(2, 1));

        if (Core.getPreferences().isBoardReversed()) {
            displayPanel.add(whiteTimerPanel);
            displayPanel.add(blackTimerPanel);
        } else {
            displayPanel.add(whiteTimerPanel);
            displayPanel.add(blackTimerPanel);
        }

        this.add(displayPanel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(300, 200));
    }

}
