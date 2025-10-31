package game.logs;

import java.util.List;

public interface GameLog {
    void addLog(String event);
    void clear();
    List<String> snapshot();
}
