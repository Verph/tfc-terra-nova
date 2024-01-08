package tfcterranova.world.surface;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ChunkDataProvider;
import net.dries007.tfc.world.noise.OpenSimplex2D;
import net.dries007.tfc.world.settings.RockSettings;
import net.dries007.tfc.world.surface.SoilSurfaceState;
import net.dries007.tfc.world.surface.SurfaceBuilderContext;
import net.dries007.tfc.world.surface.SurfaceState;
import net.dries007.tfc.util.climate.KoppenClimateClassification;
import net.dries007.tfc.util.registry.RegistryRock;
import net.dries007.tfc.util.registry.RegistrySoilVariant;

import tfcterranova.common.blocks.TerraNovaBlocks;
import tfcterranova.common.blocks.rock.TerraNovaRock;
import tfcterranova.common.blocks.soil.*;
import tfcterranova.util.TNHelpers;

public class TerraNovaSoilSurfaceState implements SurfaceState
{
    public static SurfaceState buildType(SoilBlockType type)
    {
        final ImmutableList<SurfaceState> regions = ImmutableList.of(
            sand(),
            transition(sand(), soil(type, TerraNovaSoil.Variant.LOAMY_SAND)),
            soil(type, TerraNovaSoil.Variant.LOAMY_SAND),
            transition(soil(type, TerraNovaSoil.Variant.LOAMY_SAND), soil(type, SoilBlockType.Variant.SANDY_LOAM)),
            soil(type, SoilBlockType.Variant.SANDY_LOAM),
            transition(soil(type, SoilBlockType.Variant.SANDY_LOAM), soil(type, SoilBlockType.Variant.LOAM)),
            soil(type, SoilBlockType.Variant.LOAM),
            transition(soil(type, SoilBlockType.Variant.LOAM), soil(type, SoilBlockType.Variant.SILTY_LOAM)),
            soil(type, SoilBlockType.Variant.SILTY_LOAM),
            transition(soil(type, SoilBlockType.Variant.SILTY_LOAM), soil(type, SoilBlockType.Variant.SILT)),
            soil(type, SoilBlockType.Variant.SILT)
        );
        return new TerraNovaSoilSurfaceState(regions);
    }

    public static SurfaceState buildType(TerraNovaSoil type)
    {
        final ImmutableList<SurfaceState> regions = ImmutableList.of(
            sand(),
            transition(sand(), soil(type, TerraNovaSoil.Variant.LOAMY_SAND)),
            soil(type, TerraNovaSoil.Variant.LOAMY_SAND),
            transition(soil(type, TerraNovaSoil.Variant.LOAMY_SAND), soil(type, SoilBlockType.Variant.SANDY_LOAM)),
            soil(type, SoilBlockType.Variant.SANDY_LOAM),
            transition(soil(type, SoilBlockType.Variant.SANDY_LOAM), soil(type, SoilBlockType.Variant.LOAM)),
            soil(type, SoilBlockType.Variant.LOAM),
            transition(soil(type, SoilBlockType.Variant.LOAM), soil(type, SoilBlockType.Variant.SILTY_LOAM)),
            soil(type, SoilBlockType.Variant.SILTY_LOAM),
            transition(soil(type, SoilBlockType.Variant.SILTY_LOAM), soil(type, SoilBlockType.Variant.SILT)),
            soil(type, SoilBlockType.Variant.SILT)
        );
        return new TerraNovaSoilSurfaceState(regions);
    }

