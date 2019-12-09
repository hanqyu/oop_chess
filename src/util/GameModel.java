package util;

import board.Board;
import pieces.*;
import ui.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class GameModel extends Observable {

    private GameFrame gameFrame;
    private BoardPanel boardPanel;
    private TimerPanel timerPanel;
    private ControlPanel controlPanel;
    private MoveHistoryPanel moveHistoryPanel;
    private GameStatus gameStatus;
    private static int undoListIndex = -1;
    private static List isCaptureOrCastlingMove = new ArrayList();
    private static List executeMoveReverseList = new ArrayList();
    private static List capturedOrCastlingPieceList = new ArrayList();

    private Timer whiteTimer;
    private Timer blackTimer;

    public GameModel() {
        initialize();
    }

    public GameModel(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        Board.initialize(gameStatus);
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

            moveStatusStacking(move);

            if (MoveValidator.isEnPassantPossible(move)) {
                Move prepareEnPassant = new Move(Board.getSquare(move.getDestinationFile(), move.getDestinationRank() - move.getPiece().getRankDifferenceForPawn()).getCurrentPiece(),
                        move.getDestinationFile(), move.getDestinationRank() - move.getPiece().getRankDifferenceForPawn(), move.getDestinationFile(), move.getDestinationRank());
                executeEnPassant(prepareEnPassant);
            }

            executeMove(move);

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
            }
            if (MoveValidator.isPromotionPossible(move)) {
                PromotionFrame promotionFrame = new PromotionFrame(destinationFile, destinationRank, boardPanel,
                        Board.getSquare(destinationFile, destinationRank).getCurrentPiece().getColor());
            }

            Board.getSquare(move.getDestinationFile(), move.getDestinationRank()).getCurrentPiece().plusMovingTimes();
            Board.getSquare(move.getDestinationFile(), move.getDestinationRank()).getCurrentPiece().setEverMoved();
        } else {
            //
        }
    }


    public void executePromotion(char promotionFile, int promotionRank, Piece.Type type, Piece.Color color) {
        ArrayList<Piece> promotionPieceSet = new ArrayList<>();
        boardPanel.getSquarePanel(promotionFile, promotionRank).removeAll();
        Board.getSquare(promotionFile, promotionRank).setCurrentPiece(null);
        promotionPieceSet = promotionPieceSetMaker(type, color);
        Iterator<Piece> promotionPieceIterator = promotionPieceSet.iterator();
        boardPanel.getSquarePanel(promotionFile, promotionRank).repaint();
        boardPanel.getSquarePanel(promotionFile, promotionRank).add(boardPanel.getPieceImage(promotionPieceIterator.next()));
        Board.getSquare(promotionFile, promotionRank).setCurrentPiece(promotionPieceIterator.next());
    }

    private ArrayList<Piece> promotionPieceSetMaker(Piece.Type type, Piece.Color color) {
        ArrayList<Piece> promotionPieceSet = new ArrayList<>();
        if (type.equals(Piece.Type.QUEEN) && color.equals(Piece.Color.WHITE)) {
            promotionPieceSet.add(new Queen(Piece.Color.WHITE));
            promotionPieceSet.add(new Queen(Piece.Color.WHITE));
        }
        if (type.equals(Piece.Type.ROOK) && color.equals(Piece.Color.WHITE)) {
            promotionPieceSet.add(new Rook(Piece.Color.WHITE));
            promotionPieceSet.add(new Rook(Piece.Color.WHITE));
        }
        if (type.equals(Piece.Type.BISHOP) && color.equals(Piece.Color.WHITE)) {
            promotionPieceSet.add(new Bishop(Piece.Color.WHITE));
            promotionPieceSet.add(new Bishop(Piece.Color.WHITE));
        }
        if (type.equals(Piece.Type.KNIGHT) && color.equals(Piece.Color.WHITE)) {
            promotionPieceSet.add(new Knight(Piece.Color.WHITE));
            promotionPieceSet.add(new Knight(Piece.Color.WHITE));
        }
        if (type.equals(Piece.Type.PAWN) && color.equals(Piece.Color.WHITE)) {
            promotionPieceSet.add(new Knight(Piece.Color.WHITE));
            promotionPieceSet.add(new Knight(Piece.Color.WHITE));
        }
        if (type.equals(Piece.Type.QUEEN) && color.equals(Piece.Color.BLACK)) {
            promotionPieceSet.add(new Queen(Piece.Color.BLACK));
            promotionPieceSet.add(new Queen(Piece.Color.BLACK));
        }
        if (type.equals(Piece.Type.ROOK) && color.equals(Piece.Color.BLACK)) {
            promotionPieceSet.add(new Rook(Piece.Color.BLACK));
            promotionPieceSet.add(new Rook(Piece.Color.BLACK));
        }
        if (type.equals(Piece.Type.BISHOP) && color.equals(Piece.Color.BLACK)) {
            promotionPieceSet.add(new Bishop(Piece.Color.BLACK));
            promotionPieceSet.add(new Bishop(Piece.Color.BLACK));
        }
        if (type.equals(Piece.Type.KNIGHT) && color.equals(Piece.Color.BLACK)) {
            promotionPieceSet.add(new Knight(Piece.Color.BLACK));
            promotionPieceSet.add(new Knight(Piece.Color.BLACK));
        }
        if (type.equals(Piece.Type.PAWN) && color.equals(Piece.Color.BLACK)) {
            promotionPieceSet.add(new Knight(Piece.Color.BLACK));
            promotionPieceSet.add(new Knight(Piece.Color.BLACK));
        }
        return promotionPieceSet;
    }

    private void moveStatusStacking(Move move) {
        Move reversedMove = new Move(move.getPiece(), move.getDestinationFile(), move.getDestinationRank(), move.getOriginFile(), move.getOriginRank());
        executeMoveReverseList.add(reversedMove);
        undoListIndex++;
        if (Board.getSquare(move.getDestinationFile(), move.getDestinationRank()).getCurrentPiece() == null
                && !MoveValidator.isValidCastling(move)) {
            isCaptureOrCastlingMove.add("JustMove");
        }
        if (MoveValidator.isPromotionPossible(move) && Board.getSquare(move.getDestinationFile(), move.getDestinationRank()).getCurrentPiece() == null) {
            isCaptureOrCastlingMove.add("Promotion");
        }
        if (MoveValidator.isPromotionPossible(move) && Board.getSquare(move.getDestinationFile(), move.getDestinationRank()).getCurrentPiece() != null) {
            isCaptureOrCastlingMove.add("PromotionWithCapturing");
            List capturedPieceInfo = new ArrayList();
            Piece capturedPiece = Board.getSquare(move.getDestinationFile(), move.getDestinationRank()).getCurrentPiece();
            capturedPieceInfo.add(capturedPiece);
            capturedPieceInfo.add(move.getDestinationFile());
            capturedPieceInfo.add(move.getDestinationRank());
            capturedOrCastlingPieceList.add(capturedPieceInfo);
        }
        if (Board.getSquare(move.getDestinationFile(), move.getDestinationRank()).getCurrentPiece() != null) {
            isCaptureOrCastlingMove.add("CapturingMove");
            List capturedPieceInfo = new ArrayList();
            Piece capturedPiece = Board.getSquare(move.getDestinationFile(), move.getDestinationRank()).getCurrentPiece();
            capturedPieceInfo.add(capturedPiece);
            capturedPieceInfo.add(move.getDestinationFile());
            capturedPieceInfo.add(move.getDestinationRank());
            capturedOrCastlingPieceList.add(capturedPieceInfo);
        }
        if (MoveValidator.isValidCastling(move)) {
            isCaptureOrCastlingMove.add("Castling");
        }
        if (MoveValidator.isEnPassantPossible(move)) {
            isCaptureOrCastlingMove.add("EnPassant");
            List capturedPieceInfo = new ArrayList();
            Piece enPassantedPawn = Board.getSquare(move.getDestinationFile(), move.getDestinationRank() - move.getPiece().getRankDifferenceForPawn()).getCurrentPiece();
            capturedPieceInfo.add(enPassantedPawn);
            capturedPieceInfo.add(move.getDestinationFile());
            capturedPieceInfo.add(move.getDestinationRank() - move.getPiece().getRankDifferenceForPawn());
            capturedOrCastlingPieceList.add(capturedPieceInfo);
            System.out.println(capturedPieceInfo);
        }
    }

    private void executeMove(Move move) {
        MoveLogger.addMove(move);
        Board.executeMove(move);
        MoveValidator.setEnPassantTiming(false);
        if (move.getPiece().getType().equals(Piece.Type.PAWN)
                && (move.getDestinationRank() - move.getOriginRank() == move.getPiece().getRankDifferenceForPawn() * 2)) {
            MoveValidator.setEnPassantTiming(true);
        }
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
        if (MoveValidator.isStalemateMove(move)) {
            stopTimer();
            gameFrame.showStalemateDialog();
        }
    }

    public void executeUndo() {
        Move undoMove = (Move) executeMoveReverseList.get(undoListIndex);
        String capturingOrCastling = (String) isCaptureOrCastlingMove.get(undoListIndex);
        undoMove.getPiece().minusMovingTimes();

        executeMoveReverseList.remove(undoListIndex);
        isCaptureOrCastlingMove.remove(undoListIndex);

        Board.executeMove(undoMove);
        boardPanel.executeMove(undoMove);
        switch (capturingOrCastling) {
            case "JustMove":
                break;

            case "CapturingMove":

            case "EnPassant":
                revivePiece();
                MoveValidator.setEnPassantTiming(true);
                break;

            case "Castling":
                int rookIndex = capturedOrCastlingPieceList.size();
                Move castlingUndoMove = (Move) capturedOrCastlingPieceList.get(rookIndex - 1);
                capturedOrCastlingPieceList.remove(rookIndex - 1);
                Board.executeMove(castlingUndoMove);
                boardPanel.executeMove(castlingUndoMove);
                undoMove.getPiece().setEverMoved();
                break;

            case "Promotion":
                executePromotion(undoMove.getDestinationFile(), undoMove.getDestinationRank(), Piece.Type.PAWN, undoMove.getPiece().getColor());
                break;

            case "PromotionWithCapturing":
                break;
        }

        undoListIndex -= 1;
        switchTimerUndo(undoMove);
        MoveValidator.switchCurrentColor();
    }

    private void revivePiece() {
        int index = capturedOrCastlingPieceList.size();
        List capturedPieceInfo = (ArrayList) capturedOrCastlingPieceList.get(index - 1);
        capturedOrCastlingPieceList.remove(index - 1);

        Piece capturedPiece = (Piece) capturedPieceInfo.get(0);
        char capturedFile = (char) capturedPieceInfo.get(1);
        int capturedRank = (int) capturedPieceInfo.get(2);

        boardPanel.getSquarePanel(capturedFile, capturedRank).removeAll();
        Board.getSquare(capturedFile, capturedRank).setCurrentPiece(null);
        boardPanel.getSquarePanel(capturedFile, capturedRank).repaint();
        boardPanel.getSquarePanel(capturedFile, capturedRank).add(boardPanel.getPieceImage(capturedPiece));
        Board.getSquare(capturedFile, capturedRank).setCurrentPiece(capturedPiece);
    }

    private void executeCastling(Move move) {
        Move undoCastlingRook = new Move(move.getPiece(), move.getDestinationFile(), move.getDestinationRank(), move.getOriginFile(), move.getOriginRank());
        capturedOrCastlingPieceList.add(undoCastlingRook);
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

    private void executeEnPassant(Move move) {
        Board.executeMove(move);
        boardPanel.executeMove(move);
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

                if (Integer.parseInt(time.toString().replace(":", "")) <= 0) {
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
                if (Integer.parseInt(time.toString().replace(":", "")) <= 0) {
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

    private void switchTimerUndo(Move move) {
        /*
        TODO-timer
            start and stop whiteTimer and blackTimer
         */

        if (move.getPiece().getColor().equals(Piece.Color.BLACK)) {
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
