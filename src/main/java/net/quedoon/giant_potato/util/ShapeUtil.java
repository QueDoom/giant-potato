package net.quedoon.giant_potato.util;


import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapeUtil {
    public static AABB getBoxFromVoxelCoordinates(Vec3 start, Vec3 end) {
        Vec3 localSpaceStart = new Vec3(start.x / 16, start.y / 16, start.z / 16);
        Vec3 localSpaceEnd = new Vec3(end.x / 16, end.y / 16, end.z / 16);
        return new AABB(localSpaceStart, localSpaceEnd);
    }

    public static Vec3 rotatePoint(Vec3 point, Direction facing) {
        Vec3 result = point.subtract(0.5, 0.5, 0.5);
        double x;
        double z;

        switch (facing) {
            case SOUTH -> {
                x = -result.x;
                z = -result.z;
            }
            case WEST -> {
                x = result.z;
                z = -result.x;
            }
            case EAST -> {
                x = -result.z;
                z = result.x;
            }
            default -> {
                x = result.x;
                z = result.z;
            }
        }
        return new Vec3(x, result.y, z).add(0.5, 0.5, 0.5f);
    }

    public static AABB rotateBox(AABB box, Direction facing) {
        Vec3 start = rotatePoint(new Vec3(box.minX, box.minY, box.minZ), facing);
        Vec3 end = rotatePoint(new Vec3(box.maxX, box.maxY, box.maxZ), facing);
        return new AABB(start, end);
    }

    public static VoxelShape createRotatedShape(AABB box, Direction direction) {
        return switch (direction) {
            case NORTH -> Block.box(
                    box.minX, box.minY, box.minZ,
                    box.maxX, box.maxY, box.maxZ
            );
            case SOUTH -> Block.box(
                    16 - box.maxX, box.minY, 16 - box.maxZ,
                    16 - box.minX, box.maxY, 16 - box.minZ
            );
            case WEST -> Block.box(
                    box.minZ, box.minY, 16 - box.maxX,
                    box.maxZ, box.maxY, 16 - box.minX
            );
            case EAST -> Block.box(
                    16 - box.maxZ, box.minY, box.minX,
                    16 - box.minZ, box.maxY, box.maxX
            );
            default -> Shapes.block();
        };
    }
}