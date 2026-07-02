package net.quedoon.giant_potato.util;

import net.minecraft.core.Direction;

public class GetStringFromDirection {
    public static String minecraftDir(Direction direction) {
        return switch (direction) {
            case DOWN -> "down";
            case UP -> "up";
            case NORTH -> "north";
            case SOUTH -> "south";
            case WEST -> "west";
            case EAST -> "east";
        };
    }
}
