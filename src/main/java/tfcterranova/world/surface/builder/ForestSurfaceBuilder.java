package tfcterranova.world.surface.builder;

import net.minecraft.core.BlockPos;

import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ForestType;
import net.dries007.tfc.world.noise.Noise2D;
import net.dries007.tfc.world.noise.OpenSimplex2D;
import net.dries007.tfc.world.surface.SoilSurfaceState;
import net.dries007.tfc.world.surface.SurfaceBuilderContext;
import net.dries007.tfc.world.surface.SurfaceState;
import net.dries007.tfc.world.surface.builder.*;

import tfcterranova.common.blocks.soil.RockSoil;
import tfcterranova.common.blocks.soil.TerraNovaSoil;
import tfcterranova.world.surface.TerraNovaSoilSurfaceState;

public class ForestSurfaceBuilder implements SurfaceBuilder
{
    public static SurfaceBuilderFactory create(SurfaceBuilderFactory parent)
    {
        return seed -> new ForestSurfaceBuilder(parent.apply(seed), seed);
    }

    public final SurfaceBuilder parent;
    public final Noise2D surfaceMaterialNoise;

    public ForestSurfaceBuilder(SurfaceBuilder parent, long seed)
    {
        this.parent = parent;
        this.surfaceMaterialNoise = new OpenSimplex2D(seed + 3).octaves(2).spread(0.04f);
    }

    @Override
    public void buildSurface(SurfaceBuilderContext context, int startY, int endY)
    {
        parent.buildSurface(context, startY, endY);

        float noiseRainfall = context.rainfall() + (10 * (float) context.random().nextGaussian());
        if (noiseRainfall >= 80f)
        {
            buildForestSurface(context, startY, endY);
        }
    }

    public void buildForestSurface(SurfaceBuilderContext context, int startY, int endY)
    {
        int topBlock = startY - 1;
        ChunkData data = context.getChunkData();
        BlockPos pos = new BlockPos(context.pos().getX(), topBlock, context.pos().getZ());
        final ForestType forestType = context.getChunkData().getForestType();
        final float forestDensity = data.getForestDensity();

        SurfaceState PEBBLE_COMPACT_DIRT = TerraNovaSoilSurfaceState.buildTypeRock(RockSoil.PEBBLE_COMPACT_DIRT);
        SurfaceState ROCKY_COMPACT_DIRT = TerraNovaSoilSurfaceState.buildTypeRock(RockSoil.ROCKY_COMPACT_DIRT);
        SurfaceState ROCKIER_COMPACT_DIRT = TerraNovaSoilSurfaceState.buildTypeRock(RockSoil.ROCKIER_COMPACT_DIRT);

        final SurfaceState PODZOL = TerraNovaSoilSurfaceState.buildType(TerraNovaSoil.PODZOL);
        final SurfaceState ROOTED_DIRT = SoilSurfaceState.buildType(SoilBlockType.ROOTED_DIRT);
        final SurfaceState SPARSE_GRASS = TerraNovaSoilSurfaceState.buildType(TerraNovaSoil.SPARSE_GRASS);
        final SurfaceState DENSE_GRASS = TerraNovaSoilSurfaceState.buildType(TerraNovaSoil.DENSE_GRASS);
        final SurfaceState COMPACT_DIRT = TerraNovaSoilSurfaceState.buildType(TerraNovaSoil.COMPACT_DIRT);

        final double randomGauss = Math.abs(context.random().nextGaussian()) * 0.1f;
        final double noise = surfaceMaterialNoise.noise(context.pos().getX(), context.pos().getZ()) * 0.9f + context.random().nextFloat() * 0.1f;

        if (pos.getY() > context.getSeaLevel())
        {
            if (forestDensity >= 0.3f && topBlock <= 110 && (forestType == ForestType.NORMAL || forestType == ForestType.OLD_GROWTH))
            {
                final int random = context.random().nextInt(7);
                if (noise >= -0.22f + randomGauss && noise < -0.13f + randomGauss)
                {
                    context.setBlockState(topBlock, DENSE_GRASS);
                }
                else if (noise >= -0.13f + randomGauss && noise < 0f + randomGauss)
                {
                    if (random > 3)
                        context.setBlockState(topBlock, SPARSE_GRASS);
                    else
                        context.setBlockState(topBlock, COMPACT_DIRT);
                }
                else if (noise >= 0f + randomGauss && noise < 0.1f + randomGauss)
                {
                    if (random > 4)
                        context.setBlockState(topBlock, DENSE_GRASS);
                    else
                        context.setBlockState(topBlock, PEBBLE_COMPACT_DIRT);
                }
                else if (noise >= 0.1f + randomGauss && noise < 0.2f + randomGauss)
                {
                    if (random > 4)
                        context.setBlockState(topBlock, DENSE_GRASS);
                    else
                        context.setBlockState(topBlock, ROCKY_COMPACT_DIRT);
                }
                else if (noise >= 0.2f + randomGauss && noise < 0.35f + randomGauss)
                {
                    if (random > 4)
                        context.setBlockState(topBlock, ROCKY_COMPACT_DIRT);
                    else
                        context.setBlockState(topBlock, ROCKIER_COMPACT_DIRT);
                }
                else if (noise >= 0.35f + randomGauss && noise < 0.55f + randomGauss)
                {
                    if (random > 4)
                        context.setBlockState(topBlock, DENSE_GRASS);
                    else
                        context.setBlockState(topBlock, ROOTED_DIRT);
                }
                else if (noise >= 0.55f + randomGauss && noise < 0.75f + randomGauss)
                {
                    context.setBlockState(topBlock, PODZOL);
                }
                else if (noise >= 0.75f + randomGauss)
                {
                    if (random > 3)
                        context.setBlockState(topBlock, PODZOL);
                    else
                        context.setBlockState(topBlock, ROOTED_DIRT);
                }
            }
        }
    }
}
