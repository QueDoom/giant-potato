package net.quedoon.giant_potato.block.util;

import net.minecraft.util.StringIdentifiable;

public enum SewerDrainShape implements StringIdentifiable {
    DRAIN_ONLY("drain_only"),
    DRAIN_FALL("drain_fall"),
    DRAIN_TUNNEL("drain_tunnel"),
    TUNNEL("tunnel");

    private final String name;

    @Override
    public String asString() {
        return this.name;
    }

    SewerDrainShape(final String name) {
        this.name = name;
    }

    public String toString() {
        return this.asString();
    }
}
