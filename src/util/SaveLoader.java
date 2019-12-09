package util;

import board.Board;
import pieces.Piece;

import javax.swing.*;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SaveLoader {
    private GameStatus gameStatus;

    public SaveLoader() {
        this.gameStatus = new GameStatus();
    }

    private void makeGameStatusInGame(GameModel gameModel) {
        gameStatus.setTurn(MoveValidator.getCurrentMoveColor().toString());

        gameStatus.setGameMode(Core.getPreferences().getGameMode());
        gameStatus.setNetworkMode(Core.getPreferences().getNetworkMode());
        gameStatus.setTimerMode(Core.getPreferences().getTimerMode());
        gameStatus.setTimeLimit(Core.getPreferences().getTimeLimit());
        gameStatus.setUsingCustomPieces(Core.getPreferences().isUsingCustomPieces());
        gameStatus.setBoardReversed(Core.getPreferences().isBoardReversed());
        gameStatus.setHostIP(Core.getPreferences().getHostIP());
        gameStatus.setPort(Core.getPreferences().getPort());
        gameStatus.setPlayerName(Core.getPreferences().getPlayerName());

        String whiteTime = gameModel.getTimerPanel().getWhiteTime().toString();
        String blackTime = gameModel.getTimerPanel().getBlackTime().toString();

        if (gameStatus.isBoardReversed()) {
            gameStatus.addTime("WHITE", whiteTime);
            gameStatus.addTime("BLACK", blackTime);
        } else {
            gameStatus.addTime("BLACK", blackTime);
            gameStatus.addTime("WHITE", whiteTime);
        }

        char[] files = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int[] ranks = {1, 2, 3, 4, 5, 6, 7, 8};

        for (char file : files) {
            for (int rank : ranks) {
                Piece piece = Board.getSquare(file, rank).getCurrentPiece();
                if (piece == null)
                    continue;

                String color = piece.getColor().toString();
                String type = piece.getType().toString();
                int doubleMoveRank = piece.getDoubleMoveRank();
                int rankDifferenceForPawn = piece.getRankDifferenceForPawn();
                int enPassantRank = piece.getEnPassantRank();
                int enPassantedRank = piece.getEnPassantedRank();
                int movingTimes = piece.getMovingTimes();

                gameStatus.addPiece(color, type, rank, String.valueOf(file), doubleMoveRank, rankDifferenceForPawn, enPassantRank, enPassantedRank, movingTimes);
            }
        }
    }

    public String saveGame(GameModel gameModel) throws FileNotFoundException, XMLStreamException {
        makeGameStatusInGame(gameModel);

        XMLParser xmlParser = new XMLParser();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        String userDir = System.getProperty("user.home");
        String fileName = userDir + "/Desktop/" + "saved_file_" + formatter.format(date) + ".xml";

        xmlParser.writeFile(gameStatus, fileName);
        return fileName;
    }

    public GameStatus loadGame() {
        String userDir = System.getProperty("user.home");
        JFileChooser loadGameFileChooser = new JFileChooser(userDir + "/Desktop");

        int returnVal = loadGameFileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = loadGameFileChooser.getSelectedFile();
            XMLParser xmlParser = new XMLParser();
            return xmlParser.readFile(file);
        } else {
            return null;
        }
    }
}
