package pieces;

import board.Board;
import util.Move;
import util.MoveValidator;

public class Pawn extends Piece {
//     private boolean everMoved;

    public Pawn(Color color) {
        super(color);
        this.type = Type.PAWN;
//        this.everMoved = false;
        this.doubleMoveRank = 2;  // 초기위치의 rank 지정
        if(this.color.equals(Color.BLACK)){
            this.doubleMoveRank = 7;
        }
        this.rankDifferenceForPawn = 1;  // 움직일 수 있는 rank
        if(this.color.equals(Color.BLACK)){
            this.rankDifferenceForPawn = -1;
        }
        this.enPassantRank = 5;
        if(this.color.equals(Color.BLACK)){
            this.enPassantRank = 4;
        }
        this.enPassantedRank = 4;
        if(this.color.equals(Color.BLACK)){
            this.enPassantedRank = 5;
        }
//        this.hasSpecialMove = true;  // able to go 2 rank at first
    }

    @Override
    public boolean validateMove(Move move) {
        // TODO Pawn validateMove
        if (moveValidateCondition(move)) {
            if(move.getCapturedPiece()==null){
                if((move.getDestinationFile() == move.getOriginFile()) && (move.getDestinationRank() - move.getOriginRank()) == rankDifferenceForPawn){
                    return true;
                }
                if((move.getDestinationFile() == move.getOriginFile()) && (move.getDestinationRank() - move.getOriginRank()) == rankDifferenceForPawn*2){
                    if(move.getOriginRank() == doubleMoveRank){
                        return true;
                    }
                }
                if(Math.abs(move.getDestinationFile() - move.getOriginFile()) == 1 && (move.getDestinationRank() - move.getOriginRank()) == rankDifferenceForPawn){
                    if(move.getOriginRank()==enPassantRank){
                        if(MoveValidator.isEnPassantPossible(move)){
                            return true;
                        }
                    }
                }
            }
            if(move.getCapturedPiece()!=null){
                if(Math.abs(move.getDestinationFile() - move.getOriginFile()) == 1 && (move.getDestinationRank() - move.getOriginRank()) == rankDifferenceForPawn){
                    return true;
                }
            }
        }
        return false;
    }

}


