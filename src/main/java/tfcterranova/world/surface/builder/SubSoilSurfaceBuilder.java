package tfcterranova.world.surface.builder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.world.TFCChunkGenerator;
import net.dries007.tfc.world.surface.SurfaceBuilderContext;
import net.dries007.tfc.world.surface.SurfaceState;
import net.dries007.tfc.world.surface.builder.*;

import tfcterranova.common.blocks.soil.RockSand;
import tfcterranova.common.blocks.soil.RockSoil;
import tfcterranova.common.blocks.soil.TerraNovaSoil;
import tfcterranova.world.surface.TerraNovaSoilSurfaceState;

public class SubSoilSurfaceBuilder implements SurfaceBuilder
{
    public static final int SEA_LEVEL_Y = TFCChunkGenerator.SEA_LEVEL_Y; // Matches vanilla

    public static SurfaceBuilderFactory create(SurfaceBuilderFactory parent)
    {
        return seed -> new SubSoilSurfaceBuilder(parent.apply(seed), seed);
    }

    private final SurfaceBuilder parent;

    public SubSoilSurfaceBuilder(SurfaceBuilder parent, long seed)
    {
        this.parent = parent;
    }

    @Override
    public void buildSurface(SurfaceBuilderContext context, int startY, int endY)
    {
        parent.buildSurface(context, startY, endY);

        float noiseRainfall = context.rainfall() + (10 * (float) context.random().nextGaussian());
        if (noiseRainfall >= 80f)
        {
            buildSubSoil(context, startY, endY);
        }
        else
        {
            buildSubSand(context, startY, endY);
        }
    }

    private void buildSubSoil(SurfaceBuilderContext context, int startY, int endY)
    {
        BlockPos pos = new BlockPos(context.pos().getX(), startY - 1, context.pos().getZ());

        SurfaceState PEBBLE_COMPACT_DIRT = TerraNovaSoilSurfaceState.buildTypeRock(RockSoil.PEBBLE_COMPACT_DIRT);
        SurfaceState ROCKY_COMPACT_DIRT = TerraNovaSoilSurfaceState.buildTypeRock(RockSoil.ROCKY_COMPACT_DIRT);
        SurfaceState ROCKIER_COMPACT_DIRT = TerraNovaSoilSurfaceState.buildTypeRock(RockSoil.ROCKIER_COMPACT_DIRT);
        SurfaceState ROCKIEST_COMPACT_DIRT = TerraNovaSoilSurfaceState.buildTypeRock(RockSoil.ROCKIEST_COMPACT_DIRT);
        SurfaceState COMPACT_DIRT = TerraNovaSoilSurfaceState.buildType(TerraNovaSoil.COMPACT_DIRT);

        BlockState localGravelType = TerraNovaSoilSurfaceState.rock(Rock.BlockType.GRAVEL).getState(context);
        BlockState localDirtSoilVariant = TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(TerraNovaSoilSurfaceState.currentSoilVariant(context)).get().defaultBlockState();

        int grassY = startY - 1;
        int gravelY = -1;
        int transitionHeight = -1;

        for (int y = startY - 2; y >= endY; --y)
        {
            final BlockState stateAt = context.getBlockState(y);
            if (y < startY - 2 && y > endY + 2 && !stateAt.isAir() && (stateAt == localGravelType || stateAt == localDirtSoilVariant))
            {
                gravelY = y - 1;
                transitionHeight = gravelY + 4;
                break;
            }
        }

        if (gravelY != -1)
        {
            if (transitionHeight >= grassY - 1) transitionHeight = grassY - 1;
            for (int y = gravelY; y < transitionHeight; ++y)
            {
                final double randomGauss = context.random().nextGaussian();

                if (pos.getY() > context.getSeaLevel())
                {
                    if (y == gravelY)
                    {
                        if (randomGauss >= -0.3f)
                        {
                            if (randomGauss >= 0.8f)
                            {
                                context.setBlockState(y, ROCKY_COMPACT_DIRT.getState(context));
                            }
                            else if (randomGauss >= 0.45f && randomGauss < 0.8f)
                            {
                                context.setBlockState(y, ROCKIER_COMPACT_DIRT.getState(context));
                            }
                            else
                            {
                                context.setBlockState(y, ROCKIEST_COMPACT_DIRT.getState(context));
                            }
                        }
                    }
                    else if (y == gravelY + 1)
                    {
                        if (randomGauss >= 0f)
                        {
                            if (randomGauss >= 0.7f)
                            {
                                context.setBlockState(y, ROCKY_COMPACT_DIRT.getState(context));
                            }
                            else if (randomGauss >= 0.45f && randomGauss < 0.7f)
                            {
                                context.setBlockState(y, ROCKIER_COMPACT_DIRT.getState(context));
                            }
                            else
                            {
                                context.setBlockState(y, ROCKIEST_COMPACT_DIRT.getState(context));
                            }
                        }
                    }
                    else if (y == gravelY + 2)
                    {
                        if (randomGauss >= 0.1f)
                        {
                            if (randomGauss >= 0.7f)
                            {
                                context.setBlockState(y, PEBBLE_COMPACT_DIRT.getState(context));
                            }
                            else if (randomGauss >= 0.4f && randomGauss < 0.7f)
                            {
                                context.setBlockState(y, ROCKY_COMPACT_DIRT.getState(context));
                            }
                            else
                            {
                                context.setBlockState(y, ROCKIER_COMPACT_DIRT.getState(context));
                            }
                        }
                    }
                    else if (y == gravelY + 3)
                    {
                        if (randomGauss >= 0.2f)
                        {
                            if (randomGauss >= 0.6f)
                            {
                                context.setBlockState(y, COMPACT_DIRT.getState(context));
                            }
                            else
                            {
                                context.setBlockState(y, PEBBLE_COMPACT_DIRT.getState(context));
                            }
                        }
                    }
                    else if (y > gravelY + 3)
                    {
                        if (0.3f >= randomGauss)
                        {
                            context.setBlockState(y, COMPACT_DIRT.getState(context));
                        }
                    }
                }
            }
        }
    }

