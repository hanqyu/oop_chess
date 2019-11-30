package pieces;

import util.Move;

public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color);
        this.type = Type.PAWN;
//        this.everMoved = false;
        this.doubleMoveRank = 2;  // 초기위치의 rank 지정
        if (this.color.equals(Color.BLACK)) {
            this.doubleMoveRank = 7;
        }
        this.rankDifferenceForPawn = 1;  // 움직일 수 있는 rank
        if (this.color.equals(Color.BLACK)) {
            this.rankDifferenceForPawn = -1;
        }
        this.hasSpecialMove = true;  // able to go 2 rank at first
    }

    @Override
    public boolean validateMove(Move move) {
        // TODO Pawn validateMove
        if (moveValidateCondition(move)) {
            if (move.getCapturedPiece() == null) {  // 잡지 않고 움직이는 경우
                if ((move.getDestinationFile() == move.getOriginFile()) && (move.getDestinationRank() - move.getOriginRank()) == rankDifferenceForPawn) {
                    return true;  // 한칸만 움직임
                }
                if ((move.getDestinationFile() == move.getOriginFile()) && (move.getDestinationRank() - move.getOriginRank()) == rankDifferenceForPawn * 2) {
                    if (move.getOriginRank() == doubleMoveRank) {
                        return true;  // 두칸까지 움직임. 단, 시작지점의 rank 인 경우
                    }
                }
            }
            if (move.getCapturedPiece() != null) {  // 잡으면서 움직이는 경우. 전진방향 대각선
                if (Math.abs(move.getDestinationFile() - move.getOriginFile()) == 1 && (move.getDestinationRank() - move.getOriginRank()) == rankDifferenceForPawn) {
                    return true;
                }
            }
        }
        return false;
    }
}

