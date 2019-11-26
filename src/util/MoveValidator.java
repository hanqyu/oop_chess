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


    static char attackKingFile;
    static int attackKingRank;
    static char defendKingFile;
    static int defendKingRank;
    static char[] files = {'a','b','c','d','e','f','g','h'};
    static int[] ranks = {1,2,3,4,5,6,7,8};

    public static boolean isCheckMove(Move move) {
        // TODO-check
        defendKingFile = PieceSet.getOpponentKingFile(move.getPiece().getColor());
        defendKingRank = PieceSet.getOpponentKingRank(move.getPiece().getColor());
        attackKingFile = PieceSet.getOpponentKingFile(Board.getSquare(defendKingFile,defendKingRank).getCurrentPiece().getColor());
        attackKingRank = PieceSet.getOpponentKingRank(Board.getSquare(defendKingFile,defendKingRank).getCurrentPiece().getColor());

        for(char file : files){
            for(int rank : ranks){
                if(Board.getSquare(file,rank).getCurrentPiece()==null){
                    continue;
                }
                if(Board.getSquare(file,rank).getCurrentPiece().getColor().equals(move.getPiece().getColor())){
                    Piece attackPiece = Board.getSquare(file,rank).getCurrentPiece();
                    if(attackPiece.validateMove(new Move(file, rank, defendKingFile, defendKingRank))){
                        return true;
                    }
                }
                //자살 CHECK. 이동 불가!
                if(!Board.getSquare(file,rank).getCurrentPiece().getColor().equals(move.getPiece().getColor())){
                    Piece defendPiece = Board.getSquare(file,rank).getCurrentPiece();
                    if(defendPiece.validateMove(new Move(file, rank, attackKingFile, attackKingRank))){
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isDefendSuccess(Move move){
        for(char file : files){
            for(int rank : ranks){
                if(Board.getSquare(file,rank).getCurrentPiece()==null){
                    continue;
                }
                if(Board.getSquare(file,rank).getCurrentPiece().getColor()!=move.getPiece().getColor()){
                    if(Board.getSquare(file,rank).getCurrentPiece().validateMove(new Move(file,rank,defendKingFile,defendKingRank))){
                        return false;
                    } else { continue; }
                }
            }
        }
        return true;
    }

    public static boolean isCheckMate(Move move) {
        // TODO-check
        defendKingFile = PieceSet.getOpponentKingFile(move.getPiece().getColor());
        defendKingRank = PieceSet.getOpponentKingRank(move.getPiece().getColor());
        attackKingFile = PieceSet.getOpponentKingFile(Board.getSquare(defendKingFile,defendKingRank).getCurrentPiece().getColor());
        attackKingRank = PieceSet.getOpponentKingRank(Board.getSquare(defendKingFile,defendKingRank).getCurrentPiece().getColor());
        Move defendMove;
        Piece opponentPiece;

        for(char file : files){
            for(int rank : ranks){
                if(Board.getSquare(file,rank).getCurrentPiece()==null){
                    continue;
                }
                if(!Board.getSquare(file,rank).getCurrentPiece().getColor().equals(move.getPiece().getColor())){
                    Piece defendPiece = Board.getSquare(file,rank).getCurrentPiece();
                    for(char defFile : files){
                        for(int defRank : ranks){
                            if(defendPiece.validateMove(new Move(file,rank,defFile,defRank))){
                                defendMove = new Move(defendPiece,file,rank,defFile,defRank);
                                if(Board.getSquare(defFile,defRank).getCurrentPiece()==null){
                                    Board.getSquare(defFile,defRank).setCurrentPiece(defendPiece);
                                    Board.getSquare(file,rank).setCurrentPiece(null);
                                    if(isDefendSuccess(defendMove)){
                                        return true;
                                    }
                                    Board.getSquare(file,rank).setCurrentPiece(defendPiece);
                                    Board.getSquare(defFile,defRank).setCurrentPiece(null);
                                    continue;
                                }
                                if(Board.getSquare(defFile,defRank).getCurrentPiece().getColor()!=defendPiece.getColor()){
                                    opponentPiece = Board.getSquare(defFile,defRank).getCurrentPiece();
                                    Board.getSquare(defFile,defRank).setCurrentPiece(defendPiece);
                                    Board.getSquare(file,rank).setCurrentPiece(opponentPiece);
                                    if(isDefendSuccess(defendMove)){
                                        return true;
                                    }
                                    Board.getSquare(file,rank).setCurrentPiece(defendPiece);
                                    Board.getSquare(defFile,defRank).setCurrentPiece(opponentPiece);
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean validateClearPath(Move move) {
        // TODO-movement
        return false;
    }

}
