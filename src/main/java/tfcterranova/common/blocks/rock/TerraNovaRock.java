package tfcterranova.common.blocks.rock;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.blocks.SandstoneBlockType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.*;
import net.dries007.tfc.common.blocks.rock.Rock.BlockType;
import net.dries007.tfc.util.registry.RegistryRock;
import net.dries007.tfc.world.settings.RockSettings;

import tfcterranova.common.blocks.TerraNovaBlocks;
import tfcterranova.common.blocks.soil.Colors;
import tfcterranova.config.TNConfig;
import tfcterranova.util.TNHelpers;

import static tfcterranova.common.blocks.soil.Colors.*;

public enum TerraNovaRock implements RegistryRock
{
    /*
     * Igneous rocks:
     * https://rocks.comparenature.com/en/igneous-rocks/style-2
     * 
     * Metamorphic rocks:
     * https://rocks.comparenature.com/en/metamorphic-rocks/style-3
     */

    ADAKITE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, BROWN),
    ADAMELLITE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, PINK),
    RED_GRANITE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, RED),
    AMPHIBOLITE(RockDisplayCategory.METAMORPHIC, LIGHT_BLUE),
    ANORTHOSITE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, LIGHT_GRAY),
    APLITE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, WHITE),
    APPINITE(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, GRAY),
    BASALTIC_TRACHYANDESITE(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, PURPLE),
    BASANITE(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, BLACK),
    BENMOREITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_EXTRUSIVE, BLACK),
    BLUE_GRANITE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, BLUE),
    BLUESCHIST(RockDisplayCategory.METAMORPHIC, LIGHT_BLUE),
    BONINITE(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, GRAY),
    BOROLANITE(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, GRAY),
    CARBONATITE(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, WHITE),
    CHARNOCKITE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, YELLOW),
    COMENDITE(RockDisplayCategory.FELSIC_IGNEOUS_EXTRUSIVE, GRAY),
    DIABASE(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, LIGHT_GRAY),
    DUNITE(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, LIGHT_GREEN),
    ENDERBITE(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, BLACK),
    EPIDOSITE(RockDisplayCategory.METAMORPHIC, GREEN),
    ESSEXITE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, LIGHT_GRAY),
    FELSITE(RockDisplayCategory.FELSIC_IGNEOUS_EXTRUSIVE, ORANGE),
    FOIDOLITE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, BLUE),
    GRANODIORITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_INTRUSIVE, WHITE),
    HARZBURGITE(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, YELLOW),
    HAWAIITE(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, RED),
    HORNBLENDITE(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, LIGHT_GRAY),
    HYALOCLASTITE(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, BROWN),
    ICELANDITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_EXTRUSIVE, LIGHT_BLUE),
    IGNIMBRITE(RockDisplayCategory.FELSIC_IGNEOUS_EXTRUSIVE, RED),
    IJOLITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_INTRUSIVE, LIGHT_GREEN),
    KENYTE(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, GREEN),
    KIMBERLITE(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, BLUE),
    LAMPROPHYRE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, YELLOW),
    LARVIKITE(RockDisplayCategory.FELSIC_IGNEOUS_EXTRUSIVE, BLUE),
    LATITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_EXTRUSIVE, BLACK),
    LHERZOLITE(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, GREEN),
    LITCHFIELDITE(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, YELLOW),
    LUXULLIANITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_INTRUSIVE, PINK),
    MANGERITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_INTRUSIVE, LIGHT_GRAY),
    MINETTE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, LIGHT_GREEN),
    MONZOGRANITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_INTRUSIVE, MAGENTA),
    MONZONITE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, WHITE),
    MUGEARITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_EXTRUSIVE, BROWN),
    NEPHELINE_SYENITE(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, LIGHT_GRAY),
    KAKORTOKITE(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, WHITE),
    NORITE(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, YELLOW),
    OBSIDIAN(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, BLACK), // Yes, it is a rock/mineral

    BRECCIA(RockDisplayCategory.SEDIMENTARY, WHITE),
    PORPHYRY(RockDisplayCategory.FELSIC_IGNEOUS_EXTRUSIVE, RED),
    PERIDOTITE(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, CYAN),
    //BLAIMORITE(RockDisplayCategory.IGNEOUS_EXTRUSIVE, LIGHT_GREEN), // What is blaimorite even? Change to serpentinite
    SERPENTINITE(RockDisplayCategory.METAMORPHIC, LIGHT_GREEN),
    LATERITE(RockDisplayCategory.SEDIMENTARY, ORANGE),
    MUDSTONE(RockDisplayCategory.SEDIMENTARY, LIGHT_GRAY, true),
    SANDSTONE(RockDisplayCategory.SEDIMENTARY, YELLOW),
    SILTSTONE(RockDisplayCategory.SEDIMENTARY, LIGHT_GRAY),
    ARKOSE(RockDisplayCategory.SEDIMENTARY, BROWN),
    JASPILLITE(RockDisplayCategory.SEDIMENTARY, RED),
    TRAVERTINE(RockDisplayCategory.SEDIMENTARY, WHITE, true),
    WACKESTONE(RockDisplayCategory.SEDIMENTARY, BLACK, true),
    BLACKBAND_IRONSTONE(RockDisplayCategory.SEDIMENTARY, PURPLE),
    CATACLASITE(RockDisplayCategory.METAMORPHIC, GRAY),
    CATLINITE(RockDisplayCategory.METAMORPHIC, PINK),
    GREENSCHIST(RockDisplayCategory.METAMORPHIC, GREEN),
    NOVACULITE(RockDisplayCategory.METAMORPHIC, WHITE),
    SOAPSTONE(RockDisplayCategory.METAMORPHIC, CYAN),
    KOMATIITE(RockDisplayCategory.METAMORPHIC, LIGHT_BLUE),
    MYLONITE(RockDisplayCategory.METAMORPHIC, LIGHT_GRAY);

    public static final TerraNovaRock[] VALUES = values();

    public final String serializedName;
    public final RockDisplayCategory category;
    public final MapColor color;
    public final Colors sandType;
    public final boolean isCarbonateRock;

    TerraNovaRock(RockDisplayCategory category, MapColor color, Colors sandType, boolean isCarbonateRock)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.category = category;
        this.color = color;
        this.sandType = sandType;
        this.isCarbonateRock = isCarbonateRock;
    }

    TerraNovaRock(RockDisplayCategory category, Colors sandType, boolean isCarbonateRock)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.category = category;
        this.color = sandType.getMaterialColor();
        this.sandType = sandType;
        this.isCarbonateRock = isCarbonateRock;
    }

    TerraNovaRock(RockDisplayCategory category, Colors sandType)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.category = category;
        this.color = sandType.getMaterialColor();
        this.sandType = sandType;
        this.isCarbonateRock = false;
    }

    public Supplier<? extends Block> getSandType()
    {
        if (sandType.hasSandTFC())
        {
            return TFCBlocks.SAND.get(sandType.toSandTFC(true));
        }
        else
        {
            return TerraNovaBlocks.SAND.get(sandType);
        }
    }

    public Supplier<? extends Block> getSandstone()
    {
        if (sandType.hasSandTFC())
        {
            return TFCBlocks.SANDSTONE.get(sandType.toSandTFC(true)).get(SandstoneBlockType.RAW);
        }
        else
        {
            return TerraNovaBlocks.SANDSTONE_TFC.get(sandType).get(SandstoneBlockType.RAW);
        }
    }

    @Override
    public RockDisplayCategory displayCategory()
    {
        return category;
    }

    @Override
    public MapColor color()
    {
        return color;
    }

    public Colors getColor()
    {
        return sandType;
    }

    @Override
    public Supplier<? extends Block> getBlock(BlockType type)
    {
        return TerraNovaBlocks.ROCK_BLOCKS.get(this).get(type);
    }

    @Override
    public Supplier<? extends Block> getAnvil()
    {
        return TerraNovaBlocks.ROCK_ANVILS.get(this);
    }

    @Override
    public Supplier<? extends SlabBlock> getSlab(BlockType type)
    {
        return TerraNovaBlocks.ROCK_DECORATIONS.get(this).get(type).slab();
    }

    @Override
    public Supplier<? extends StairBlock> getStair(BlockType type)
    {
        return TerraNovaBlocks.ROCK_DECORATIONS.get(this).get(type).stair();
    }

    @Override
    public Supplier<? extends WallBlock> getWall(BlockType type)
    {
        return TerraNovaBlocks.ROCK_DECORATIONS.get(this).get(type).wall();
    }

    public static boolean isTFCRock(RegistryRock rock)
    {
        return rock instanceof Rock ? true : false;
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }

    public boolean isCarbonateRock()
    {
        return isCarbonateRock;
    }

    public static boolean isCarbonateRock(RegistryRock rock)
    {
        return rock == Rock.LIMESTONE || rock == Rock.DOLOMITE || rock == Rock.CHALK || rock == Rock.MARBLE;
    }

    public boolean isEnabled()
    {
        return TNConfig.COMMON.toggleRock.get(this).get();
    }

    public enum TNBlockType implements StringRepresentable
    {
        STONE_TILES((rock, self) -> new Block(properties(rock).sound(SoundType.DEEPSLATE).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops()), true),
        COBBLED_BRICKS((rock, self) -> new MossGrowingRotatedPillarBlock(properties(rock).sound(SoundType.DEEPSLATE_BRICKS).strength(rock.category().hardness(9f), 10).requiresCorrectToolForDrops(), transformRock(rock).get(self.mossy())), true),
        POLISHED_COBBLED_BRICKS((rock, self) -> new Block(properties(rock).sound(SoundType.ANCIENT_DEBRIS).strength(rock.category().hardness(7f), 10).requiresCorrectToolForDrops()), true),
        FLAGSTONE_BRICKS((rock, self) -> new MossGrowingBlock(properties(rock).sound(SoundType.DEEPSLATE_TILES).strength(rock.category().hardness(9f), 10).requiresCorrectToolForDrops(), transformRock(rock).get(self.mossy())), true),
        MOSSY_COBBLED_BRICKS((rock, self) -> new MossSpreadingRotatedPillarBlock(properties(rock).sound(SoundType.DEEPSLATE_BRICKS).strength(rock.category().hardness(9f), 10).requiresCorrectToolForDrops()), true),
        MOSSY_FLAGSTONE_BRICKS((rock, self) -> new MossSpreadingBlock(properties(rock).sound(SoundType.DEEPSLATE_TILES).strength(rock.category().hardness(9f), 10).requiresCorrectToolForDrops()), true),
        CRACKED_FLAGSTONE_BRICKS((rock, self) -> new Block(properties(rock).sound(SoundType.DEEPSLATE_TILES).strength(rock.category().hardness(9f), 10).requiresCorrectToolForDrops()), true),
        ROCK_PILE((rock, self) -> new MossGrowingBoulderBlock(properties(rock).sound(SoundType.BASALT).strength(rock.category().hardness(1.5f), 10).requiresCorrectToolForDrops().randomTicks().speedFactor(0.8F).noCollission().hasPostProcess(TerraNovaBlocks::always).noOcclusion().dynamicShape(), transformRock(rock).get(self.mossy()))),
        MOSSY_ROCK_PILE((rock, self) -> new MossSpreadingBoulderBlock(properties(rock).sound(SoundType.BASALT).strength(rock.category().hardness(1.5f), 10).requiresCorrectToolForDrops().randomTicks().speedFactor(0.8F).noCollission().hasPostProcess(TerraNovaBlocks::always).noOcclusion().dynamicShape())),
        MORTAR_AND_COBBLE((rock, self) -> new MossGrowingBlock(properties(rock).sound(SoundType.STONE).strength(rock.category().hardness(5.5f), 10).requiresCorrectToolForDrops(), transformRock(rock).get(self.mossy())), true),
        MOSSY_MORTAR_AND_COBBLE((rock, self) -> new MossSpreadingBlock(properties(rock).sound(SoundType.STONE).strength(rock.category().hardness(5.5f), 10).requiresCorrectToolForDrops()), true);

        public static BlockBehaviour.Properties properties(RegistryRock rock)
        {
            return BlockBehaviour.Properties.of().mapColor(rock.color()).instrument(NoteBlockInstrument.BASEDRUM);
        }

        public static final TNBlockType[] VALUES = TNBlockType.values();

        public static TNBlockType valueOf(int i)
        {
            return i >= 0 && i < VALUES.length ? VALUES[i] : STONE_TILES;
        }

        public final boolean variants;
        public final BiFunction<RegistryRock, TNBlockType, Block> factory;
        public final String serializedName;

        TNBlockType(BiFunction<RegistryRock, TNBlockType, Block> factory, boolean variants)
        {
            this.factory = factory;
            this.variants = variants;
            this.serializedName = name().toLowerCase(Locale.ROOT);
        }

        TNBlockType(BiFunction<RegistryRock, TNBlockType, Block> factory)
        {
            this.factory = factory;
            this.variants = false;
            this.serializedName = name().toLowerCase(Locale.ROOT);
        }

        /**
         * @return if this block type should be given slab, stair and wall variants
         */
        public boolean hasVariants()
        {
            return variants;
        }

        public Block create(RegistryRock rock)
        {
            return factory.apply(rock, this);
        }

        public BiFunction<RegistryRock, TNBlockType, Block> getFactory()
        {
            return factory;
        }

        @Override
        public String getSerializedName()
        {
            return serializedName;
        }

        @Nullable
        public TNBlockType mossy()
        {
            return switch (this)
            {
                case COBBLED_BRICKS, MOSSY_COBBLED_BRICKS -> MOSSY_COBBLED_BRICKS;
                case FLAGSTONE_BRICKS, MOSSY_FLAGSTONE_BRICKS -> MOSSY_FLAGSTONE_BRICKS;
                case ROCK_PILE, MOSSY_ROCK_PILE -> MOSSY_ROCK_PILE;
                case MORTAR_AND_COBBLE -> MOSSY_MORTAR_AND_COBBLE;
                default -> null;
            };
        }

        @Nullable
        public static Map<?, RegistryObject<Block>> transformRock(RegistryRock rock)
        {
            if (rock instanceof Rock)
            {
                return TerraNovaBlocks.ROCK_BLOCKS_SPECIAL_TFC.get(rock);
            }
            else
            {
                return TerraNovaBlocks.ROCK_BLOCKS_SPECIAL.get(rock);
            }
        }

        public SlabBlock createSlab(RegistryRock rock)
        {
            final RegistryObject<? extends SlabBlock> slab = isTFCRock(rock) ? TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL_TFC.get(rock).get(this).slab() : TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL.get(rock).get(this).slab();
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
            final TNBlockType mossy = mossy();
            if (mossy == this)
            {
                return new MossSpreadingSlabBlock(properties);
            }
            else if (mossy != null)
            {
                return new MossGrowingSlabBlock(properties, slab);
            }
            return new SlabBlock(properties);
        }

        public StairBlock createStairs(RegistryRock rock)
        {
            final Supplier<BlockState> state = () -> isTFCRock(rock) ? TerraNovaBlocks.ROCK_BLOCKS_SPECIAL_TFC.get(rock).get(this).get().defaultBlockState() : TerraNovaBlocks.ROCK_BLOCKS_SPECIAL.get(rock).get(this).get().defaultBlockState();
            final RegistryObject<? extends StairBlock> stair = isTFCRock(rock) ? TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL_TFC.get(rock).get(this).stair() : TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL.get(rock).get(this).stair();

            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
            final TNBlockType mossy = mossy();
            if (mossy == this)
            {
                return new MossSpreadingStairBlock(state, properties);
            }
            else if (mossy != null)
            {
                return new MossGrowingStairsBlock(state, properties, stair);
            }
            return new StairBlock(state, properties);
        }

        public WallBlock createWall(RegistryRock rock)
        {
            final RegistryObject<? extends WallBlock> wall = isTFCRock(rock) ? TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL_TFC.get(rock).get(this).wall() : TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL.get(rock).get(this).wall();
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 10).requiresCorrectToolForDrops();
            final TNBlockType mossy = mossy();
            if (mossy == this)
            {
                return new MossSpreadingWallBlock(properties);
            }
            else if (mossy != null)
            {
                return new MossGrowingWallBlock(properties, wall);
            }
            return new WallBlock(properties);
        }
    }

    public static void registerDefaultRocks()
    {
        for (TerraNovaRock rock : TerraNovaRock.values())
        {
            final ResourceLocation id = TNHelpers.identifier(rock.getSerializedName());
            final Map<Rock.BlockType, RegistryObject<Block>> blocks = TerraNovaBlocks.ROCK_BLOCKS.get(rock);

            RockSettings.register(id, new RockSettings(
                blocks.get(Rock.BlockType.RAW).get(),
                blocks.get(Rock.BlockType.HARDENED).get(),
                blocks.get(Rock.BlockType.GRAVEL).get(),
                blocks.get(Rock.BlockType.COBBLE).get(),
                rock.getSandType().get(),
                rock.getSandstone().get(),
                Optional.of(blocks.get(Rock.BlockType.SPIKE).get()),
                Optional.of(blocks.get(Rock.BlockType.LOOSE).get()),
                Optional.of(blocks.get(Rock.BlockType.MOSSY_LOOSE).get())
            ));
        }
    }
}