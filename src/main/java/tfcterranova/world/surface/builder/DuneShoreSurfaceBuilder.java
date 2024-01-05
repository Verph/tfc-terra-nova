package tfcterranova.world.surface.builder;

import net.minecraft.core.BlockPos;

import net.dries007.tfc.world.noise.Noise2D;
import net.dries007.tfc.world.noise.OpenSimplex2D;
import net.dries007.tfc.world.surface.SurfaceBuilderContext;
import net.dries007.tfc.world.surface.SurfaceState;
import net.dries007.tfc.world.surface.SurfaceStates;
import net.dries007.tfc.world.surface.builder.*;

import tfcterranova.common.blocks.soil.RockSand;
import tfcterranova.world.surface.TerraNovaSoilSurfaceState;

public class DuneShoreSurfaceBuilder implements SurfaceBuilder
{
    public static SurfaceBuilderFactory create(SurfaceBuilderFactory parent)
    {
        return seed -> new DuneShoreSurfaceBuilder(parent.apply(seed), seed);
    }

    private final SurfaceBuilder parent;
    private final Noise2D surfaceMaterialNoise;
    private final Noise2D variantNoise;

    public DuneShoreSurfaceBuilder(SurfaceBuilder parent, long seed)
    {
        this.parent = parent;
        this.surfaceMaterialNoise = new OpenSimplex2D(seed).octaves(2).spread(0.04f);
        this.variantNoise = new OpenSimplex2D(seed).octaves(2).spread(0.003f).abs();
    }

    @Override
    public void buildSurface(SurfaceBuilderContext context, int startY, int endY)
    {
        parent.buildSurface(context, startY, endY);

        float noiseRainfall = context.rainfall() + (10 * (float) context.random().nextGaussian());
        if (noiseRainfall >= 50f)
        {
            buildSandySurface(context, startY, endY);
        }
    }

    private void buildSandySurface(SurfaceBuilderContext context, int startY, int endY)
    {
        int surface = startY - 1;
        BlockPos pos = new BlockPos(context.pos().getX(), surface, context.pos().getZ());
        double variantNoiseValue = variantNoise.noise(context.pos().getX(), context.pos().getZ());

        SurfaceState PEBBLE = TerraNovaSoilSurfaceState.rockSand(RockSand.PEBBLE);
        SurfaceState ROCKY = TerraNovaSoilSurfaceState.rockSand(RockSand.ROCKY);
        SurfaceState ROCKIER = TerraNovaSoilSurfaceState.rockSand(RockSand.ROCKIER);
        SurfaceState ROCKIEST = TerraNovaSoilSurfaceState.rockSand(RockSand.ROCKIEST);
        SurfaceState SPARSE_GRASS = TerraNovaSoilSurfaceState.rockSandSparseGrass();
        SurfaceState DENSE_GRASS = TerraNovaSoilSurfaceState.rockSandDenseGrass();
        SurfaceState SHORE_SAND = SurfaceStates.SHORE_SAND;

        if (variantNoiseValue > 0.6f)
        {
            SHORE_SAND = SurfaceStates.RARE_SHORE_SAND;
            SPARSE_GRASS = TerraNovaSoilSurfaceState.rockRareSandSparseGrass();
            DENSE_GRASS = TerraNovaSoilSurfaceState.rockRareSandDenseGrass();
        }

        final double randomGauss = Math.abs(context.random().nextGaussian()) * 0.1f;
        final double gauss = context.random().nextGaussian();
        final double noise = surfaceMaterialNoise.noise(context.pos().getX(), context.pos().getZ()) * 0.9f + context.random().nextFloat() * 0.1f;

        if (pos.getY() > context.getSeaLevel() + 3 + gauss)
        {
            final int random = context.random().nextInt(7);
            if (noise >= -0.45f + randomGauss && noise < -0.2f + randomGauss)
            {
                context.setBlockState(surface, DENSE_GRASS);
            }
            else if (noise >= -0.2f + randomGauss && noise < -0.05f + randomGauss)
            {
                context.setBlockState(surface, DENSE_GRASS);
            }
            else if (noise >= -0.05f + randomGauss && random <= 0)
            {
                context.setBlockState(surface, SPARSE_GRASS);
            }
            else if (noise >= -0.05f + randomGauss && random == 1)
            {
                context.setBlockState(surface, SHORE_SAND);
            }
            else if (noise >= -0.05f + randomGauss && random == 2)
            {
                context.setBlockState(surface, PEBBLE);
            }
            else if (noise >= -0.05f + randomGauss && random == 3)
            {
                context.setBlockState(surface, ROCKY);
            }
            else if (noise >= -0.05f + randomGauss && random == 4)
            {
                context.setBlockState(surface, ROCKIER);
            }
            else if (noise >= -0.05f + randomGauss && random == 5)
            {
                context.setBlockState(surface, ROCKIEST);
            }
            else if (noise >= -0.05f + randomGauss && random == 6)
            {
                context.setBlockState(surface, SPARSE_GRASS);
            }
        }
    }
}