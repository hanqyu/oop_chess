package util;

import pieces.PieceSet;

import java.util.ArrayList;

public class GameStatus {
    public class Time {
        private pieces.Piece.Color color;
        private String time;

        Time(pieces.Piece.Color color, String time) {
            this.color = color;
            this.time = time;
        }

        Time(String color, String time) {
            this(pieces.Piece.Color.valueOf(color), time);
        }

        public pieces.Piece.Color getColor() {
            return color;
        }

        public String getTime() {
            return time;
        }
    }

    public class Piece {
        private pieces.Piece.Color color;
        private pieces.Piece.Type type;
        private int rank;
        private String file;
        private String special;

        Piece(String color, String type, int rank, String file, String special) {
            this.color = pieces.Piece.Color.valueOf(color);
            this.type = pieces.Piece.Type.valueOf(type);
            this.rank = rank;
            this.file = file;
            this.special = special;
        }

        Piece(String color, String type, int rank, String file) {
            this(color, type, rank, file, null);
        }

        public pieces.Piece.Color getColor() {
            return color;
        }

        public pieces.Piece.Type getType() {
            return type;
        }

        public int getRank() {
            return rank;
        }

        public String getFile() {
            return file;
        }

        String getSpecial() {
            return special;
        }

        public pieces.Piece makePiece() {
            // TODO check if Casting infomation set properly
            pieces.Piece pieceObj = PieceSet.getPieces(color, type).iterator().next();
            if (getSpecial() != null) {
                pieceObj.setEverMoved();
            }
            return pieceObj;
        }
    }

    private pieces.Piece.Color turn;
    private ArrayList<Time> times = new ArrayList<>();
    private ArrayList<Piece> pieceObjs = new ArrayList<>();

    // properties from Preferences
    private Preferences.GameMode gameMode = null;
    private Preferences.NetworkMode networkMode = null;
    private Preferences.TimerMode timerMode = null;
    private int timeLimit;
    private boolean usingCustomPieces = false;
    private boolean boardReversed = false;
    private String hostIP = null;
    private int port;
    private String playerName = null;

    static final String[] preferenceNames = {"gameMode", "networkMode", "timerMode", "timeLimit", "usingCustomPieces", "boardReversed", "hostIP", "port", "playerName"};

    pieces.Piece.Color getTurn() {
        return turn;
    }

    private void setTurn(pieces.Piece.Color turn) {
        this.turn = turn;
    }

    void setTurn(String turn) {
        setTurn(pieces.Piece.Color.valueOf(turn));
    }

    ArrayList<Time> getTimes() {
        return this.times;
    }

    public ArrayList<Piece> getPieceObjs() {
        return this.pieceObjs;
    }

    void addTime(String color, String time) {
        this.times.add(new Time(color, time));
    }

    void addPiece(String color, String type, int rank, String file, String special) {
        this.pieceObjs.add(new Piece(color, type, rank, file, special));
    }

    void addPiece(String color, String type, int rank, String file) {
        this.pieceObjs.add(new Piece(color, type, rank, file));
    }

    public Time getWhiteTime() {
        for (int i = 0; i <= 1; i++) {
            if (getTimes().get(i).getColor().equals(pieces.Piece.Color.WHITE)) {
                return getTimes().get(i);
            }
        }
        return null;
    }

    public Time getBlackTime() {
        for (int i = 0; i <= 1; i++) {
            if (getTimes().get(i).getColor().equals(pieces.Piece.Color.BLACK)) {
                return getTimes().get(i);
            }
        }
        return null;
    }

    void setValue(String name, String value) {
        if (value.equals("null")) {
            value = null;
        }

        switch (name) {
            case "gameMode":
                setGameMode(value);
                break;
            case "networkMode":
                setNetworkMode(value);
                break;
            case "timerMode":
                setTimerMode(value);
                break;
            case "timeLimit":
                setTimeLimit(value);
                break;
            case "usingCustomPieces":
                setUsingCustomPieces(value);
                break;
            case "boardReversed":
                setBoardReversed(value);
                break;
            case "hostIP":
                setHostIP(value);
                break;
            case "port":
                setPort(value);
                break;
            case "playerName":
                setPlayerName(value);
                break;
        }
    }

    String getValue(String name) {
        Object value = null;
        String typedValue;

        switch (name) {
            case "gameMode":
                value = getGameMode();
                break;
            case "networkMode":
                value = getNetworkMode();
                break;
            case "timerMode":
                value = getTimerMode();
                break;
            case "timeLimit":
                value = String.valueOf(getTimeLimit());
                break;
            case "usingCustomPieces":
                value = String.valueOf(isUsingCustomPieces());
                break;
            case "boardReversed":
                value = String.valueOf(isBoardReversed());
                break;
            case "hostIP":
                value = getHostIP();
                break;
            case "port":
                value = String.valueOf(getPort());
                break;
            case "playerName":
                value = getPlayerName();
                break;
        }
        try {
            typedValue = value.toString();
        } catch (NullPointerException e) {
            typedValue = "null";
        }
        return typedValue;
    }

    /* general getter and setter for private properties,
     * including overload setter for String parameter */

    Preferences.GameMode getGameMode() {
        return gameMode;
    }

    Preferences.NetworkMode getNetworkMode() {
        return networkMode;
    }

    void setGameMode(Preferences.GameMode gameMode) {
        this.gameMode = gameMode;
    }

    private void setGameMode(String gameMode) {
        try {
            setGameMode(Preferences.GameMode.valueOf(gameMode));
        } catch (NullPointerException e) {
            this.gameMode = null;
        }
    }

    void setNetworkMode(Preferences.NetworkMode networkMode) {
        this.networkMode = networkMode;
    }

    private void setNetworkMode(String networkMode) {
        try {
            setNetworkMode(Preferences.NetworkMode.valueOf(networkMode));
        } catch (NullPointerException e) {
            this.networkMode = null;
        }
    }

    public util.Preferences.TimerMode getTimerMode() {
        return timerMode;
    }

    void setTimerMode(util.Preferences.TimerMode timerMode) {
        this.timerMode = timerMode;
    }

    void setTimerMode(String timerMode) {
        try {
            setTimerMode(util.Preferences.TimerMode.valueOf(timerMode));
        } catch (NullPointerException e) {
            this.timerMode = null;
        }
    }

    int getTimeLimit() {
        return timeLimit;
    }

    void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    private void setTimeLimit(String timeLimit) { setTimeLimit(Integer.parseInt(timeLimit)); }

    boolean isUsingCustomPieces() {
        return usingCustomPieces;
    }

    void setUsingCustomPieces(boolean usingCustomPieces) {
        this.usingCustomPieces = usingCustomPieces;
    }

    private void setUsingCustomPieces(String usingCustomPieces) {
        setUsingCustomPieces(Boolean.valueOf(usingCustomPieces));
    }

    String getHostIP() {
        return hostIP;
    }

    void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    int getPort() {
        return port;
    }

    void setPort(int port) {
        this.port = port;
    }

    private void setPort(String port) {
        setPort(Integer.parseInt(port));
    }

    String getPlayerName() {
        return playerName;
    }

    void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    boolean isBoardReversed() {
        return boardReversed;
    }

    void setBoardReversed(boolean bool) {
        this.boardReversed = bool;
    }

    private void setBoardReversed(String reversed) {
        if (reversed.equals("true")) {
            setBoardReversed(true);
        } else if (reversed.equals("false")) {
            setBoardReversed(false);
        }
    }
}
