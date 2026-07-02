package net.quedoon.giant_potato.block.util;

import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ModProperties {
    public static final EnumProperty<PipeShape.PipeShapes> NORTH_PIPE_SHAPE = EnumProperty.create("north_pipe_shape", PipeShape.PipeShapes.class);
    public static final EnumProperty<PipeShape.PipeShapes> SOUTH_PIPE_SHAPE = EnumProperty.create("south_pipe_shape", PipeShape.PipeShapes.class);
    public static final EnumProperty<PipeShape.PipeShapes> EAST_PIPE_SHAPE = EnumProperty.create("east_pipe_shape", PipeShape.PipeShapes.class);
    public static final EnumProperty<PipeShape.PipeShapes> WEST_PIPE_SHAPE = EnumProperty.create("west_pipe_shape", PipeShape.PipeShapes.class);
    public static final EnumProperty<PipeShape.PipeShapes> UP_PIPE_SHAPE = EnumProperty.create("up_pipe_shape", PipeShape.PipeShapes.class);
    public static final EnumProperty<PipeShape.PipeShapes> DOWN_PIPE_SHAPE = EnumProperty.create("down_pipe_shape", PipeShape.PipeShapes.class);

    public static final EnumProperty<SewerDrainShape> DRAIN_SHAPE = EnumProperty.create("drain_shape", SewerDrainShape.class);
    public static final EnumProperty<SewerStates> SEWER_STATES = EnumProperty.create("sewer_states", SewerStates.class);
}
