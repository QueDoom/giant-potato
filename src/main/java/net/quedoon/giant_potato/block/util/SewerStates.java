package net.quedoon.giant_potato.block.util;

import net.minecraft.util.StringIdentifiable;

public enum SewerStates implements StringIdentifiable {
    CONNECT_LEFT("left"),
    LEFT_ONLY("left_only"),
    CONNECT_FORWARD("forward"),
    FORWARD_ONLY("forward_only"),
    CONNECT_RIGHT("right"),
    RIGHT_ONLY("right_only"),
    BACKWARDS_ONLY("back"),
    DO_NOT_CONNECT("none"),
    NO_SHAPE("shape");

    private final String name;

    @Override
    public String asString() {
        return this.name;
    }

    SewerStates(final String name) {
        this.name = name;
    }

    public String toString() {
        return this.asString();
    }
}
