package net.quedoon.giant_potato.block.util;

import net.minecraft.state.property.EnumProperty;

public class ModProperties {
    public static final EnumProperty<PipeShape.PipeShapes> NORTH_PIPE_SHAPE = EnumProperty.of("north_pipe_shape", PipeShape.PipeShapes.class);
    public static final EnumProperty<PipeShape.PipeShapes> SOUTH_PIPE_SHAPE = EnumProperty.of("south_pipe_shape", PipeShape.PipeShapes.class);
    public static final EnumProperty<PipeShape.PipeShapes> EAST_PIPE_SHAPE = EnumProperty.of("east_pipe_shape", PipeShape.PipeShapes.class);
    public static final EnumProperty<PipeShape.PipeShapes> WEST_PIPE_SHAPE = EnumProperty.of("west_pipe_shape", PipeShape.PipeShapes.class);
    public static final EnumProperty<PipeShape.PipeShapes> UP_PIPE_SHAPE = EnumProperty.of("up_pipe_shape", PipeShape.PipeShapes.class);
    public static final EnumProperty<PipeShape.PipeShapes> DOWN_PIPE_SHAPE = EnumProperty.of("down_pipe_shape", PipeShape.PipeShapes.class);

    public static final EnumProperty<SewerDrainShape> DRAIN_SHAPE = EnumProperty.of("drain_shape", SewerDrainShape.class);
    public static final EnumProperty<SewerStates> SEWER_STATES = EnumProperty.of("sewer_states", SewerStates.class);
}
