package ui;

import pieces.Piece;
import util.*;

import javax.swing.*;
import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

public class ControlPanel extends JPanel implements Observer {

    private GameModel gameModel;

    private JButton undoButton;
    private JButton saveButton;
    private JButton loadButton;

    public ControlPanel(GameModel gameModel) {
        this.gameModel = gameModel;
        initialize();
        gameModel.addObserver(this);
    }

    private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setLayout(new GridLayout(0, 1));

        undoButton = new JButton("Request Undo");
        undoButton.setEnabled(false);
        saveButton = new JButton("Save Game");
        saveButton.setEnabled(true);
        saveButton.addActionListener(e -> {
            SaveLoader saveLoader = new SaveLoader();

            try {
                String savedFileName = saveLoader.saveGame(gameModel);
                JOptionPane.showMessageDialog(null, "Successfully saved!\n" + savedFileName, "Save", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException er) {
                JOptionPane.showMessageDialog(null, "File cannot be saved.\nPlease check if directory is right.", "Save", JOptionPane.WARNING_MESSAGE);
            } catch (XMLStreamException er) {
                JOptionPane.showMessageDialog(null, "Something happend. Saving didn't go well", "Save", JOptionPane.WARNING_MESSAGE);
            }
        });

        loadButton = new JButton("Load Game");
        loadButton.setEnabled(true);
        loadButton.addActionListener(e -> {
            SaveLoader saveLoader = new SaveLoader();
            GameStatus gameStatus = saveLoader.loadGame();
            if (gameStatus != null) {
                Core.startGame(gameStatus);
            }
            setVisible(false);
        });

        this.add(undoButton);
        this.add(saveButton);
        this.add(loadButton);
        this.setPreferredSize(new Dimension(300, 200));
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}
