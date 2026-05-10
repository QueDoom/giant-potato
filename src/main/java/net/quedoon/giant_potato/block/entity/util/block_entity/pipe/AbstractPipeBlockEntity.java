package net.quedoon.giant_potato.block.entity.util.block_entity.pipe;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.quedoon.giant_potato.GiantPotato;
import net.quedoon.giant_potato.block.entity.data.hitbox.AbstractPipeHitbox;
import net.quedoon.giant_potato.block.entity.util.AbstractInteractionHitbox;
import net.quedoon.giant_potato.block.entity.util.block.AbstractPipeBlock;
import net.quedoon.giant_potato.block.util.ModBlockHitboxes;
import net.quedoon.giant_potato.block.util.ModProperties;
import net.quedoon.giant_potato.block.util.PipeShape;
import net.quedoon.giant_potato.util.ModTags;
import net.quedoon.giant_potato.util.ShapeUtil;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractPipeBlockEntity extends BlockEntity {
    private int NORTH = 0;
    private int SOUTH = 0;
    private int EAST = 0;
    private int WEST = 0;
    private int UP = 0;
    private int DOWN = 0;

    private final AbstractPipeBlock notOutput;
    private final AbstractPipeBlock output;

    private final HashMap<Identifier, AbstractInteractionHitbox> hitBoxes = new HashMap<>();

    protected final Map<String, Integer> sidesIntLookup = Map.of(
            "none", 0,
            "none_locked", 1,
            "true", 2,
            "locked", 3,
            "output", 4,
            "output_locked", 5
    );
    
    public AbstractPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, AbstractPipeBlock notOutput, AbstractPipeBlock output) {
        super(type, pos, state);
        this.initializeInnerHitBoxes();
        this.notOutput = notOutput;
        this.output = output;
    }

    private void initializeInnerHitBoxes() {
        for(Direction dir : Direction.values()) {
            this.hitBoxes.put(
                    AbstractPipeHitbox.getIdentifier(dir),
                    new AbstractPipeHitbox(
                            this,
                            ShapeUtil.getBoxFromVoxelCoordinates(
                                    ModBlockHitboxes.BoxPipe.dirFrom(dir),
                                    ModBlockHitboxes.BoxPipe.dirTo(dir)
                            ),
                            AbstractInteractionHitbox.RED,
                            dir
                    )
            );
        }
    }

    public ActionResult attemptInteraction(PlayerEntity player, Hand hand) {
        BlockState state = this.getCachedState();
        if (!state.isIn(ModTags.Blocks.PIPES)) return ActionResult.FAIL;
        Pair<AbstractInteractionHitbox, Vec3d> targetedHitbox = getTargetedHitbox(player);
        if (targetedHitbox == null) return ActionResult.PASS;
        return targetedHitbox.getLeft().interact(this, targetedHitbox.getRight(), player, hand);
    }

    @Nullable
    protected Pair<AbstractInteractionHitbox, Vec3d> getTargetedHitbox(PlayerEntity player) {
        double distance = -1;
        AbstractInteractionHitbox closestInteraction = null;
        Vec3d closestInteractionHitPos = null;

        double reachDistance = player.isCreative() ? 5.0 : 4.5;
        Vec3d eyePos = player.getEyePos();
        Vec3d fullRangeReach = player.getRotationVector().multiply(reachDistance);
        Vec3d endPos = eyePos.add(fullRangeReach);

        for (var hitBox : this.getHitBoxes().values()) {
            Optional<Vec3d> raycast = hitBox.getRotatedBox(Direction.NORTH).offset(this.pos).raycast(eyePos, endPos);
            if (raycast.isEmpty()) continue;
            Vec3d successfulRaycast = raycast.get();
            double currentDistance = eyePos.squaredDistanceTo(successfulRaycast);
            if (closestInteraction == null || distance > currentDistance) {
                closestInteraction = hitBox;
                distance = currentDistance;
                closestInteractionHitPos = successfulRaycast;
            }
        }
        return closestInteraction == null ? null : new Pair<>(closestInteraction, closestInteractionHitPos);
    }

    public void toggleSideFromHitbox(AbstractPipeBlockEntity blockEntity, Vec3d actualPos, PlayerEntity player, Hand hand, Direction side) {
        BlockPos pos = blockEntity.getPos();
        boolean isServer = player.getWorld() instanceof ServerWorld;
        if (isServer) {
            toggleOutput(blockEntity, side);
        }
    }

    protected void toggleOutput(AbstractPipeBlockEntity blockEntity, Direction direction) {
        assert blockEntity.getWorld() != null;
        BlockState state = blockEntity.getWorld().getBlockState(blockEntity.getPos());
        switch (direction) {
            case DOWN -> {
                int intState = blockEntity.DOWN;
                blockEntity.setSide(state, direction, toggleState(intState));
            }
            case UP -> {
                int intState = blockEntity.UP;
                blockEntity.setSide(state, direction, toggleState(intState));
            }
            case NORTH -> {
                int intState = blockEntity.NORTH;
                blockEntity.setSide(state, direction, toggleState(intState));
            }
            case SOUTH -> {
                int intState = blockEntity.SOUTH;
                blockEntity.setSide(state, direction, toggleState(intState));
            }
            case EAST -> {
                int intState = blockEntity.EAST;
                blockEntity.setSide(state, direction, toggleState(intState));
            }
            case WEST -> {
                int intState = blockEntity.WEST;
                blockEntity.setSide(state, direction, toggleState(intState));
            }
        }
    }

    protected int toggleState(int input) {
        return switch (input) {
            case 1 -> 1;
            case 2, 3 -> 5;
            case 4, 5 -> 2;
            default -> 0;
        };
    }

    public HashMap<Identifier, AbstractInteractionHitbox> getHitBoxes() {
        return hitBoxes;
    }

    public void initializeSides(PipeShape.PipeShapes n, PipeShape.PipeShapes s, PipeShape.PipeShapes e,
                                PipeShape.PipeShapes w, PipeShape.PipeShapes u, PipeShape.PipeShapes d) {
        NORTH = PipeShape.As.integer(n);
        SOUTH = PipeShape.As.integer(s);
        EAST = PipeShape.As.integer(e);
        WEST = PipeShape.As.integer(w);
        UP = PipeShape.As.integer(u);
        DOWN = PipeShape.As.integer(d);
    }

    public void setPipeStates(World world, BlockPos pos) {
        if (isUnlocked(NORTH)) {
            NORTH = world.getBlockState(pos.north()).isIn(ModTags.Blocks.MASH_PIPE_CONNECT_TO) ? 2 : 0;
        }
        if (isUnlocked(SOUTH)) {
            SOUTH = world.getBlockState(pos.south()).isIn(ModTags.Blocks.MASH_PIPE_CONNECT_TO) ? 2 : 0;
        }
        if (isUnlocked(EAST)) {
            EAST = world.getBlockState(pos.east()).isIn(ModTags.Blocks.MASH_PIPE_CONNECT_TO) ? 2 : 0;
        }
        if (isUnlocked(WEST)) {
            WEST = world.getBlockState(pos.west()).isIn(ModTags.Blocks.MASH_PIPE_CONNECT_TO) ? 2 : 0;
        }
        if (isUnlocked(UP)) {
            UP = world.getBlockState(pos.up()).isIn(ModTags.Blocks.MASH_PIPE_CONNECT_TO) ? 2 : 0;
        }
        if (isUnlocked(DOWN)) {
            DOWN = world.getBlockState(pos.down()).isIn(ModTags.Blocks.MASH_PIPE_CONNECT_TO) ? 2 : 0;
        }
        BlockState state = world.getBlockState(pos);
        updateSides(state, world, pos);
    }

    public ArrayList<Direction> getConnectedDirections(AbstractPipeBlockEntity blockEntity) {
        ArrayList<Direction> output = new ArrayList<>();
        if (isConnected(blockEntity.NORTH)) output.add(Direction.NORTH);
        if (isConnected(blockEntity.SOUTH)) output.add(Direction.SOUTH);
        if (isConnected(blockEntity.EAST)) output.add(Direction.EAST);
        if (isConnected(blockEntity.WEST)) output.add(Direction.WEST);
        if (isConnected(blockEntity.UP)) output.add(Direction.UP);
        if (isConnected(blockEntity.DOWN)) output.add(Direction.DOWN);
        return output;
    }

    public List<Direction> getOutputDirections(AbstractPipeBlockEntity blockEntity) {
        List<Direction> output = new ArrayList<>();
        if (isOutput(blockEntity.NORTH)) output.add(Direction.NORTH);
        if (isOutput(blockEntity.SOUTH)) output.add(Direction.SOUTH);
        if (isOutput(blockEntity.EAST)) output.add(Direction.EAST);
        if (isOutput(blockEntity.WEST)) output.add(Direction.WEST);
        if (isOutput(blockEntity.UP)) output.add(Direction.UP);
        if (isOutput(blockEntity.DOWN)) output.add(Direction.DOWN);
        return output;
    }

    public ArrayList<Direction> getNonOutputConnectedDirections(AbstractPipeBlockEntity blockEntity) {
        ArrayList<Direction> output = new ArrayList<>();
        if (!isOutput(blockEntity.NORTH) && isConnected(blockEntity.NORTH)) output.add(Direction.NORTH);
        if (!isOutput(blockEntity.SOUTH) && isConnected(blockEntity.SOUTH)) output.add(Direction.SOUTH);
        if (!isOutput(blockEntity.EAST) && isConnected(blockEntity.EAST)) output.add(Direction.EAST);
        if (!isOutput(blockEntity.WEST) && isConnected(blockEntity.WEST)) output.add(Direction.WEST);
        if (!isOutput(blockEntity.UP) && isConnected(blockEntity.UP)) output.add(Direction.UP);
        if (!isOutput(blockEntity.DOWN) && isConnected(blockEntity.DOWN)) output.add(Direction.DOWN);
        return output;
    }

    public static boolean isUnlocked(int value) {
        boolean isit = value != 1 && value != 3 && value != 5;
        GiantPotato.LOGGER.info("{}", isit);
        return isit;
    }
    public static boolean isConnected(int value) {
        return value >= 2 && value <= 5;
    }

    public void setSide(BlockState state, Direction side, PipeShape.PipeShapes value) {
        World world = getWorld();
        if(world == null) return;
        BlockPos pos = getPos();

        int valueInt = PipeShape.As.integer(value);
        switch (side) {
            case Direction.NORTH -> this.NORTH = valueInt;
            case Direction.SOUTH -> this.SOUTH = valueInt;
            case Direction.EAST -> this.EAST = valueInt;
            case Direction.WEST -> this.WEST = valueInt;
            case Direction.UP -> this.UP = valueInt;
            case Direction.DOWN -> this.DOWN = valueInt;
        }
        markDirty();
        updateSides(state, world, pos);
    }

    public void setSide(BlockState state, Direction side, int valueInt) {
        World world = getWorld();
        if(world == null) return;
        BlockPos pos = getPos();

        switch (side) {
            case Direction.NORTH -> this.NORTH = valueInt;
            case Direction.SOUTH -> this.SOUTH = valueInt;
            case Direction.EAST -> this.EAST = valueInt;
            case Direction.WEST -> this.WEST = valueInt;
            case Direction.UP -> this.UP = valueInt;
            case Direction.DOWN -> this.DOWN = valueInt;
        }
        markDirty();
        updateSides(state, world, pos);
    }

    public boolean isOutput(int valueInt) {
        return valueInt == 4 || valueInt == 5;
    }

    public void updateSides(BlockState state, World world, BlockPos pos) {
        boolean isOutput = isOutput(NORTH) || isOutput(SOUTH) || isOutput(EAST) || isOutput(WEST) || isOutput(UP) || isOutput(DOWN);
        world.setBlockState(pos, isOutput ? output.getDefaultState()
                .with(ModProperties.NORTH_PIPE_SHAPE, PipeShape.As.pipeShapes(NORTH))
                .with(ModProperties.SOUTH_PIPE_SHAPE, PipeShape.As.pipeShapes(SOUTH))
                .with(ModProperties.EAST_PIPE_SHAPE, PipeShape.As.pipeShapes(EAST))
                .with(ModProperties.WEST_PIPE_SHAPE, PipeShape.As.pipeShapes(WEST))
                .with(ModProperties.UP_PIPE_SHAPE, PipeShape.As.pipeShapes(UP))
                .with(ModProperties.DOWN_PIPE_SHAPE, PipeShape.As.pipeShapes(DOWN))
                :
                notOutput.getDefaultState()
                .with(ModProperties.NORTH_PIPE_SHAPE, PipeShape.As.pipeShapes(NORTH))
                .with(ModProperties.SOUTH_PIPE_SHAPE, PipeShape.As.pipeShapes(SOUTH))
                .with(ModProperties.EAST_PIPE_SHAPE, PipeShape.As.pipeShapes(EAST))
                .with(ModProperties.WEST_PIPE_SHAPE, PipeShape.As.pipeShapes(WEST))
                .with(ModProperties.UP_PIPE_SHAPE, PipeShape.As.pipeShapes(UP))
                .with(ModProperties.DOWN_PIPE_SHAPE, PipeShape.As.pipeShapes(DOWN))
        );
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        this.NORTH = nbt.getInt("north");
        this.SOUTH = nbt.getInt("south");
        this.EAST = nbt.getInt("east");
        this.WEST = nbt.getInt("west");
        this.UP = nbt.getInt("up");
        this.DOWN = nbt.getInt("down");
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt("north", this.NORTH);
        nbt.putInt("south", this.SOUTH);
        nbt.putInt("east", this.EAST);
        nbt.putInt("west", this.WEST);
        nbt.putInt("up", this.UP);
        nbt.putInt("down", this.DOWN);
    }
}
