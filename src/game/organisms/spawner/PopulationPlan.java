package game.organisms.spawner;

import game.organisms.Organism;
import java.util.*;

public final class PopulationPlan {
    public static final class Builder {
        private final Map<Class<? extends Organism>, Integer> byType = new LinkedHashMap<>();
        private Class<? extends Organism> humanType;

        /** 1 Human */
        public Builder human(Class<? extends Organism> human) {
            this.humanType = Objects.requireNonNull(human);
            return this;
        }

        /** Organisms with percent of fields */
        public Builder percent(Class<? extends Organism> type, int percent) {
            if (percent < 0) throw new IllegalArgumentException("percent < 0");
            byType.put(Objects.requireNonNull(type), percent);
            return this;
        }

        public PopulationPlan build() {
            if (humanType == null) throw new IllegalStateException("Human type must be set");
            return new PopulationPlan(humanType, Collections.unmodifiableMap(new LinkedHashMap<>(byType)));
        }
    }

    private final Class<? extends Organism> humanType;
    private final Map<Class<? extends Organism>, Integer> percentByType;

    private PopulationPlan(Class<? extends Organism> humanType,
                           Map<Class<? extends Organism>, Integer> percentByType) {
        this.humanType = humanType;
        this.percentByType = percentByType;
    }

    public Class<? extends Organism> humanType() { return humanType; }
    public Map<Class<? extends Organism>, Integer> percentByType() { return percentByType; }

}
