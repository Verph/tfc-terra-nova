package tfcterranova.world.surface.builder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.function.Function;

import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.world.noise.Noise2D;
import net.dries007.tfc.world.noise.OpenSimplex2D;
import net.dries007.tfc.world.surface.SurfaceBuilderContext;
import net.dries007.tfc.world.surface.SurfaceState;
import net.dries007.tfc.world.surface.SurfaceStates;
import net.dries007.tfc.world.surface.builder.NormalSurfaceBuilder;
import net.dries007.tfc.world.surface.builder.SurfaceBuilder;
import net.dries007.tfc.world.surface.builder.SurfaceBuilderFactory;

import tfcterranova.common.blocks.TerraNovaBlocks;
import tfcterranova.common.blocks.soil.Colors;
import tfcterranova.common.blocks.soil.TNSandstoneBlockType;
import tfcterranova.common.blocks.soil.TerraNovaSoil;
import tfcterranova.world.surface.TerraNovaSoilSurfaceState;

import static net.dries007.tfc.world.TFCChunkGenerator.SEA_LEVEL_Y;

public class TerraNovaBadlandsSurfaceBuilder implements SurfaceBuilder
{
    public static final SurfaceBuilderFactory NORMAL = seed -> new TerraNovaBadlandsSurfaceBuilder(false, seed);
    public static final SurfaceBuilderFactory INVERTED = seed -> new TerraNovaBadlandsSurfaceBuilder(true, seed);

    private static final int PRIMARY_SIZE = 8;
    private static final int SECONDARY_SIZE = 5;
    private static final int UNCOMMON_SIZE = 3;
    private static final int LAYER_SIZE = PRIMARY_SIZE + SECONDARY_SIZE + UNCOMMON_SIZE; // 16

    private static void fill(Random random, BlockState[] sandLayers, BlockState[] sandstoneLayers, Colors primary, Colors secondary, Colors uncommon)
    {
        fill(random, sandLayers, primary, secondary, uncommon, TerraNovaBadlandsSurfaceBuilder::sand);
        fill(random, sandstoneLayers, primary, secondary, uncommon, TerraNovaBadlandsSurfaceBuilder::sandstone);
    }

    private static void fill(Random random, BlockState[] layers, Colors primary, Colors secondary, Colors uncommon, Function<Colors, BlockState> block)
    {
        // 8 - 5 - 3 of primary, secondary, and uncommon across the 16 block sequence.
        // This should make it near impossible to get completely 'undesirable' combinations
        Arrays.fill(layers, 0, PRIMARY_SIZE, block.apply(primary));
        Arrays.fill(layers, PRIMARY_SIZE, PRIMARY_SIZE + SECONDARY_SIZE, block.apply(secondary));
        Arrays.fill(layers, PRIMARY_SIZE + SECONDARY_SIZE, LAYER_SIZE, block.apply(uncommon));
        Collections.shuffle(Arrays.asList(layers), random);
    }

    private static BlockState sand(Colors color)
    {
        if (color.hasSandTFC())
        {
            return TFCBlocks.SAND.get(color.toSandTFC(true)).get().defaultBlockState();
        }
        else
        {
            return TerraNovaBlocks.SAND.get(color).get().defaultBlockState();
        }
    }

    private static BlockState sandstone(Colors color)
    {
        return TerraNovaBlocks.SANDSTONE.get(color.hasLayered() ? color : Colors.ORANGE).get(TNSandstoneBlockType.LAYERED).get().defaultBlockState();
    }

    private final boolean inverted;
    private final BlockState[] sandLayers0, sandLayers1;
    private final BlockState[] sandstoneLayers0, sandstoneLayers1;
    private final float[] layerThresholds;

    private final Noise2D grassHeightVariationNoise;
    private final Noise2D sandHeightOffsetNoise;
    private final Noise2D sandStyleNoise;
    private final Noise2D surfaceMaterialNoise;
    private final Noise2D variantNoise;

    public TerraNovaBadlandsSurfaceBuilder(boolean inverted, long seed)
    {
        this.inverted = inverted;

        final Random random = new Random(seed);

        sandLayers0 = new BlockState[LAYER_SIZE];
        sandLayers1 = new BlockState[LAYER_SIZE];

        sandstoneLayers0 = new BlockState[LAYER_SIZE];
        sandstoneLayers1 = new BlockState[LAYER_SIZE];

        layerThresholds = new float[LAYER_SIZE];

        for (int i = 0; i < LAYER_SIZE; i++)
        {
            layerThresholds[i] = random.nextFloat();
        }

        grassHeightVariationNoise = new OpenSimplex2D(random.nextLong()).octaves(2).scaled(SEA_LEVEL_Y + 14, SEA_LEVEL_Y + 18).spread(0.5f);
        sandHeightOffsetNoise = new OpenSimplex2D(random.nextLong()).octaves(2).scaled(0, 6).spread(0.0014f);
        sandStyleNoise = new OpenSimplex2D(random.nextLong()).octaves(2).scaled(-0.3f, 1.3f).spread(0.0003f);
        surfaceMaterialNoise = new OpenSimplex2D(seed + 3).octaves(2).spread(0.04f);
        variantNoise = new OpenSimplex2D(seed).octaves(2).spread(0.003f).abs();
    }