    public static SurfaceState buildTypeClay(SoilBlockType type)
    {
        final ImmutableList<SurfaceState> regions = ImmutableList.of(
            sand(),
            transition(sand(), soil(type, TerraNovaSoil.Variant.SANDY_CLAY)),
            soil(type, TerraNovaSoil.Variant.SANDY_CLAY),
            transition(soil(type, TerraNovaSoil.Variant.SANDY_CLAY), soil(type, TerraNovaSoil.Variant.SANDY_CLAY_LOAM)),
            soil(type, TerraNovaSoil.Variant.SANDY_CLAY_LOAM),
            transition(soil(type, TerraNovaSoil.Variant.SANDY_CLAY_LOAM), soil(type, TerraNovaSoil.Variant.CLAY_LOAM)),
            soil(type, TerraNovaSoil.Variant.CLAY_LOAM),
            transition(soil(type, TerraNovaSoil.Variant.CLAY_LOAM), soil(type, TerraNovaSoil.Variant.SILTY_CLAY_LOAM)),
            soil(type, TerraNovaSoil.Variant.SILTY_CLAY_LOAM),
            transition(soil(type, TerraNovaSoil.Variant.SILTY_CLAY_LOAM), soil(type, TerraNovaSoil.Variant.SILTY_CLAY)),
            soil(type, TerraNovaSoil.Variant.SILTY_CLAY)
        );
        return new TerraNovaSoilSurfaceState(regions);
    }

    public static SurfaceState buildTypeClay(TerraNovaSoil type)
    {
        final ImmutableList<SurfaceState> regions = ImmutableList.of(
            sand(),
            transition(sand(), soil(type, TerraNovaSoil.Variant.SANDY_CLAY)),
            soil(type, TerraNovaSoil.Variant.SANDY_CLAY),
            transition(soil(type, TerraNovaSoil.Variant.SANDY_CLAY), soil(type, TerraNovaSoil.Variant.SANDY_CLAY_LOAM)),
            soil(type, TerraNovaSoil.Variant.SANDY_CLAY_LOAM),
            transition(soil(type, TerraNovaSoil.Variant.SANDY_CLAY_LOAM), soil(type, TerraNovaSoil.Variant.CLAY_LOAM)),
            soil(type, TerraNovaSoil.Variant.CLAY_LOAM),
            transition(soil(type, TerraNovaSoil.Variant.CLAY_LOAM), soil(type, TerraNovaSoil.Variant.SILTY_CLAY_LOAM)),
            soil(type, TerraNovaSoil.Variant.SILTY_CLAY_LOAM),
            transition(soil(type, TerraNovaSoil.Variant.SILTY_CLAY_LOAM), soil(type, TerraNovaSoil.Variant.SILTY_CLAY)),
            soil(type, TerraNovaSoil.Variant.SILTY_CLAY)
        );
        return new TerraNovaSoilSurfaceState(regions);
    }

    public static SurfaceState buildTypeRock(RockSoil type)
    {
        final ImmutableList<SurfaceState> regions = ImmutableList.of(
            sand(),
            transition(sand(), rockSoil(type, TerraNovaSoil.Variant.LOAMY_SAND)),
            rockSoil(type, TerraNovaSoil.Variant.LOAMY_SAND),
            transition(rockSoil(type, SoilBlockType.Variant.SANDY_LOAM), rockSoil(type, SoilBlockType.Variant.SANDY_LOAM)),
            rockSoil(type, SoilBlockType.Variant.SANDY_LOAM),
            transition(rockSoil(type, SoilBlockType.Variant.SANDY_LOAM), rockSoil(type, SoilBlockType.Variant.LOAM)),
            rockSoil(type, SoilBlockType.Variant.LOAM),
            transition(rockSoil(type, SoilBlockType.Variant.LOAM), rockSoil(type, SoilBlockType.Variant.SILTY_LOAM)),
            rockSoil(type, SoilBlockType.Variant.SILTY_LOAM),
            transition(rockSoil(type, SoilBlockType.Variant.SILTY_LOAM), rockSoil(type, SoilBlockType.Variant.SILT)),
            rockSoil(type, SoilBlockType.Variant.SILT)
        );
        return new TerraNovaSoilSurfaceState(regions);
    }

