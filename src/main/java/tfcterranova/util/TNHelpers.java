package tfcterranova.util;

import java.util.Arrays;
import java.util.Locale;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity.NutrientType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.util.registry.RegistryRock;
import net.dries007.tfc.util.registry.RegistrySoilVariant;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ChunkDataProvider;
import net.dries007.tfc.world.settings.RockSettings;

import tfcterranova.common.blocks.TerraNovaBlocks;
import tfcterranova.common.blocks.rock.TerraNovaRock;
import tfcterranova.common.blocks.soil.Colors;
import tfcterranova.common.blocks.soil.TerraNovaSoil;

import static tfcterranova.TFCTerraNova.MOD_ID;

public class TNHelpers
{
    public static final Direction[] NOT_DOWN = new Direction[] {Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.UP};
    public static final Direction[] NOT_UP = new Direction[] {Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN};
    public static final Direction[] DIRECTIONS = Direction.values();
    public static final Direction[] DIRECTIONS_HORIZONTAL_FIRST = new Direction[] {Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN};
    public static final Direction[] DIRECTIONS_HORIZONTAL = Arrays.stream(DIRECTIONS).filter(d -> d != Direction.DOWN && d != Direction.UP).toArray(Direction[]::new);
    public static final Direction[] DIRECTIONS_VERTICAL = Arrays.stream(DIRECTIONS).filter(d -> d == Direction.DOWN || d == Direction.UP).toArray(Direction[]::new);

    /**
     * Default {@link ResourceLocation}, except with a TFC namespace
     */
    public static ResourceLocation identifier(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }

    public static RegistryRock rockType(ChunkData data, BlockPos pos)
    {
        RockSettings surfaceRock = data.getRockData().getRock(pos);
        if (surfaceRock != null)
        {
            for (Rock rockTFC : Rock.values())
            {
                if (surfaceRock.raw() == TFCBlocks.ROCK_BLOCKS.get(rockTFC).get(Rock.BlockType.RAW).get())
                {
                    return rockTFC;
                }
                else
                {
                    for (TerraNovaRock rockTN : TerraNovaRock.values())
                    {
                        if (surfaceRock.raw() == TerraNovaBlocks.ROCK_BLOCKS.get(rockTN).get(Rock.BlockType.RAW).get())
                        {
                            return rockTN;
                        }
                    }
                }
            }
        }
        return Rock.GRANITE;
    }

    public static RegistryRock rockType(ServerLevel level, BlockPos pos)
    {
        RockSettings surfaceRock = ChunkDataProvider.get(level).get(level, pos).getRockData().getRock(pos);
        if (surfaceRock != null)
        {
            for (Rock rockTFC : Rock.values())
            {
                if (surfaceRock.raw() == TFCBlocks.ROCK_BLOCKS.get(rockTFC).get(Rock.BlockType.RAW).get())
                {
                    return rockTFC;
                }
                else
                {
                    for (TerraNovaRock rockTN : TerraNovaRock.values())
                    {
                        if (surfaceRock.raw() == TerraNovaBlocks.ROCK_BLOCKS.get(rockTN).get(Rock.BlockType.RAW).get())
                        {
                            return rockTN;
                        }
                    }
                }
            }
        }
        return Rock.GRANITE;
    }

    public static RegistryRock rockType(WorldGenLevel level, BlockPos pos)
    {
        RockSettings surfaceRock = ChunkDataProvider.get(level).get(level, pos).getRockData().getRock(pos);
        if (surfaceRock != null)
        {
            for (Rock rockTFC : Rock.values())
            {
                if (surfaceRock.raw() == TFCBlocks.ROCK_BLOCKS.get(rockTFC).get(Rock.BlockType.RAW).get())
                {
                    return rockTFC;
                }
                else
                {
                    for (TerraNovaRock rockTN : TerraNovaRock.values())
                    {
                        if (surfaceRock.raw() == TerraNovaBlocks.ROCK_BLOCKS.get(rockTN).get(Rock.BlockType.RAW).get())
                        {
                            return rockTN;
                        }
                    }
                }
            }
        }
        return Rock.GRANITE;
    }

    public static RegistrySoilVariant getSoilVariant(WorldGenLevel level, BlockPos pos)
    {
        for (SoilBlockType.Variant soilVariant : SoilBlockType.Variant.values())
        {
            if (level.getBlockState(pos).getBlock().getName().toString().toLowerCase(Locale.ROOT).contains(soilVariant.name().toLowerCase(Locale.ROOT)))
            {
                return soilVariant;
            }
        }
        for (TerraNovaSoil.Variant soilVariant : TerraNovaSoil.Variant.values())
        {
            if (level.getBlockState(pos).getBlock().getName().toString().toLowerCase(Locale.ROOT).contains(soilVariant.name().toLowerCase(Locale.ROOT)))
            {
                return soilVariant;
            }
        }
        return SoilBlockType.Variant.LOAM;
    }

