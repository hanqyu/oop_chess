package ui;

import util.GameModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UndoListener implements ActionListener {

    GameModel gameModel;

    public UndoListener(GameModel gameModel){
        this.gameModel = gameModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameModel.executeUndo();
    }
}