    @Override
    public void buildSurface(SurfaceBuilderContext context, int startY, int endY)
    {
        final Random random = new Random(context.getSeed());
        final double noiseGrass = surfaceMaterialNoise.noise(context.pos().getX(), context.pos().getZ()) * 0.9f + context.random().nextFloat() * 0.1f;
        final SurfaceState SPARSE_GRASS = TerraNovaSoilSurfaceState.buildType(TerraNovaSoil.SPARSE_GRASS);
        final SurfaceState DENSE_GRASS = TerraNovaSoilSurfaceState.buildType(TerraNovaSoil.DENSE_GRASS);
        final SurfaceState COMPACT_DIRT = TerraNovaSoilSurfaceState.buildType(TerraNovaSoil.COMPACT_DIRT);
        SurfaceState grass = SurfaceStates.GRASS;

        final double randomGauss = Math.abs(context.random().nextGaussian()) * 0.1f;
        final int randomChance = context.random().nextInt(7);

        if (noiseGrass >= -0.3f + randomGauss && noiseGrass < -0.1f + randomGauss)
        {
            if (randomChance > 3)
                grass = SPARSE_GRASS;
            else
                grass = COMPACT_DIRT;
        }
        else if (noiseGrass >= -0.1f + randomGauss)
        {
            grass = DENSE_GRASS;
        }

        double variantNoiseValue = variantNoise.noise(context.pos().getX(), context.pos().getZ());
        if (variantNoiseValue > 0f)
        {
            if (context.rainfall() > 300f && context.averageTemperature() > 15f)
            {
                fill(random, sandLayers0, sandstoneLayers0, Colors.GREEN, Colors.WHITE, Colors.YELLOW);
                fill(random, sandLayers1, sandstoneLayers1, Colors.GREEN, Colors.YELLOW, Colors.LIGHT_GREEN);
            }
            else if (context.rainfall() > 200f && context.getChunkData().getForestDensity() < 0.5f)
            {
                fill(random, sandLayers0, sandstoneLayers0, Colors.RED, Colors.YELLOW, Colors.WHITE);
                fill(random, sandLayers1, sandstoneLayers1, Colors.RED, Colors.ORANGE, Colors.YELLOW);
            }
            else if (context.rainfall() > 100f && context.getChunkData().getForestDensity() > 0.5f && context.averageTemperature() < 15f)
            {
                fill(random, sandLayers0, sandstoneLayers0, Colors.BLACK, Colors.WHITE, Colors.RED);
                fill(random, sandLayers1, sandstoneLayers1, Colors.GRAY, Colors.BLACK, Colors.WHITE);
            }
            else
            {
                fill(random, sandLayers0, sandstoneLayers0, Colors.PINK, Colors.RED, Colors.WHITE);
                fill(random, sandLayers1, sandstoneLayers1, Colors.PURPLE, Colors.PINK, Colors.BLUE);
            }
        }
        else
        {
            fill(random, sandLayers0, sandstoneLayers0, Colors.RED, Colors.BROWN, Colors.YELLOW);
            fill(random, sandLayers1, sandstoneLayers1, Colors.BROWN, Colors.YELLOW, Colors.WHITE);
        }

        final double heightVariation = grassHeightVariationNoise.noise(context.pos().getX(), context.pos().getZ());
        final float weightVariation = (float) (1f - context.weight()) * 23f;
        if (inverted ? startY + 5 < heightVariation + weightVariation : startY - 5 > heightVariation - weightVariation)
        {
            NormalSurfaceBuilder.INSTANCE.buildSurface(context, startY, endY, grass, SurfaceStates.DIRT, SurfaceStates.SANDSTONE_OR_GRAVEL);
        }
        else
        {
            if (startY < 81 + (context.random().nextGaussian() * 4))
            {
                NormalSurfaceBuilder.INSTANCE.buildSurface(context, startY, endY, grass, SurfaceStates.DIRT, SurfaceStates.SANDSTONE_OR_GRAVEL);
            }
            else
            {
                buildSandySurface(context, startY, endY);
            }
        }
    }

    private void buildSandySurface(SurfaceBuilderContext context, int startHeight, int minSurfaceHeight)
    {
        final double style = sandStyleNoise.noise(context.pos().getX(), context.pos().getZ());
        final int height = (int) sandHeightOffsetNoise.noise(context.pos().getX(), context.pos().getZ());

        int surfaceDepth = -1;
        int randomDepth = (int) context.random().nextGaussian() * 4;
        for (int y = startHeight; y >= (minSurfaceHeight - 15) + randomDepth; --y)
        {
            BlockState stateAt = context.getBlockState(y);
            if (stateAt.isAir())
            {
                surfaceDepth = -1; // Reached air, reset surface depth
            }
            else if (context.isDefaultBlock(stateAt))
            {
                if (surfaceDepth == -1)
                {
                    // Reached surface. Place top state and switch to subsurface layers
                    if (y < context.getSeaLevel() - 1)
                    {
                        context.setBlockState(y, SurfaceStates.SAND_OR_GRAVEL);
                    }
                    else
                    {
                        context.setBlockState(y, sampleLayer(sandLayers0, sandLayers1, y + height, style));
                        surfaceDepth = inverted ? 9 : 3;
                    }
                }
                else if (surfaceDepth > 0)
                {
                    // Subsurface layers
                    surfaceDepth--;
                    context.setBlockState(y, sampleLayer(sandstoneLayers0, sandstoneLayers1, y + height, style));
                }
            }
        }
    }

    private BlockState sampleLayer(BlockState[] layers0, BlockState[] layers1, int y, double threshold)
    {
        final int index = Math.floorMod(y, LAYER_SIZE);
        return (layerThresholds[index] < threshold ? layers0 : layers1)[index];
    }
}