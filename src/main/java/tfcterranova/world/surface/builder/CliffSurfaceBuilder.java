package tfcterranova.world.surface.builder;

import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.world.noise.Noise2D;
import net.dries007.tfc.world.noise.OpenSimplex2D;
import net.dries007.tfc.world.surface.SurfaceBuilderContext;
import net.dries007.tfc.world.surface.SurfaceState;
import net.dries007.tfc.world.surface.SurfaceStates;
import net.dries007.tfc.world.surface.builder.*;

import tfcterranova.common.blocks.rock.TerraNovaRock;
import tfcterranova.world.surface.TerraNovaSoilSurfaceState;

public class CliffSurfaceBuilder implements SurfaceBuilder
{
    public static SurfaceBuilderFactory create(SurfaceBuilderFactory parent)
    {
        return seed -> new CliffSurfaceBuilder(parent.apply(seed), seed);
    }

    private final SurfaceBuilder parent;
    private final Noise2D jitterNoise;

    public CliffSurfaceBuilder(SurfaceBuilder parent, long seed)
    {
        this.parent = parent;
        jitterNoise = new OpenSimplex2D(seed + 1234123L).octaves(4).scaled(-1f, 1f).spread(0.3f);
    }

    @Override
    public void buildSurface(SurfaceBuilderContext context, int startY, int endY)
    {
        parent.buildSurface(context, startY, endY);
        buildCliffSurface(context, startY, endY);
    }

    private void buildCliffSurface(SurfaceBuilderContext context, int startY, int endY)
    {
        final NormalSurfaceBuilder surfaceBuilder = NormalSurfaceBuilder.INSTANCE;
        final double jitterNoise = this.jitterNoise.noise(context.pos().getX(), context.pos().getZ());

        SurfaceState rockType = TerraNovaSoilSurfaceState.rockCustom(Rock.CHERT, Rock.BlockType.RAW); // Fallback
        if (jitterNoise > 0.85f)
        {
            rockType = TerraNovaSoilSurfaceState.rockCustom(Rock.CLAYSTONE, Rock.BlockType.RAW);
        }
        else if (jitterNoise > 0.7f && jitterNoise <= 0.85f)
        {
            rockType = TerraNovaSoilSurfaceState.rockCustom(Rock.LIMESTONE, Rock.BlockType.RAW);
        }
        else if (jitterNoise > 0.55f && jitterNoise <= 0.7f)
        {
            rockType = TerraNovaSoilSurfaceState.rockCustom(Rock.CHERT, Rock.BlockType.RAW);
        }
        else if (jitterNoise > 0.4f && jitterNoise <= 0.55f)
        {
            rockType = TerraNovaSoilSurfaceState.rockCustom(Rock.CHALK, Rock.BlockType.RAW);
        }
        else if (jitterNoise > 0.25f && jitterNoise <= 0.4f)
        {
            rockType = TerraNovaSoilSurfaceState.rockCustom(Rock.MARBLE, Rock.BlockType.RAW);
        }
        else if (jitterNoise > 0.1f && jitterNoise <= 0.25f)
        {
            rockType = TerraNovaSoilSurfaceState.rockCustom(TerraNovaRock.CATLINITE, Rock.BlockType.RAW);
        }
        else if (jitterNoise > -0.05f && jitterNoise <= 0.1f)
        {
            rockType = TerraNovaSoilSurfaceState.rockCustom(TerraNovaRock.MUDSTONE, Rock.BlockType.RAW);
        }
        else if (jitterNoise > -0.2f && jitterNoise <= -0.05f)
        {
            rockType = TerraNovaSoilSurfaceState.rockCustom(TerraNovaRock.SANDSTONE, Rock.BlockType.RAW);
        }
        else if (jitterNoise > -0.35f && jitterNoise <= -0.2f)
        {
            rockType = TerraNovaSoilSurfaceState.rockCustom(TerraNovaRock.SILTSTONE, Rock.BlockType.RAW);
        }
        else if (jitterNoise > -0.5f && jitterNoise <= -0.35f)
        {
            rockType = TerraNovaSoilSurfaceState.rockCustom(TerraNovaRock.SOAPSTONE, Rock.BlockType.RAW);
        }
        else if (jitterNoise > -0.65f && jitterNoise <= -0.5f)
        {
            rockType = TerraNovaSoilSurfaceState.rockCustom(TerraNovaRock.TRAVERTINE, Rock.BlockType.RAW);
        }
        else if (jitterNoise > -0.8f && jitterNoise <= -0.65f)
        {
            rockType = TerraNovaSoilSurfaceState.rockCustom(Rock.GRANITE, Rock.BlockType.RAW);
        }
        else
        {
            rockType = TerraNovaSoilSurfaceState.rockCustom(Rock.CHALK, Rock.BlockType.RAW);
        }
        surfaceBuilder.buildSurface(context, startY, endY, rockType, rockType, rockType);
        context.setBlockState(startY, SurfaceStates.GRASS);
    }
}