package util;

public class Preferences {

    Preferences() {}

    Preferences(GameStatus gameStatus) {
        this.gameMode = gameStatus.getGameMode();
        this.networkMode = gameStatus.getNetworkMode();
        this.timerMode = gameStatus.getTimerMode();
        this.timeLimit = gameStatus.getTimeLimit();
        this.usingCustomPieces = gameStatus.isUsingCustomPieces();
        this.boardReversed = gameStatus.isBoardReversed();
        this.hostIP = gameStatus.getHostIP();
        this.port = gameStatus.getPort();
        this.playerName = gameStatus.getPlayerName();
    }

    public enum GameMode {
        ONLINE, OFFLINE
    }

    public enum NetworkMode {
        HOST, CLIENT
    }

    public enum TimerMode {
        COUNTDOWN, STOPWATCH
    }

    private GameMode gameMode = null;
    private NetworkMode networkMode = null;
    private TimerMode timerMode = null;
    private int timeLimit;
    private boolean usingCustomPieces = false;
    private boolean boardReversed = false;
    private String hostIP = null;
    private int port;
    private String playerName = null;

    public boolean isPreferencesComplete() {
        if (gameMode == null) {
            return false;
        }
        switch (gameMode) {
            case ONLINE:
                if (networkMode == null || hostIP == null || port == 0 || playerName == null) {
                    return false;
                } else if (networkMode.equals(NetworkMode.HOST)) {
                    if (timerMode == null) {
                        return false;
                    }
                }
                return true;
            case OFFLINE:
                if (timerMode == null) {
                    return false;
                }
                return true;
        }
        return false;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public NetworkMode getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(NetworkMode networkMode) {
        this.networkMode = networkMode;
    }

    public TimerMode getTimerMode() {
        return timerMode;
    }

    public void setTimerMode(TimerMode timerMode) {
        this.timerMode = timerMode;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isUsingCustomPieces() {
        return usingCustomPieces;
    }

    public void setUsingCustomPieces(boolean usingCustomPieces) {
        this.usingCustomPieces = usingCustomPieces;
    }

    public boolean isBoardReversed() {
        return boardReversed;
    }

    public void setBoardReversed(boolean boardReversed) {
        this.boardReversed = boardReversed;
    }

    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = new String(hostIP);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}
