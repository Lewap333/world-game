package game.logs;

import java.util.ArrayList;
import java.util.List;

public class MemoryLog implements GameLog {
    private final List<String> events = new ArrayList<>();

    @Override
    public void addLog(String event) {
        if (event != null && !event.isEmpty()) {
            events.add(event);
        }
    }

    @Override
    public void clear() {
        events.clear();
    }

    @Override
    public List<String> snapshot() {
        return List.copyOf(events);
    }
}