    public static RegistrySoilVariant getSoilVariant(ServerLevel level, BlockPos pos)
    {
        for (SoilBlockType.Variant soilVariant : SoilBlockType.Variant.values())
        {
            if (level.getBlockState(pos).getBlock().getName().toString().toLowerCase(Locale.ROOT).contains(soilVariant.name().toLowerCase(Locale.ROOT)))
            {
                return soilVariant;
            }
        }
        for (TerraNovaSoil.Variant soilVariant : TerraNovaSoil.Variant.values())
        {
            if (level.getBlockState(pos).getBlock().getName().toString().toLowerCase(Locale.ROOT).contains(soilVariant.name().toLowerCase(Locale.ROOT)))
            {
                return soilVariant;
            }
        }
        return SoilBlockType.Variant.LOAM;
    }

    public static boolean isVanillaSoilVariant(WorldGenLevel level, BlockPos pos)
    {
        for (SoilBlockType.Variant soilVariant : SoilBlockType.Variant.values())
        {
            if (TNHelpers.getSoilVariant(level, pos).toString().toLowerCase(Locale.ROOT).contains(soilVariant.name().toLowerCase(Locale.ROOT)))
            {
                return true;
            }
        }
        return false;
    }

    public static Block getSandBlock(WorldGenLevel level, BlockPos pos, boolean cheap)
    {
        Colors color = getSandColorTN(level, pos, cheap);
        if (color.hasSandNew())
        {
            return TerraNovaBlocks.SAND.get(color).get();
        }
        else
        {
            return TFCBlocks.SAND.get(color.toSandTFC(true)).get();
        }
    }

	public static SandBlockType getSandColor(WorldGenLevel level, BlockPos pos, boolean cheap)
	{
        boolean foundColour = false;
        if (!cheap)
        {
            for (Direction direction : TNHelpers.DIRECTIONS_HORIZONTAL_FIRST)
            {
                SandBlockType colour = Colors.toSandTFC(Colors.fromMaterialColour(level.getBlockState(pos.relative(direction)).getBlock().defaultMapColor()), true);
                if (colour != null)
                {
                    foundColour = true;
                    return colour;
                }
                else
                {
                    foundColour = false;
                }
            }
        }
        else if (cheap)
        {
            SandBlockType colour = Colors.toSandTFC(Colors.fromMaterialColour(level.getBlockState(pos.below()).getBlock().defaultMapColor()), true);
            if (colour != null)
            {
                foundColour = true;
                return colour;
            }
            else
            {
                foundColour = false;
            }
        }
        else if (!foundColour)
        {
            final ChunkDataProvider provider = ChunkDataProvider.get(level);
            if (provider != null)
            {
                final ChunkData data = provider.get(level, pos);
                final Block sand = data.getRockData().getRock(pos).sand();

                for (Colors sandColors : Colors.values())
                {
                    if (sand != null && (sand == TFCBlocks.SAND.get(Colors.toSandTFC(sandColors, true)).get() || sand == TerraNovaBlocks.SAND.get(Colors.nonTFC(sandColors)).get()))
                    {
                        return Colors.toSandTFC(sandColors, true);
                    }
                }
            }
        }
		return SandBlockType.YELLOW;
	}

	public static Colors getSandColorTN(WorldGenLevel level, BlockPos pos, boolean cheap)
	{
        boolean foundColour = false;
        if (!cheap)
        {
            for (Direction direction : TNHelpers.DIRECTIONS_HORIZONTAL_FIRST)
            {
                Colors colour = Colors.fromMaterialColour(level.getBlockState(pos.relative(direction)).getBlock().defaultMapColor());
                if (colour != null)
                {
                    foundColour = true;
                    return colour;
                }
                else
                {
                    foundColour = false;
                }
            }
        }
        else if (cheap)
        {
            Colors colour = Colors.fromMaterialColour(level.getBlockState(pos.below()).getBlock().defaultMapColor());
            if (colour != null)
            {
                foundColour = true;
                return colour;
            }
            else
            {
                foundColour = false;
            }
        }
        else if (!foundColour)
        {
            final ChunkDataProvider provider = ChunkDataProvider.get(level);
            if (provider != null)
            {
                final ChunkData data = provider.get(level, pos);
                final Block sand = data.getRockData().getRock(pos).sand();

                for (Colors sandColors : Colors.values())
                {
                    if (sand != null && (sand == TFCBlocks.SAND.get(Colors.toSandTFC(sandColors, true)).get() || sand == TerraNovaBlocks.SAND.get(Colors.nonTFC(sandColors)).get()))
                    {
                        return sandColors;
                    }
                }
            }
        }
		return Colors.ORANGE;
	}

