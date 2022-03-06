package fr.vahor.schematics;

import java.util.HashMap;
import java.util.Map;

public enum RotationMode {

    DEFAULT,
    AUTO,
    RANDOM;

    private static final Map<Integer, RotationMode> byOrdinal = new HashMap<>(3, 1);
    private static final int maxOrdinal;

    static {
        RotationMode[] values = values();
        for (RotationMode value : values) {
            byOrdinal.put(value.ordinal(), value);
        }
        maxOrdinal = values.length;
    }

    public RotationMode next() {
        return byOrdinal.get((ordinal() + 1) % maxOrdinal);
    }
}
