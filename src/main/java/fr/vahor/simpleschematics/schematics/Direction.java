/*
 *     Copyright (C) 2022 Nathan David
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