    public static float soilNutrientModifier(RegistrySoilVariant soil, NutrientType type)
    {
        if (soil instanceof SoilBlockType.Variant variant)
        {
            switch (variant)
            {
                case SILT:
                    return 0.04F;
                case LOAM:
                    return 0.08F;
                case SANDY_LOAM:
                    return 0.03F;
                case SILTY_LOAM:
                    return 0.06F;
                default:
                    return 0F;
            }
        }
        else if (soil instanceof TerraNovaSoil.Variant variant)
        {
            switch (variant)
            {
                case LOAMY_SAND:
                    return 0F;
                case SANDY_CLAY:
                    return 0.04F;
                case SILTY_CLAY:
                    return 0.06F;
                case SANDY_CLAY_LOAM:
                case SILTY_CLAY_LOAM:
                    return 0.1F;
                case CLAY_LOAM:
                    return 0.15F;
                case TERRA_PETRA:
                    return (type == NutrientType.NITROGEN ? 0.69F : type == NutrientType.PHOSPHOROUS ? 0.4F : type == NutrientType.POTASSIUM ? 0.3F : 0.45F);
                case TERRA_ROSSA:
                    return (type == NutrientType.NITROGEN ? 0.3F : type == NutrientType.PHOSPHOROUS ? 0.66F : type == NutrientType.POTASSIUM ? 0.57F : 0.3F);
                case CHERNOZEM:
                    return (type == NutrientType.NITROGEN ? 0.8F : type == NutrientType.PHOSPHOROUS ? 0.7F : type == NutrientType.POTASSIUM ? 0.5F : 0.3F);
                case HUMUS:
                    return (type == NutrientType.NITROGEN ? 0.36F : type == NutrientType.PHOSPHOROUS ? 0.29F : type == NutrientType.POTASSIUM ? 0.25F : 0.25F);
                default:
                    return 0F;
            }
        }
        return 0;
    }

    public static float getRockNutrient(RegistrySoilVariant soil, RegistryRock rockType, NutrientType forType, RandomSource random)
    {
        float gaussModifier = Mth.abs((float) random.nextGaussian()) * 0.09F;

        float nutrientAmount = 0;
        if (forType == NutrientType.NITROGEN)
        {
            float soilModifier = soil != null ? soilNutrientModifier(soil, forType) : 0F;
            switch (rockType.category())
            {
                case IGNEOUS_EXTRUSIVE:
                    nutrientAmount = 0.005F + gaussModifier + soilModifier;
                case IGNEOUS_INTRUSIVE:
                    nutrientAmount = 0.01F + gaussModifier + soilModifier;
                case METAMORPHIC:
                    nutrientAmount = 0.025F + gaussModifier + soilModifier;
                case SEDIMENTARY:
                    nutrientAmount = 0.04F + gaussModifier + soilModifier;
                default:
                    nutrientAmount = 0.01F + gaussModifier + soilModifier;
            }
        }
        else if (forType == NutrientType.PHOSPHOROUS)
        {
            float soilModifier = soil != null ? soilNutrientModifier(soil, forType) : 0F;
            switch (rockType.category())
            {
                case IGNEOUS_EXTRUSIVE:
                    nutrientAmount = 0.13F + gaussModifier + soilModifier;
                case IGNEOUS_INTRUSIVE:
                    nutrientAmount = 0.09F + gaussModifier + soilModifier;
                case METAMORPHIC:
                    nutrientAmount = 0.05F + gaussModifier + soilModifier;
                case SEDIMENTARY:
                    nutrientAmount = 0.08F + gaussModifier + soilModifier;
                default:
                    nutrientAmount = 0.04F + gaussModifier + soilModifier;
            }
        }
        else if (forType == NutrientType.POTASSIUM)
        {
            float soilModifier = soil != null ? soilNutrientModifier(soil, forType) : 0F;
            switch (rockType.category())
            {
                case IGNEOUS_EXTRUSIVE:
                    nutrientAmount = 0.2F + gaussModifier + soilModifier;
                case IGNEOUS_INTRUSIVE:
                    nutrientAmount = 0.125F + gaussModifier + soilModifier;
                case METAMORPHIC:
                    nutrientAmount = 0.265F + gaussModifier + soilModifier;
                case SEDIMENTARY:
                    nutrientAmount = 0.05F + gaussModifier + soilModifier;
                default:
                    nutrientAmount = 0.04F + gaussModifier + soilModifier;
            }
        }
        return Mth.clamp(nutrientAmount, 0F, 1F);
    }
}