    public static SurfaceState buildTypeRockSand(RockSand type)
    {
        final ImmutableList<SurfaceState> regions = ImmutableList.of(
            sand(),
            transition(sand(), rockSand(type)),
            rockSand(type),
            transition(rockSand(type), rockSand(type)),
            rockSand(type),
            transition(rockSand(type), rockSand(type)),
            rockSand(type),
            transition(rockSand(type), rockSand(type)),
            rockSand(type),
            transition(rockSand(type), rockSand(type)),
            rockSand(type)
        );
        return new TerraNovaSoilSurfaceState(regions);
    }

    public static SurfaceState buildSandOrGravel(boolean sandIsSandstone)
    {
        final SurfaceState sand = sandIsSandstone ? sandstone() : sand();
        final SurfaceState gravel = gravel();
        return new TerraNovaSoilSurfaceState(ImmutableList.of(
            sand,
            transition(sand, gravel),
            gravel,
            gravel,
            gravel,
            gravel,
            gravel,
            gravel,
            gravel,
            gravel,
            gravel
        ));
    }

    public static SurfaceState transition(SurfaceState first, SurfaceState second)
    {
        return context -> {
            final BlockPos pos = context.pos();
            double noise = SoilSurfaceState.PATCH_NOISE.noise(pos.getX(), pos.getZ());
            return noise > 0 ? first.getState(context) : second.getState(context);
        };
    }

    public static SurfaceState rock(Rock.BlockType type)
    {
        return context -> {
            if (isTerraNovaRock(context))
            {
                return TerraNovaBlocks.ROCK_BLOCKS.get(rockType(context)).get(type).get().defaultBlockState();
            }
            else
            {
                return TFCBlocks.ROCK_BLOCKS.get(rockType(context)).get(type).get().defaultBlockState();
            }
        };
    }

    public static SurfaceState rockCustom(RegistryRock rock, Rock.BlockType type)
    {
        return context -> {
            if (isCurrentRock(context, rock))
            {
                return TerraNovaBlocks.ROCK_BLOCKS.get(rockType(context)).get(type).get().defaultBlockState();
            }
            else
            {
                return TFCBlocks.ROCK_BLOCKS.get(rockType(context)).get(type).get().defaultBlockState();
            }
        };
    }

    public static SurfaceState sand()
    {
        return context -> context.getRock().sand().defaultBlockState();
    }

    public static SurfaceState sandstone()
    {
        return context -> context.getRock().sandstone().defaultBlockState();
    }

    public static SurfaceState gravel()
    {
        return context -> context.getRock().gravel().defaultBlockState();
    }

    public static SurfaceState soil(SoilBlockType type, SoilBlockType.Variant variant)
    {
        final Supplier<Block> block = TFCBlocks.SOIL.get(type).get(variant);
        return context -> block.get().defaultBlockState();
    }

    public static SurfaceState soil(SoilBlockType type, TerraNovaSoil.Variant variant)
    {
        final Supplier<Block> block = TerraNovaBlocks.SOIL.get(TerraNovaSoil.fromTFC(type)).get(variant);
        return context -> block.get().defaultBlockState();
    }

    public static SurfaceState soil(TerraNovaSoil type, SoilBlockType.Variant variant)
    {
        final Supplier<Block> block = TerraNovaBlocks.SOIL_TFC.get(type).get(variant);
        return context -> block.get().defaultBlockState();
    }

    public static SurfaceState soil(TerraNovaSoil type, TerraNovaSoil.Variant variant)
    {
        final Supplier<Block> block = TerraNovaBlocks.SOIL.get(type).get(variant);
        return context -> block.get().defaultBlockState();
    }

