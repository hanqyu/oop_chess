package pieces;

import util.Move;

/**
 * Abstract class for chess piece.
 */
public abstract class Piece {

    public enum Color {
        WHITE, BLACK
    }

    public enum Type {
        KING, ROOK, BISHOP, QUEEN, KNIGHT, PAWN
    }

    protected Color color;
    protected Type type;
    protected int doubleMoveRank;
    protected int rankDifferenceForPawn;
    protected int enPassantRank;
    protected int enPassantedRank;
    protected boolean capture;
    protected int movingTimes = 0;

    public Piece(Color color) {
        this.color = color;
        this.capture = false;
    }

    public abstract boolean validateMove(Move move);

    public String getImageFileName() {
        String fileName = "/pieces/";
        switch (color) {
            case WHITE:
                fileName += "white_";
                break;
            case BLACK:
                fileName += "black_";
                break;
        }
        switch (type) {
            case KING:
                fileName += "king";
                break;
            case ROOK:
                fileName += "rook";
                break;
            case BISHOP:
                fileName += "bishop";
                break;
            case QUEEN:
                fileName += "queen";
                break;
            case KNIGHT:
                fileName += "knight";
                break;
            case PAWN:
                fileName += "pawn";
                break;
        }
        fileName += ".png";
        return fileName;
    }

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }


    public void setCapture(boolean isCaptured) {
        this.capture = isCaptured;
    }

    public boolean getCapture() {
        return this.capture;
    }

    public boolean moveValidateCondition(Move move) {
        return (move.getCapturedPiece() == null)
                || (move.getCapturedPiece() != null
                && !move.getPiece().getColor().equals(move.getCapturedPiece().getColor()));
    }

    public int getRankDifferenceForPawn() {
        return rankDifferenceForPawn;
    }

    public void setEverMoved() {
    }

    public int getMovingTimes() {
        return this.movingTimes;
    }

    public void plusMovingTimes() {
        this.movingTimes++;
    }

    public void minusMovingTimes() {
        this.movingTimes--;
    }

    public int getDoubleMoveRank() {
        return doubleMoveRank;
    }

    public int getEnPassantRank() {
        return enPassantRank;
    }

    public int getEnPassantedRank() {
        return enPassantedRank;
    }
}
