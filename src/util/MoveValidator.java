package util;

import board.Board;
import pieces.Piece;
import pieces.PieceSet;

import java.util.List;

public class MoveValidator {

    private static MoveValidator ourInstance = new MoveValidator();

    public static MoveValidator getInstance() {
        return ourInstance;
    }

    private MoveValidator() {
        currentMoveColor = Piece.Color.WHITE;
    }

    private static Piece.Color currentMoveColor;

    public static boolean validateMove(Move move) {
        return validateMove(move, false);
    }

    public static boolean validateMove(Move move, boolean ignoreColorCheck) {
        // check for out of bounds
        if (move.getDestinationFile() < 'a' || move.getDestinationFile() > 'h'
                || move.getDestinationRank() < 1 || move.getDestinationRank() > 8) {
            return false;
        }

        // check for valid origin
        if (move.getPiece() == null) {
            return false;
        }

        // check for valid color
        if (!move.getPiece().getColor().equals(currentMoveColor) && !ignoreColorCheck) {
            return false;
        }

        // check for valid destination
        if (move.getCapturedPiece() != null) {
            if (move.getPiece().getColor().equals(move.getCapturedPiece().getColor())) {
                return false;
            }
        }

        // check for piece rule
        if (!move.getPiece().validateMove(move)) {
            return false;
        }

        // check for clear path
        if (!validateClearPath(move)) {
            return false;
        }

        currentMoveColor = currentMoveColor.equals(Piece.Color.WHITE) ? Piece.Color.BLACK : Piece.Color.WHITE;
        return true;
    }

    public static boolean isCheckMove(Move move) {
        // TODO-check
        char[] files = {'a','b','c','d','e','f','g','h'};
        int[] ranks = {1,2,3,4,5,6,7,8};
        Piece.Color color = Piece.Color.WHITE;
        if(move.getPiece().getColor().equals(color)){
            char blackKingFile = PieceSet.getOpponentKingFile(color);
            int blackKingRank = PieceSet.getOpponentKingRank(color);
            for(char file : files){
                for(int rank : ranks){
                    if (Board.getSquare(file, rank).getCurrentPiece() == null) {
                        continue;
                    }
                    Piece currentPiece = Board.getSquare(file, rank).getCurrentPiece();
                    if (currentPiece.getColor().equals(color)){
                        Move check = new Move(file,rank,blackKingFile,blackKingRank);
                        if(currentPiece.validateMove(check)){
                            System.out.println("Check");
                            return true;
                        }
                    }
                }
            }
        }

        color = Piece.Color.BLACK;
        if(move.getPiece().getColor().equals(color)){
            char blackKingFile = PieceSet.getOpponentKingFile(color);
            int blackKingRank = PieceSet.getOpponentKingRank(color);
            for(char file : files){
                for(int rank : ranks){
                    if (Board.getSquare(file, rank).getCurrentPiece() == null) {
                        continue;
                    }
                    Piece currentPiece = Board.getSquare(file, rank).getCurrentPiece();
                    if (currentPiece.getColor().equals(color)){
                        Move check = new Move(file,rank,blackKingFile,blackKingRank);
                        if(currentPiece.validateMove(check)){
                            System.out.println("Check");
                            return true;
                        }
                    }
                }
            }
        }



        return false;
    }

    public static boolean isCheckMate(Move move) {
        // TODO-check
        return false;
    }

    private static boolean validateClearPath(Move move) {
        // TODO-movement
        return false;
    }

}