    public static SurfaceState rockSoil(RockSoil type, RegistrySoilVariant variant)
    {
        return context -> {
            if (isTerraNovaRock(context))
            {
                if (variant instanceof SoilBlockType.Variant soil)
                {
                    return TerraNovaBlocks.ROCKY_SOIL_SV.get(type).get(soil).get(rockType(context)).get().defaultBlockState();
                }
                else if (variant instanceof TerraNovaSoil.Variant soil)
                {
                    return TerraNovaBlocks.ROCKY_SOIL_TV.get(type).get(soil).get(rockType(context)).get().defaultBlockState();
                }
            }
            else
            {
                if (variant instanceof SoilBlockType.Variant soil)
                {
                    return TerraNovaBlocks.ROCKY_SOIL_TFC_SV.get(type).get(soil).get(rockType(context)).get().defaultBlockState();
                }
                else if (variant instanceof TerraNovaSoil.Variant soil)
                {
                    return TerraNovaBlocks.ROCKY_SOIL_TFC_TV.get(type).get(soil).get(rockType(context)).get().defaultBlockState();
                }
            }
            // Defaults -- hopefully not
            return TerraNovaBlocks.ROCKY_SOIL_TFC_SV.get(type).get(SoilBlockType.Variant.LOAM).get(rockType(context)).get().defaultBlockState();
        };
    }

    public static SurfaceState rockSandGrass()
    {
        return context -> TerraNovaBlocks.SAND_GRASS.get(sandColor(context)).get().defaultBlockState();
    }

    public static SurfaceState rockSandSparseGrass()
    {
        return context -> TerraNovaBlocks.SPARSE_SAND_GRASS.get(sandColor(context)).get().defaultBlockState();
    }

    public static SurfaceState rockSandDenseGrass()
    {
        return context -> TerraNovaBlocks.DENSE_SAND_GRASS.get(sandColor(context)).get().defaultBlockState();
    }

    public static SurfaceState rockSand(RockSand type)
    {
        return context -> {
            if (isTerraNovaRock(context))
            {
                return TerraNovaBlocks.ROCKY_SAND.get(type).get(sandColor(context)).get(rockType(context)).get().defaultBlockState();
            }
            else
            {
                return TerraNovaBlocks.ROCKY_SAND_TFC.get(type).get(sandColor(context)).get(rockType(context)).get().defaultBlockState();
            }
        };
    }

    public static SurfaceState rockRareSandGrass()
    {
        final Supplier<Block> pinkSand = TerraNovaBlocks.SAND_GRASS.get(Colors.PINK);
        final Supplier<Block> blackSand =  TerraNovaBlocks.SAND_GRASS.get(Colors.BLACK);

        return context -> {
            if (context.rainfall() > 300f && context.averageTemperature() > 15f)
            {
                return pinkSand.get().defaultBlockState();
            }
            else if (context.rainfall() > 300f)
            {
                return blackSand.get().defaultBlockState();
            }
            else
            {
                return TerraNovaBlocks.SAND_GRASS.get(sandColor(context)).get().defaultBlockState();
            }
        };
    }

    public static SurfaceState rockRareSandSparseGrass()
    {
        final Supplier<Block> pinkSand = TerraNovaBlocks.SPARSE_SAND_GRASS.get(Colors.PINK);
        final Supplier<Block> blackSand =  TerraNovaBlocks.SPARSE_SAND_GRASS.get(Colors.BLACK);

        return context -> {
            if (context.rainfall() > 300f && context.averageTemperature() > 15f)
            {
                return pinkSand.get().defaultBlockState();
            }
            else if (context.rainfall() > 300f)
            {
                return blackSand.get().defaultBlockState();
            }
            else
            {
                return TerraNovaBlocks.SPARSE_SAND_GRASS.get(sandColor(context)).get().defaultBlockState();
            }
        };
    }

    public static SurfaceState rockRareSandDenseGrass()
    {
        final Supplier<Block> pinkSand = TerraNovaBlocks.DENSE_SAND_GRASS.get(Colors.PINK);
        final Supplier<Block> blackSand =  TerraNovaBlocks.DENSE_SAND_GRASS.get(Colors.BLACK);

        return context -> {
            if (context.rainfall() > 300f && context.averageTemperature() > 15f)
            {
                return pinkSand.get().defaultBlockState();
            }
            else if (context.rainfall() > 300f)
            {
                return blackSand.get().defaultBlockState();
            }
            else
            {
                return TerraNovaBlocks.DENSE_SAND_GRASS.get(sandColor(context)).get().defaultBlockState();
            }
        };
    }