    private void buildSubSand(SurfaceBuilderContext context, int startY, int endY)
    {
        BlockPos pos = new BlockPos(context.pos().getX(), startY - 1, context.pos().getZ());

        SurfaceState PEBBLE = TerraNovaSoilSurfaceState.buildTypeRockSand(RockSand.PEBBLE);
        SurfaceState ROCKY = TerraNovaSoilSurfaceState.buildTypeRockSand(RockSand.ROCKY);
        SurfaceState ROCKIER = TerraNovaSoilSurfaceState.buildTypeRockSand(RockSand.ROCKIER);
        SurfaceState ROCKIEST = TerraNovaSoilSurfaceState.buildTypeRockSand(RockSand.ROCKIEST);
        SurfaceState COMPACT_DIRT = TerraNovaSoilSurfaceState.buildType(TerraNovaSoil.COMPACT_DIRT);

        BlockState localGravelType = TerraNovaSoilSurfaceState.rock(Rock.BlockType.GRAVEL).getState(context);
        BlockState localDirtSoilVariant = TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(TerraNovaSoilSurfaceState.currentSoilVariant(context)).get().defaultBlockState();


        int grassY = startY - 1;
        int gravelY = -1;
        int transitionHeight = -1;

        for (int y = startY - 2; y >= endY; --y)
        {
            final BlockState stateAt = context.getBlockState(y);
            if (y < startY - 2 && y > endY + 2 && !stateAt.isAir() && (stateAt == localGravelType || stateAt == localDirtSoilVariant))
            {
                gravelY = y - 1;
                transitionHeight = gravelY + 4;
                break;
            }
        }

        if (gravelY != -1)
        {
            if (transitionHeight >= grassY - 1) transitionHeight = grassY - 1;
            for (int y = gravelY; y < transitionHeight; ++y)
            {
                final double randomGauss = context.random().nextGaussian();

                if (pos.getY() > context.getSeaLevel())
                {
                    if (y == gravelY)
                    {
                        if (randomGauss >= -0.3f)
                        {
                            if (randomGauss >= 0.8f)
                            {
                                context.setBlockState(y, ROCKY.getState(context));
                            }
                            else if (randomGauss >= 0.45f && randomGauss < 0.8f)
                            {
                                context.setBlockState(y, ROCKIER.getState(context));
                            }
                            else
                            {
                                context.setBlockState(y, ROCKIEST.getState(context));
                            }
                        }
                    }
                    else if (y == gravelY + 1)
                    {
                        if (randomGauss >= 0f)
                        {
                            if (randomGauss >= 0.7f)
                            {
                                context.setBlockState(y, ROCKY.getState(context));
                            }
                            else if (randomGauss >= 0.45f && randomGauss < 0.7f)
                            {
                                context.setBlockState(y, ROCKIER.getState(context));
                            }
                            else
                            {
                                context.setBlockState(y, ROCKIEST.getState(context));
                            }
                        }
                    }
                    else if (y == gravelY + 2)
                    {
                        if (randomGauss >= 0.1f)
                        {
                            if (randomGauss >= 0.7f)
                            {
                                context.setBlockState(y, PEBBLE.getState(context));
                            }
                            else if (randomGauss >= 0.4f && randomGauss < 0.7f)
                            {
                                context.setBlockState(y, ROCKY.getState(context));
                            }
                            else
                            {
                                context.setBlockState(y, ROCKIER.getState(context));
                            }
                        }
                    }
                    else if (y == gravelY + 3)
                    {
                        if (randomGauss >= 0.2f)
                        {
                            if (randomGauss >= 0.6f)
                            {
                                context.setBlockState(y, COMPACT_DIRT.getState(context));
                            }
                            else
                            {
                                context.setBlockState(y, PEBBLE.getState(context));
                            }
                        }
                    }
                    else if (y > gravelY + 3)
                    {
                        if (0.3f >= randomGauss)
                        {
                            context.setBlockState(y, COMPACT_DIRT.getState(context));
                        }
                    }
                }
            }
        }
    }
}