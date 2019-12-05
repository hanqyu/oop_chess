package util;

import board.Board;
import pieces.Piece;
import ui.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.Observable;

public class GameModel extends Observable {

    private GameFrame gameFrame;
    private BoardPanel boardPanel;
    private TimerPanel timerPanel;
    private ControlPanel controlPanel;
    private MoveHistoryPanel moveHistoryPanel;
    private GameStatus gameStatus;

    private Timer whiteTimer;
    private Timer blackTimer;

    public GameModel() {
        initialize();
    }

    public GameModel(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        Board.initialize(gameStatus);
        MoveValidator.initialize(gameStatus);
        initialize();
    }

    private void initialize() {
        initializeTimers();
        initializeUIComponents();
    }

    public void onMoveRequest(char originFile, int originRank, char destinationFile, int destinationRank) {
        onLocalMoveRequest(originFile, originRank, destinationFile, destinationRank);
    }

    private void onLocalMoveRequest(char originFile, int originRank, char destinationFile, int destinationRank) {
        Move move = new Move(originFile, originRank, destinationFile, destinationRank);
        Move castlingKing = new Move(Board.getSquare(originFile, originRank).getCurrentPiece(), originFile, originRank, destinationFile, destinationRank);
        Move castlingRook = null;
        if (MoveValidator.validateMove(move)) {
            if (MoveValidator.isValidCastling(castlingKing)) {
                if (castlingKing.getDestinationFile() == 'c' && castlingKing.getDestinationRank() == 1) {
                    castlingRook = new Move('a', 1, 'd', 1);
                }
                if (castlingKing.getDestinationFile() == 'c' && castlingKing.getDestinationRank() == 8) {
                    castlingRook = new Move('a', 8, 'd', 8);
                }
                if (castlingKing.getDestinationFile() == 'g' && castlingKing.getDestinationRank() == 1) {
                    castlingRook = new Move('h', 1, 'f', 1);
                }
                if (castlingKing.getDestinationFile() == 'g' && castlingKing.getDestinationRank() == 8) {
                    castlingRook = new Move('h', 8, 'f', 8);
                }
                executeCastling(castlingRook);
                castlingKing = null;
            }
            executeMove(move);
            Board.getSquare(move.getDestinationFile(), move.getDestinationRank()).getCurrentPiece().setEverMoved();
        } else {
            //
        }
    }

    private void executeMove(Move move) {
        MoveLogger.addMove(move);
        Board.executeMove(move);
        if (move.getPiece().hasSpecialMove()) {
            move.evokedSpecialMove();
        }
        moveHistoryPanel.printMove(move);
        boardPanel.executeMove(move);
        switchTimer(move);
        if (MoveValidator.isCheckMove(move)) {
            if (MoveValidator.isCheckMate(move)) {
                stopTimer();
                gameFrame.showCheckmateDialog();
            } else {
                gameFrame.showCheckDialog();
            }
        }
    }

    private void executeCastling(Move move) {
        Board.executeMove(move);
        boardPanel.executeMove(move);
        if (MoveValidator.isCheckMove(move)) {
            if (MoveValidator.isCheckMate(move)) {
                stopTimer();
                gameFrame.showCheckmateDialog();
            } else {
                gameFrame.showCheckDialog();
            }
        }
    }

    public Piece queryPiece(char file, int rank) {
        return Board.getSquare(file, rank).getCurrentPiece();
    }

    private void initializeUIComponents() {
        boardPanel = new BoardPanel(this, gameStatus);
        timerPanel = new TimerPanel(this, gameStatus);
        controlPanel = new ControlPanel(this);
        moveHistoryPanel = new MoveHistoryPanel(this);
        gameFrame = new GameFrame(this);
    }

    private void initializeTimers() {
        whiteTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Time time = timerPanel.whiteTimerTikTok();

                if (Integer.parseInt(time.toString().replace(":","")) <= 0) {
                    stopTimer();
                    gameFrame.showTimeOutDialog("White");
                    boardPanel.setEnabled(false);
                }
            }
        });
        blackTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Time time = timerPanel.blackTimerTikTok();
                if (Integer.parseInt(time.toString().replace(":","")) <= 0) {
                    stopTimer();
                    gameFrame.showTimeOutDialog("Black");
                    boardPanel.setEnabled(false);
                }
            }
        });
    }

    private void switchTimer(Move move) {
        /*
        TODO-timer
            start and stop whiteTimer and blackTimer
         */

        if (move.getPiece().getColor().equals(Piece.Color.WHITE)) {
            whiteTimer.stop();
            blackTimer.start();
        } else {
            whiteTimer.start();
            blackTimer.stop();
        }
    }

    private void stopTimer() {
        // TODO-timer: stop timers
        whiteTimer.stop();
        blackTimer.stop();
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public TimerPanel getTimerPanel() {
        return timerPanel;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public MoveHistoryPanel getMoveHistoryPanel() {
        return moveHistoryPanel;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public GameFrame getGameFrame() { return gameFrame; }
}