    public static SurfaceState rockRareSand(RockSand type)
    {
        return context -> {
            if (isTerraNovaRock(context))
            {
                final Supplier<Block> pinkSand = TerraNovaBlocks.ROCKY_SAND_TFC.get(type).get(Colors.PINK).get(rockType(context));
                final Supplier<Block> blackSand =  TerraNovaBlocks.ROCKY_SAND_TFC.get(type).get(Colors.BLACK).get(rockType(context));

                if (context.rainfall() > 300f && context.averageTemperature() > 15f)
                {
                    return pinkSand.get().defaultBlockState();
                }
                else if (context.rainfall() > 300f)
                {
                    return blackSand.get().defaultBlockState();
                }
                else
                {
                    return TerraNovaBlocks.ROCKY_SAND.get(type).get(sandColor(context)).get(rockType(context)).get().defaultBlockState();
                }
            }
            else
            {
                final Supplier<Block> pinkSand = TerraNovaBlocks.ROCKY_SAND_TFC.get(type).get(Colors.PINK).get(rockType(context));
                final Supplier<Block> blackSand =  TerraNovaBlocks.ROCKY_SAND_TFC.get(type).get(Colors.BLACK).get(rockType(context));

                if (context.rainfall() > 300f && context.averageTemperature() > 15f)
                {
                    return pinkSand.get().defaultBlockState();
                }
                else if (context.rainfall() > 300f)
                {
                    return blackSand.get().defaultBlockState();
                }
                else
                {
                    return TerraNovaBlocks.ROCKY_SAND_TFC.get(type).get(sandColor(context)).get(rockType(context)).get().defaultBlockState();
                }
            }
        };
    }

    public static Colors sandColor(SurfaceBuilderContext context)
    {
        if (context.getBottomRock().sand() != null)
        {
            for (Colors sandColors : Colors.values())
            {
                if (context.getBottomRock().sand() == TFCBlocks.SAND.get(sandColors.toSandTFC(true)).get())
                {
                    return sandColors;
                }
                else if (context.getBottomRock().sand() == TerraNovaBlocks.SAND.get(sandColors.nonTFC()).get())
                {
                    return sandColors;
                }
            }
        }
        else if (Colors.fromMaterialColour(context.getBottomRock().sand().defaultBlockState().getBlock().defaultMapColor()) != null)
        {
            return Colors.fromMaterialColour(context.getBottomRock().sand().defaultBlockState().getBlock().defaultMapColor());
        }
        return Colors.YELLOW;
    }

    public static RegistryRock rockType(SurfaceBuilderContext context)
    {
        if (surfaceRock(context) != null)
        {
            for (Rock rockTFC : Rock.values())
            {
                if (surfaceRock(context).raw() == TFCBlocks.ROCK_BLOCKS.get(rockTFC).get(Rock.BlockType.RAW).get())
                {
                    return rockTFC;
                }
                else
                {
                    for (TerraNovaRock rockTerraNova : TerraNovaRock.values())
                    {
                        if (surfaceRock(context).raw() == TerraNovaBlocks.ROCK_BLOCKS.get(rockTerraNova).get(Rock.BlockType.RAW).get())
                        {
                            return rockTerraNova;
                        }
                    }
                }
            }
        }
        return Rock.GRANITE;
    }

