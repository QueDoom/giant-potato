package net.quedoon.giant_potato.block.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3d;

public class ModBlockHitboxes {
    public static VoxelShape getMashBowlHitbox() {
        return Shapes.or(
                  Block.box(0, 0, 0, 16, 8, 16)//,
        );
    }
    public static VoxelShape getCrusherWheelHitbox(Direction direction) {
        VoxelShape north = Shapes.or(Block.box(0, 0, 0, 16, 13, 3));
        VoxelShape south = Shapes.or(Block.box( 0, 0, 13,16, 13, 16));
        VoxelShape east = Shapes.or(Block.box(13, 0, 0, 16, 13, 16));
        VoxelShape west = Shapes.or(Block.box(0, 0, 0, 4, 13, 16));
        return switch (direction) {
            case Direction.SOUTH -> south;
            case Direction.EAST -> east;
            case Direction.WEST -> west;
            default -> north;
        };
    }
    public static VoxelShape getCrusherHitbox() {
        return Shapes.or(
                Block.box(0, 0, 0, 16, 13, 16)//,
        );
    }

    public static class Pipe {
        public static VoxelShape center() {
            return Block.box(5.5, 5.5, 5.5, 10.5, 10.5, 10.5);
        }
        public static VoxelShape north() {
            return Block.box(5.5, 5.5, 0, 10.5, 10.5, 5.5);
        }
        public static VoxelShape south() {
            return Block.box(5.5, 5.5, 10.5, 10.5, 10.5, 16);
        }
        public static VoxelShape east() {
            return Block.box(10.5, 5.5, 5.5, 16, 10.5, 10.5);
        }
        public static VoxelShape west() {
            return Block.box(0, 5.5, 5.5, 5.5, 10.5, 10.5);
        }
        public static VoxelShape up() {
            return Block.box(5.5, 10.5, 5.5, 10.5, 16, 10.5);
        }
        public static VoxelShape down() {
            return Block.box(5.5, 0, 5.5, 10.5, 5.5, 10.5);
        }



        public static VoxelShape getShape(BlockState state) {
            PipeShape.PipeShapes north = state.getValue(ModProperties.NORTH_PIPE_SHAPE);
            PipeShape.PipeShapes south = state.getValue(ModProperties.SOUTH_PIPE_SHAPE);
            PipeShape.PipeShapes east = state.getValue(ModProperties.EAST_PIPE_SHAPE);
            PipeShape.PipeShapes west = state.getValue(ModProperties.WEST_PIPE_SHAPE);
            PipeShape.PipeShapes up = state.getValue(ModProperties.UP_PIPE_SHAPE);
            PipeShape.PipeShapes down = state.getValue(ModProperties.DOWN_PIPE_SHAPE);
            VoxelShape empty = Shapes.empty();
            VoxelShape center = center();
            VoxelShape northShape = north != PipeShape.PipeShapes.NONE ? north() : empty;
            VoxelShape southShape = south != PipeShape.PipeShapes.NONE ? south() : empty;
            VoxelShape eastShape = east != PipeShape.PipeShapes.NONE ? east() : empty;
            VoxelShape westShape = west != PipeShape.PipeShapes.NONE ? west() : empty;
            VoxelShape upShape = up != PipeShape.PipeShapes.NONE ? up() : empty;
            VoxelShape downShape = down != PipeShape.PipeShapes.NONE ? down() : empty;
            return Shapes.or(center, northShape, southShape, eastShape, westShape, upShape, downShape);
        }
    }

    public static class BoxPipe {
        public static Vec3 dirFrom(Direction direction) {
            return switch (direction) {
                case DOWN -> downFrom();
                case UP -> upFrom();
                case NORTH -> northFrom();
                case SOUTH -> southFrom();
                case WEST -> westFrom();
                case EAST -> eastFrom();
            };
        }
        public static Vec3 dirTo(Direction direction) {
            return switch (direction) {
                case DOWN -> downTo();
                case UP -> upTo();
                case NORTH -> northTo();
                case SOUTH -> southTo();
                case WEST -> westTo();
                case EAST -> eastTo();
            };
        }
        public static Vec3 northFrom() {
            //return Block.createCuboidShape(5.5, 5.5, 0, 10.5, 10.5, 5.5);
            return new Vec3(5.5, 5.5, 0);
        }
        public static Vec3 northTo() {
            //return Block.createCuboidShape(5.5, 5.5, 0, 10.5, 10.5, 5.5);
            return new Vec3(10.5, 10.5, 5.5);
        }
        public static Vec3 southFrom() {
            //return Block.createCuboidShape(5.5, 5.5, 10.5, 10.5, 10.5, 16);
            return new Vec3(5.5, 5.5, 10.5);
        }
        public static Vec3 southTo() {
            //return Block.createCuboidShape(5.5, 5.5, 10.5, 10.5, 10.5, 16);
            return new Vec3(10.5, 10.5, 16);
        }
        public static Vec3 eastFrom() {
            //return Block.createCuboidShape(10.5, 5.5, 5.5, 16, 10.5, 10.5);
            return new Vec3(10.5, 5.5, 5.5);
        }
        public static Vec3 eastTo() {
            //return Block.createCuboidShape(10.5, 5.5, 5.5, 16, 10.5, 10.5);
            return new Vec3(16, 10.5, 10.5);
        }
        public static Vec3 westFrom() {
            //return Block.createCuboidShape(0, 5.5, 5.5, 5.5, 10.5, 10.5);
            return new Vec3(0, 5.5, 5.5);
        }
        public static Vec3 westTo() {
            //return Block.createCuboidShape(0, 5.5, 5.5, 5.5, 10.5, 10.5);
            return new Vec3(5.5, 10.5, 10.5);
        }
        public static Vec3 upFrom() {
            //return Block.createCuboidShape(5.5, 10.5, 5.5, 10.5, 16, 10.5);
            return new Vec3(5.5, 10.5, 5.5);
        }
        public static Vec3 upTo() {
            //return Block.createCuboidShape(5.5, 10.5, 5.5, 10.5, 16, 10.5);
            return new Vec3(10.5, 16, 10.5);
        }
        public static Vec3 downFrom() {
            //return Block.createCuboidShape(5.5, 0, 5.5, 10.5, 5.5, 10.5);
            return new Vec3(5.5, 0, 5.5);
        }
        public static Vec3 downTo() {
            //return Block.createCuboidShape(5.5, 0, 5.5, 10.5, 5.5, 10.5);
            return new Vec3(10.5, 5.5, 10.5);
        }
    }
}
