package net.quedoon.giant_potato.block.util;

import net.minecraft.util.StringRepresentable;
public class PipeShape {
    public enum PipeShapes implements StringRepresentable {
        NONE("none"),
        TRUE("true"),
        OUTPUT("output");

        private final String name;

        @Override
        public String getSerializedName() {
            return this.name;
        }

        PipeShapes(final String name) {
            this.name = name;
        }

        public String toString() {
            return this.getSerializedName();
        }
    }

    public static class As {
        public static int integer(PipeShapes shapes) {
            return switch (shapes) {
                case NONE -> 0;
                case TRUE -> 2;
                case OUTPUT -> 4;
            };
        }
        public static PipeShapes pipeShapes(int i) {
            return switch (i) {
                case 2, 3 -> PipeShapes.TRUE;
                case 4, 5 -> PipeShapes.OUTPUT;
                default -> PipeShapes.NONE;
            };
        }
    }
}