    public static boolean isTerraNovaRock(SurfaceBuilderContext context)
    {
        if (surfaceRock(context) != null)
        {
            for (TerraNovaRock rock : TerraNovaRock.values())
            {
                if (surfaceRock(context).raw() == TerraNovaBlocks.ROCK_BLOCKS.get(rock).get(Rock.BlockType.RAW).get())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isCurrentRock(SurfaceBuilderContext context, RegistryRock rock)
    {
        return rockType(context) == rock;
    }

    public static RegistrySoilVariant currentSoilVariant(SurfaceBuilderContext context)
    {
        for (SoilBlockType type : SoilBlockType.values())
        {
            for (SoilBlockType.Variant variantTFC : SoilBlockType.Variant.values())
            {
                if (SoilSurfaceState.buildType(type).getState(context).getBlock() == TFCBlocks.SOIL.get(type).get(variantTFC).get())
                {
                    return variantTFC;
                }
            }
        }
        return SoilBlockType.Variant.LOAM;
    }

    public static RockSettings surfaceRock(SurfaceBuilderContext context)
    {
        return context.getRock();
    }

    public final List<SurfaceState> regions;

    public TerraNovaSoilSurfaceState(List<SurfaceState> regions)
    {
        this.regions = regions;
    }

    /*@Override
    public BlockState getState(SurfaceBuilderContext context)
    {
        // Adjust rainfall to bias a little bit towards sand regions
        // Without: pure sand < 55mm, mixed sand < 105mm. With: pure sand < 75mm, mixed sand < 136mm
        final double rainfall = context.rainfall() + 20f;
        final int index = Mth.clamp((int) Mth.clampedMap(rainfall, 0, 500, 0, regions.size() - 0.01f), 0, regions.size() - 1);

        return regions.get(index).getState(context);
    }*/

    @Override
    public BlockState getState(SurfaceBuilderContext context)
    {
        BlockPos pos = context.pos();
        final RandomSource random = context.random();
        final double variantNoiseValue = new OpenSimplex2D(context.getSeed()).octaves(2).spread(0.003f).abs().noise(pos.getX(), pos.getZ());
        final double clayNoiseValue = new OpenSimplex2D(context.getSeed()).octaves(3).spread(0.003f).abs().noise(pos.getX(), pos.getZ());
        final double rainfall = context.rainfall() + 20f;

        final int index = Mth.clamp((int) Mth.clampedMap(rainfall, 0, 500, 0, regions.size() - 0.01f), 0, regions.size() - 1);
        final BlockState defaultState = regions.get(index).getState(context);

        if (variantNoiseValue >= 0.4D && context.getSlope() <= 3)
        {
            final ChunkData data = context.getChunkData();
            final RegistryRock rock = TNHelpers.rockType(data, pos);

            final float gaussFuzz = (float) gaussFuzz(pos, random, 1.25D);
            final float rainfallFuzz = (float) rainfall + gaussFuzz;
            final float temperature = context.averageTemperature() + gaussFuzz;
            final float forestDensity = data.getForestDensity() + gaussFuzz; // min 0, max 1 (%)
            final boolean isCarbonateRock = rock instanceof TerraNovaRock rockTN ? rockTN.isCarbonateRock() : TerraNovaRock.isCarbonateRock(rock);
            final KoppenClimateClassification koppen = KoppenClimateClassification.classify(temperature, rainfallFuzz);

            switch(koppen)
            {
                case ARCTIC:
                case COLD_DESERT:
                case HOT_DESERT:
                    return defaultState;
                case TUNDRA:
                case SUBARCTIC:
                case HUMID_SUBARCTIC:
                    if (forestDensity >= 0.33F)
                    {
                        return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(TerraNovaSoil.Variant.HUMUS).get().defaultBlockState();
                    }
                case HUMID_OCEANIC:
                case TEMPERATE:
                    if (forestDensity >= 0.22F)
                    {
                        return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(TerraNovaSoil.Variant.HUMUS).get().defaultBlockState();
                    }
                    return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(TerraNovaSoil.Variant.CHERNOZEM).get().defaultBlockState();
                case SUBTROPICAL:
                case HUMID_SUBTROPICAL:
                case TROPICAL_SAVANNA:
                    if (isCarbonateRock)
                    {
                        return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(TerraNovaSoil.Variant.TERRA_ROSSA).get().defaultBlockState();
                    }
                case TROPICAL_RAINFOREST:
                    if (forestDensity >= 0.15F)
                    {
                        return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(TerraNovaSoil.Variant.TERRA_PETRA).get().defaultBlockState();
                    }
                default:
                    return defaultState;
            }
        }
        if (clayNoiseValue >= 0.7D && clayNoiseValue <= 0.9D && context.getSlope() <= 2)
        {
            switch (index)
            {
                case 1:
                    return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(transitionSoil(TerraNovaSoil.Variant.SANDY_CLAY, TerraNovaSoil.Variant.SANDY_CLAY, pos, random)).get().defaultBlockState();
                case 2:
                    return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(TerraNovaSoil.Variant.SANDY_CLAY).get().defaultBlockState();
                case 3:
                    return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(transitionSoil(TerraNovaSoil.Variant.SANDY_CLAY, TerraNovaSoil.Variant.SANDY_CLAY_LOAM, pos, random)).get().defaultBlockState();
                case 4:
                    return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(TerraNovaSoil.Variant.SANDY_CLAY_LOAM).get().defaultBlockState();
                case 5:
                    return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(transitionSoil(TerraNovaSoil.Variant.SANDY_CLAY_LOAM, TerraNovaSoil.Variant.CLAY_LOAM, pos, random)).get().defaultBlockState();
                case 6:
                    return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(TerraNovaSoil.Variant.CLAY_LOAM).get().defaultBlockState();
                case 7:
                    return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(transitionSoil(TerraNovaSoil.Variant.CLAY_LOAM, TerraNovaSoil.Variant.SILTY_CLAY_LOAM, pos, random)).get().defaultBlockState();
                case 8:
                    return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(TerraNovaSoil.Variant.SILTY_CLAY_LOAM).get().defaultBlockState();
                case 9:
                    return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(transitionSoil(TerraNovaSoil.Variant.SILTY_CLAY_LOAM, TerraNovaSoil.Variant.SILTY_CLAY, pos, random)).get().defaultBlockState();
                case 10:
                    return TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(TerraNovaSoil.Variant.SILTY_CLAY).get().defaultBlockState();
                default:
                    return defaultState;
            }
        }
        return defaultState;
    }

    static class NeedsPostProcessing extends TerraNovaSoilSurfaceState
    {
        private NeedsPostProcessing(List<SurfaceState> regions)
        {
            super(regions);
        }

        @Override
        public void setState(SurfaceBuilderContext context)
        {
            context.chunk().setBlockState(context.pos(), getState(context), false);
            context.chunk().markPosForPostprocessing(context.pos());
        }
    }

    public static RegistrySoilVariant getSoilVariant(WorldGenLevel level, BlockPos pos)
    {
        final RandomSource random = level.getRandom();
        final ChunkDataProvider provider = ChunkDataProvider.get(level);
        final ChunkData data = provider.get(level, pos);

        // Adjust rainfall to bias a little bit towards sand regions
        // Without: pure sand < 55mm, mixed sand < 105mm. With: pure sand < 75mm, mixed sand < 136mm
        final double rainfall = data.getRainfall(pos) + 20f;

        final int index = Mth.clamp((int) Mth.clampedMap(rainfall, 0, 500, 0, 11 - 0.01f), 0, 11 - 1);

        switch (index)
        {
            case 1:
                return transitionSoil(TerraNovaSoil.Variant.LOAMY_SAND, TerraNovaSoil.Variant.LOAMY_SAND, pos, random);
            case 2:
                return TerraNovaSoil.Variant.LOAMY_SAND;
            case 3:
                return transitionSoil(TerraNovaSoil.Variant.LOAMY_SAND, SoilBlockType.Variant.SANDY_LOAM, pos, random);
            case 4:
                return SoilBlockType.Variant.SANDY_LOAM;
            case 5:
                return transitionSoil(SoilBlockType.Variant.SANDY_LOAM, SoilBlockType.Variant.LOAM, pos, random);
            case 6:
                return SoilBlockType.Variant.LOAM;
            case 7:
                return transitionSoil(SoilBlockType.Variant.LOAM, SoilBlockType.Variant.SILTY_LOAM, pos, random);
            case 8:
                return SoilBlockType.Variant.SILTY_LOAM;
            case 9:
                return transitionSoil(SoilBlockType.Variant.SILTY_LOAM, SoilBlockType.Variant.SILT, pos, random);
            case 10:
                return SoilBlockType.Variant.SILT;
            default:
                return TerraNovaSoil.Variant.LOAMY_SAND;
        }
    }

    public static RegistrySoilVariant getOrganicSoilVariant(WorldGenLevel level, BlockPos pos)
	{
        final ChunkDataProvider provider = ChunkDataProvider.get(level);
        final ChunkData data = provider.get(level, pos);
        final RegistryRock rock = TNHelpers.rockType(level, pos);

        final float gaussFuzz = (float) gaussFuzz(pos, level.getRandom(), 1.25D);
        final float rainfall = data.getRainfall(pos) + gaussFuzz;
        final float temperature = data.getAverageTemp(pos) + gaussFuzz;
        final double forestDensity = data.getForestDensity() + gaussFuzz; // min 0, max 1 (%)
        final boolean isCarbonateRock = rock instanceof TerraNovaRock rockTN ? rockTN.isCarbonateRock() : TerraNovaRock.isCarbonateRock(rock);
        final double variantNoiseValue = new OpenSimplex2D(level.getSeed()).octaves(2).spread(0.003f).abs().noise(pos.getX(), pos.getZ());

        final KoppenClimateClassification koppen = KoppenClimateClassification.classify(temperature, rainfall);

        if (variantNoiseValue >= 0.4D && isFlatEnough(level, pos, pos.mutable()))
        {
            switch(koppen)
            {
                case ARCTIC:
                case COLD_DESERT:
                case HOT_DESERT:
                    return getSoilVariant(level, pos);
                case TUNDRA:
                case SUBARCTIC:
                case HUMID_SUBARCTIC:
                    if (forestDensity >= 0.33F)
                    {
                        return TerraNovaSoil.Variant.HUMUS;
                    }
                case HUMID_OCEANIC:
                case TEMPERATE:
                    if (forestDensity >= 0.22F)
                    {
                        return TerraNovaSoil.Variant.HUMUS;
                    }
                    return TerraNovaSoil.Variant.CHERNOZEM;
                case SUBTROPICAL:
                case HUMID_SUBTROPICAL:
                case TROPICAL_SAVANNA:
                    if (isCarbonateRock)
                    {
                        return TerraNovaSoil.Variant.TERRA_ROSSA;
                    }
                case TROPICAL_RAINFOREST:
                    if (forestDensity >= 0.15F)
                    {
                        return TerraNovaSoil.Variant.TERRA_PETRA;
                    }
                default:
                    return getSoilVariant(level, pos);
            }
        }
        return getSoilVariant(level, pos);
	}

    public static double gaussFuzz(BlockPos pos, RandomSource random, double factor)
    {
        double noise = SoilSurfaceState.PATCH_NOISE.noise(pos.getX(), pos.getZ());
        return noise + (factor * random.nextGaussian());
    }

    public static RegistrySoilVariant transitionSoil(RegistrySoilVariant first, RegistrySoilVariant second, BlockPos pos, RandomSource random)
    {
        return gaussFuzz(pos, random, 2) > 0 ? first : second;
    }

    public static boolean isFlatEnough(WorldGenLevel level, BlockPos pos, BlockPos.MutableBlockPos mutablePos)
    {
        float flatness = 0.25F;
        int radius = 3;
        int maxDepth = 4;
        int flatAmount = 0;

        for (int x = -radius; x <= radius; x++)
        {
            for (int z = -radius; z <= radius; z++)
            {
                for (int y = 0; y < maxDepth; y++)
                {
                    mutablePos.set(pos).move(x, y, z);
                    BlockState stateAt = level.getBlockState(mutablePos);
                    if (!stateAt.isAir() && stateAt.getFluidState().getType() == Fluids.EMPTY) // No direct access to world, cannot use forge method
                    {
                        flatAmount++;
                    }
                }
            }
        }
        return flatAmount / ((1f + 2 * radius) * (1f + 2 * radius)) > flatness;
    }
}