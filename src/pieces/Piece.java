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
    protected boolean capture;
    protected boolean hasSpecialMove;
    protected boolean everMoved = false;

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

    public Type getType() { return type; }

    public boolean hasSpecialMove() { return hasSpecialMove; }

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


    public void evokedSpecialMove() { }

    public void setEverMoved(){
        this.everMoved = true;
    }

    public boolean getEverMoved() { return this.everMoved; }

}
