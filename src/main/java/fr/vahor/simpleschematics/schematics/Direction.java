package fr.vahor.simpleschematics.schematics;

import fr.vahor.simpleschematics.API;
import lombok.Getter;

public enum Direction {

    SOUTH(270),
    WEST(180),
    NORTH(90),
    EAST(0),

    ;

    @Getter private final int rotation;

    Direction(int rotation) {
        this.rotation = rotation;
    }

    private static final Direction[] values = values();

    public static Direction random() {
        return values[API.RANDOM.nextInt(values.length)];
    }

    public static Direction getDirectionByYaw(double yaw) {
        yaw = yaw % 360;
        if (yaw < 0.0D) {
            yaw += 360.0D;
        }

        if (0 <= yaw && yaw < 90) {
            return SOUTH;
        }
        else if (90 <= yaw && yaw < 180) {
            return WEST;
        }
        else if (180 <= yaw && yaw < 270) {
            return NORTH;
        }
        else {
            return EAST;
        }
    }

    public static Direction getDirectionByAngle(double radian) {
        return getDirectionByYaw(Math.toDegrees(radian));
    }
}
