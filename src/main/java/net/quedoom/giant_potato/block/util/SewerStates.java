package net.quedoom.giant_potato.block.util;

import net.minecraft.util.StringRepresentable;

public enum SewerStates implements StringRepresentable {
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
    public String getSerializedName() {
        return this.name;
    }

    SewerStates(final String name) {
        this.name = name;
    }

    public String toString() {
        return this.getSerializedName();
    }
}
