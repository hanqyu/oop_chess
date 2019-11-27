package util;

import board.Board;
import board.Square;
import pieces.Piece;
import pieces.PieceSet;
import ui.BoardPanel;


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

    static Piece.Color attackColor;
    static char attackKingFile;
    static int attackKingRank;
    static char defendKingFile;
    static int defendKingRank;
    static char[] files = {'a','b','c','d','e','f','g','h'};
    static int[] ranks = {1,2,3,4,5,6,7,8};

    public static boolean isCheckMove(Move move) {
        // TODO-check
        attackColor = move.getPiece().getColor();
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
                        if(validateClearPath(new Move(file, rank, defendKingFile, defendKingRank))){  // 방어자의 King에 이르는 길 사이에 아무것도 없는가?
                            return true;
                        }
                    }
                }
                //자살 CHECK. 이동 불가! return값도 false!
                if(!Board.getSquare(file,rank).getCurrentPiece().getColor().equals(move.getPiece().getColor())){
                    Piece defendPiece = Board.getSquare(file,rank).getCurrentPiece();
                    if(defendPiece.validateMove(new Move(file, rank, attackKingFile, attackKingRank))){
                        if(validateClearPath(new Move(file, rank, defendKingFile, defendKingRank))){  // 방어자의 King에 이르는 길 사이에 아무것도 없는가?
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean isCheckMate(Move move) {
        // TODO-check
//        attackColor = move.getPiece().getColor();
//        defendKingFile = PieceSet.getOpponentKingFile(move.getPiece().getColor());
//        defendKingRank = PieceSet.getOpponentKingRank(move.getPiece().getColor());
//        attackKingFile = PieceSet.getOpponentKingFile(Board.getSquare(defendKingFile,defendKingRank).getCurrentPiece().getColor());
//        attackKingRank = PieceSet.getOpponentKingRank(Board.getSquare(defendKingFile,defendKingRank).getCurrentPiece().getColor());
        Move defendMove;
        Piece opponentPiece;

        int not0WhenNotCheckMate = 0;
        int iHaveTried = 0;

        for(char file : files){
            for(int rank : ranks){
                if(Board.getSquare(file,rank).getCurrentPiece()==null){                }
                if(Board.getSquare(file,rank).getCurrentPiece()!=null
                        &&!(Board.getSquare(file,rank).getCurrentPiece().getColor().equals(move.getPiece().getColor()))){ // move를 유발한 색과 다른 색(defend측)의 기물을 모두 탐색
                    Piece defendPiece = Board.getSquare(file,rank).getCurrentPiece(); // 탐색되면 defendPiece에 할당
                    System.out.println(defendPiece.toString());
                    for(char defFile : files){
                        for(int defRank : ranks){                                     // 갈 수 있는 위치들을 탐색
                            if(defendPiece.validateMove(new Move(file,rank,defFile,defRank))){ // 갈 수 있다면?
                                System.out.println(defendPiece.toString()+"'ve gone.. ("+defFile+", "+defRank);
//                                defendMove = new Move(defendPiece,file,rank,defFile,defRank);  // 갈 수 있는 움직임을 만들어 defendMove에 할당
                                if(Board.getSquare(defFile,defRank).getCurrentPiece()==null){  // 목적지에 아무도 없다면?
                                    Board.getSquare(defFile,defRank).setCurrentPiece(defendPiece);  // 목적지에 defendPiece를 두는데
                                    if(defendPiece.getType()== Piece.Type.KING){  // 왕이 움직이는 경우엔 우리가 임시로 가진 King의 위치를 변경
                                        defendKingFile = defFile;
                                        defendKingRank = defRank;
                                    }
                                    Board.getSquare(file,rank).setCurrentPiece(null);  // 원래 있던 곳엔 null을 할당해 piece를 없애고
                                    if(isDefendSuccess()){                   // 이 상황에서 defend를 Success한 적이 한번이라도 있다?
                                        not0WhenNotCheckMate++;                        // 그렇다면 checkmate가 아님.
                                    }
                                    Board.getSquare(file,rank).setCurrentPiece(defendPiece); //다시 제자리에 놔두기
                                    Board.getSquare(defFile,defRank).setCurrentPiece(null);
                                    if(defendPiece.getType()== Piece.Type.KING){  // 왕이 움직이는 경우엔 우리가 임시로 가진 King의 위치를 변경
                                        defendKingFile = file;
                                        defendKingRank = rank;
                                    }
                                    iHaveTried++;
                                    if(not0WhenNotCheckMate!=0){
                                        return false;
                                    }
                                }
                                if(Board.getSquare(defFile,defRank).getCurrentPiece()!=null
                                        &&Board.getSquare(defFile,defRank).getCurrentPiece().getColor()!=defendPiece.getColor()){ // 목적지에 다른 색의 기물이 있다면?
                                    opponentPiece = Board.getSquare(defFile,defRank).getCurrentPiece(); // 그 기물을 저장해두고
                                    System.out.println("and captured ("+opponentPiece.toString()+")");
                                    Board.getSquare(defFile,defRank).setCurrentPiece(defendPiece);      // 목적지에 defendPiece를 두고
                                    Board.getSquare(file,rank).setCurrentPiece(null);                   // 원래 있던 곳은 null
                                    if(defendPiece.getType()== Piece.Type.KING){  // 왕이 움직이는 경우엔 우리가 임시로 가진 King의 위치를 변경
                                        defendKingFile = defFile;
                                        defendKingRank = defRank;
                                    }
                                    if(isDefendSuccess()){                                    // 이 상황에서 defend를 Success한 적이 한번이라도 있다?
                                        not0WhenNotCheckMate++;                                         // 그렇다면 checkmate가 아님.
                                    }
                                    Board.getSquare(file,rank).setCurrentPiece(defendPiece);            // defendPiece를 제자리에 놔두기
                                    Board.getSquare(defFile,defRank).setCurrentPiece(opponentPiece);    // 원래 있던 공격자의 기물 두기
                                    if(defendPiece.getType()== Piece.Type.KING){  // 왕이 움직이는 경우엔 우리가 임시로 가진 King의 위치를 변경
                                        defendKingFile = file;
                                        defendKingRank = rank;
                                    }
                                    iHaveTried++;
                                    if(not0WhenNotCheckMate!=0){
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
//        System.out.println(iHaveTried);
//        if(not0WhenNotCheckMate!=0){
//            return false;
//        }
        return true;
    }

    public static boolean isDefendSuccess(){
        for(char file : files){
            for(int rank : ranks){
                if(Board.getSquare(file,rank).getCurrentPiece()==null){                }
                if(Board.getSquare(file,rank).getCurrentPiece()!=null
                        &&Board.getSquare(file,rank).getCurrentPiece().getColor().equals(attackColor)){ // 다시 공격자의 기물을 하나씩 탐색
                    if(Board.getSquare(file,rank).getCurrentPiece().validateMove(new Move(file,rank,defendKingFile,defendKingRank))){ // 만약 하나의 기물이라도 방어자의 King을 잡을 수 있다면?
                        if(validateClearPath(new Move(file, rank, defendKingFile, defendKingRank))){  // 또한 방어자의 King 에게 다다르는 길이 Clear 하다면?
                            System.out.println("But "+Board.getSquare(file,rank).getCurrentPiece().toString()+" got the King");  // false
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private static boolean validateClearPath(Move move) {
        // TODO-movement
        int blockPiece = 0;
        if(Math.abs(move.getDestinationFile() - move.getOriginFile()) == 1 || Math.abs(move.getDestinationRank() - move.getOriginRank()) == 1){  // 한칸씩만 움직이는 경우 당연히 true
            return true;
        }

        if((move.getDestinationFile()==move.getOriginFile()) && Math.abs(move.getDestinationRank()-move.getOriginRank()) > 1){  // Rank 를 따라 움직이는 경우
            for(int i = 1; Math.min(move.getDestinationRank(), move.getOriginRank()) + i < Math.max(move.getDestinationRank(),move.getOriginRank()); i++){
                if(Board.getSquare(move.getOriginFile(), Math.min(move.getDestinationRank(), move.getOriginRank()) + i).getCurrentPiece() != null){
                    blockPiece++;
                }
            }
            if(blockPiece>0){
                return false;
            }
            return true;
        }
        if((move.getDestinationRank()==move.getOriginRank()) && Math.abs(move.getDestinationFile()-move.getOriginFile()) > 1){  // File 을 따라 움직이는 경우
            for(int i = 1; Math.min(move.getDestinationFile(), move.getOriginFile()) + i < Math.max(move.getDestinationFile(),move.getOriginFile()); i++){
                if(Board.getSquare((char)(Math.min(move.getDestinationFile(), move.getOriginFile()) + i), move.getOriginRank()).getCurrentPiece() != null){
                    blockPiece++;
                }
            }
            if(blockPiece>0){
                return false;
            }
            return true;
        }
        if(Math.abs(move.getDestinationFile()-move.getOriginFile()) == Math.abs(move.getDestinationRank()-move.getOriginRank())){  // 대각선으로 움직이는 경우
            if ((move.getDestinationFile()-move.getOriginFile())*(move.getDestinationRank()-move.getOriginRank()) > 0) {  // 우상향, 좌하향 움직임인 경우
                for(int i = 1; Math.min(move.getDestinationRank(), move.getOriginRank()) + i < Math.max(move.getDestinationRank(),move.getOriginRank()); i++){
                    if(Board.getSquare((char)(Math.min(move.getDestinationFile(), move.getOriginFile()) + i), Math.min(move.getDestinationRank(), move.getOriginRank()) + i).getCurrentPiece() != null){
                        blockPiece++;
                    }
                }
                if(blockPiece>0){
                    return false;
                }
                return true;
            }
            if ((move.getDestinationFile()-move.getOriginFile())*(move.getDestinationRank()-move.getOriginRank()) < 0) {  // 우하향, 좌상향 움직임인 경우
                for(int i = 1; Math.min(move.getDestinationRank(), move.getOriginRank()) + i < Math.max(move.getDestinationRank(),move.getOriginRank()); i++){
                    if(Board.getSquare((char)(Math.max(move.getDestinationFile(), move.getOriginFile()) - i), Math.min(move.getDestinationRank(), move.getOriginRank()) + i).getCurrentPiece() != null){
                        blockPiece++;
                    }
                }
                if(blockPiece>0){
                    return false;
                }
                return true;
            }
        }
        return false;
    }


}
