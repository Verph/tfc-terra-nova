package tfcterranova.world.surface.builder;

import net.minecraft.core.BlockPos;

import net.dries007.tfc.world.noise.Noise2D;
import net.dries007.tfc.world.noise.OpenSimplex2D;
import net.dries007.tfc.world.surface.SurfaceBuilderContext;
import net.dries007.tfc.world.surface.SurfaceState;
import net.dries007.tfc.world.surface.builder.*;

import tfcterranova.common.blocks.soil.TerraNovaSoil;
import tfcterranova.world.surface.TerraNovaSoilSurfaceState;

public class GrassSurfaceBuilder implements SurfaceBuilder
{
    public static SurfaceBuilderFactory create(SurfaceBuilderFactory parent)
    {
        return seed -> new GrassSurfaceBuilder(parent.apply(seed), seed);
    }

    private final SurfaceBuilder parent;
    private final Noise2D heightNoise;

    public GrassSurfaceBuilder(SurfaceBuilder parent, long seed)
    {
        this.parent = parent;
        heightNoise = new OpenSimplex2D(seed + 71829341L).octaves(2).spread(0.1f);
    }

    @Override
    public void buildSurface(SurfaceBuilderContext context, int startY, int endY)
    {
        parent.buildSurface(context, startY, endY);

        float noiseRainfall = context.rainfall() + (10 * (float) context.random().nextGaussian());
        if (noiseRainfall >= 80f)
        {
            buildSoilSurface(context, startY, endY);
        }
    }

    private void buildSoilSurface(SurfaceBuilderContext context, int startY, int endY)
    {
        int topBlock = startY - 1;
        BlockPos pos = new BlockPos(context.pos().getX(), topBlock, context.pos().getZ());
        final float rainfall = context.getChunkData().getRainfall(pos);

        final SurfaceState SPARSE_GRASS = TerraNovaSoilSurfaceState.buildType(TerraNovaSoil.SPARSE_GRASS);
        final SurfaceState DENSE_GRASS = TerraNovaSoilSurfaceState.buildType(TerraNovaSoil.DENSE_GRASS);

        final double gauss = context.random().nextGaussian();
        final double heightNoise = this.heightNoise.noise(context.pos().getX(), context.pos().getZ()) * 4f + startY;

        if (pos.getY() > context.getSeaLevel() && heightNoise <= 130)
        {
            if (rainfall < +1.5 * gauss + 100f)
            {
                if (rainfall >= +1.5 * gauss + 65f)
                {
                    context.setBlockState(topBlock, DENSE_GRASS);
                }
                else
                {
                    context.setBlockState(topBlock, SPARSE_GRASS);
                }
            }
        }
    }
}
