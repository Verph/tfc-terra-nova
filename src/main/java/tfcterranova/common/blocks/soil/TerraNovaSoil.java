package tfcterranova.common.blocks.soil;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MudBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.devices.DryingBricksBlock;
import net.dries007.tfc.common.blocks.soil.*;
import net.dries007.tfc.util.registry.RegistrySoilVariant;

import tfcterranova.common.blocks.TerraNovaBlocks;
import tfcterranova.common.items.TerraNovaItems;

public enum TerraNovaSoil
{
    /*
     * Register new blocks for only the new soil variants for TFCs own soil block types
     */
    GRASS_PATH((self, variant) -> new PathBlock(dirtProperties().strength(1.5f).sound(SoundType.GRASS), getBlockTN(self, variant))),
    CLAY((self, variant) -> new DirtBlock(dirtProperties().strength(1.5f).sound(SoundType.GRAVEL), getBlockTN(self, variant), getBlockTN(GRASS_PATH, variant), getBlockTN(self, variant))),
    FARMLAND((self, variant) -> new FarmlandBlock(ExtendedProperties.of(MapColor.DIRT).strength(1.3f).sound(SoundType.GRAVEL).isViewBlocking(TFCBlocks::always).isSuffocating(TFCBlocks::always).blockEntity(TFCBlockEntities.FARMLAND), getBlockTN(self, variant))),
    GRASS((self, variant) -> new ConnectedGrassBlock(grassProperties(), getBlockTN(self, variant), getBlockTN(GRASS_PATH, variant), getBlockTN(FARMLAND, variant))),
    CLAY_GRASS((self, variant) -> new ConnectedGrassBlock(grassProperties(), getBlockTN(self, variant), getBlockTN(GRASS_PATH, variant), getBlockTN(FARMLAND, variant))),
    ROOTED_DIRT((self, variant) -> new TFCRootedDirtBlock(dirtProperties().strength(2.0f).sound(SoundType.ROOTED_DIRT), getBlockTN(self, variant))),
    DIRT((self, variant) -> new DirtBlock(dirtProperties().strength(1.4f).sound(SoundType.GRAVEL), getBlockTN(self, variant), getBlockTN(GRASS_PATH, variant), getBlockTN(FARMLAND, variant), getBlockTN(ROOTED_DIRT, variant))),
    MUD((self, variant) -> new MudBlock(dirtProperties().sound(SoundType.MUD).strength(2f).speedFactor(0.8f).isRedstoneConductor(TFCBlocks::always).isViewBlocking(TFCBlocks::always).isSuffocating(TFCBlocks::always).instrument(NoteBlockInstrument.BASEDRUM))),
    MUD_BRICKS((self, variant) -> new Block(dirtProperties().sound(SoundType.MUD_BRICKS).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.6f))),
    DRYING_BRICKS((self, variant) -> new DryingBricksBlock(ExtendedProperties.of(MapColor.DIRT).noCollission().noOcclusion().instabreak().sound(SoundType.STEM).randomTicks().blockEntity(TFCBlockEntities.TICK_COUNTER), variant.getDriedMudBrick())),
    MUDDY_ROOTS((self, variant) -> new RotatedPillarBlock(Block.Properties.copy(Blocks.MUDDY_MANGROVE_ROOTS).strength(4f))),

    /*
     * New soil block types that require both TFCs and the new soil variants
     */
    PACKED_MUD((self, variant) -> new PackedSoilBlock(packedMudProperties().speedFactor(0.8f), getBlockTN(self.transformTN(), variant)), 
                (self, variant) -> new PackedSoilBlock(packedMudProperties().speedFactor(0.8f), getBlockTFC(toTFC(self.transformTN()), variant))),
    MOSSY_PACKED_MUD((self, variant) -> new PackedSoilBlock(packedMudProperties().speedFactor(0.8f), getBlockTN(self.transformTN(), variant)), 
                    (self, variant) -> new PackedSoilBlock(packedMudProperties().speedFactor(0.8f), getBlockTFC(toTFC(self.transformTN()), variant))),
    LOOSE_MUD((self, variant) -> new LooseMudBlock(dirtProperties().strength(0.3F, 0.3F).sound(SoundType.MUD)), 
                (self, variant) -> new LooseMudBlock(dirtProperties().strength(0.3F, 0.3F).sound(SoundType.MUD))),
    MYCELIUM_DIRT((self, variant) -> new TFCRootedDirtBlock(dirtProperties().strength(0.5F).sound(SoundType.NETHER_WART).instrument(NoteBlockInstrument.BASEDRUM), getBlockTN(self.transformTN(), variant)),
                    (self, variant) -> new TFCRootedDirtBlock(dirtProperties().strength(0.5F).sound(SoundType.NETHER_WART).instrument(NoteBlockInstrument.BASEDRUM), getBlockTFC(toTFC(self.transformTN()), variant))),
    COARSE_DIRT((self, variant) -> new PackedSoilBlock(dirtProperties().strength(0.5F).sound(SoundType.GRAVEL).instrument(NoteBlockInstrument.SNARE), getBlockTN(self.transformTN(), variant)), 
                (self, variant) -> new PackedSoilBlock(dirtProperties().strength(0.5F).sound(SoundType.GRAVEL).instrument(NoteBlockInstrument.SNARE), getBlockTFC(toTFC(self.transformTN()), variant))),
    COMPACT_DIRT((self, variant) -> new PackedSoilBlock(dirtProperties().strength(0.75F).sound(SoundType.TUFF).requiresCorrectToolForDrops(), getBlockTN(self.transformTN(), variant)),
                (self, variant) -> new PackedSoilBlock(dirtProperties().strength(0.75F).sound(SoundType.TUFF).requiresCorrectToolForDrops(), getBlockTFC(toTFC(self.transformTN()), variant))),
    PODZOL((self, variant) -> new PodzolBlock(podzolProperties(), getBlockTN(self, variant), getBlockTN(GRASS_PATH, variant), getBlockTN(FARMLAND, variant)),
            (self, variant) -> new PodzolBlock(podzolProperties(), getBlockTFC(toTFC(self.transformTN()), variant), getBlockTFC(toTFC(GRASS_PATH), variant), getBlockTFC(toTFC(FARMLAND), variant))),

    SPARSE_GRASS_PATH((self, variant) -> new PathBlock(dirtProperties().strength(0.65F).sound(SoundType.GRASS), getBlockTN(self, variant)),
                        (self, variant) -> new PathBlock(dirtProperties().strength(0.65F).sound(SoundType.GRASS), getBlockTFC(toTFC(self), variant))),
    DENSE_GRASS_PATH((self, variant) -> new PathBlock(dirtProperties().strength(0.65F).sound(SoundType.GRASS), getBlockTN(self, variant)),
                        (self, variant) -> new PathBlock(dirtProperties().strength(0.65F).sound(SoundType.GRASS), getBlockTFC(toTFC(self), variant))),
    SPARSE_GRASS((self, variant) -> new ConnectedGrassBlock(grassProperties(), getBlockTN(self, variant), getBlockTN(SPARSE_GRASS_PATH, variant), getBlockTN(FARMLAND, variant)),
                    (self, variant) -> new ConnectedGrassBlock(grassProperties(), getBlockTFC(toTFC(self), variant), getSpecialBlockTFC(SPARSE_GRASS_PATH, variant), getBlockTFC(toTFC(FARMLAND), variant))),
    DENSE_GRASS((self, variant) -> new ConnectedGrassBlock(grassProperties(), getBlockTN(self, variant), getBlockTN(DENSE_GRASS_PATH, variant), getBlockTN(FARMLAND, variant)),
                    (self, variant) -> new ConnectedGrassBlock(grassProperties(), getBlockTFC(toTFC(self), variant), getSpecialBlockTFC(DENSE_GRASS_PATH, variant), getBlockTFC(toTFC(FARMLAND), variant))),
    SPARSE_CLAY_GRASS((self, variant) -> new ConnectedGrassBlock(grassProperties(), getBlockTN(self, variant), getBlockTN(SPARSE_GRASS_PATH, variant), getBlockTN(FARMLAND, variant)),
                        (self, variant) -> new ConnectedGrassBlock(grassProperties(), getBlockTFC(toTFC(self), variant), getSpecialBlockTFC(SPARSE_GRASS_PATH, variant), getBlockTFC(toTFC(FARMLAND), variant))),
    DENSE_CLAY_GRASS((self, variant) -> new ConnectedGrassBlock(grassProperties(), getBlockTN(self, variant), getBlockTN(DENSE_GRASS_PATH, variant), getBlockTN(FARMLAND, variant)),
                        (self, variant) -> new ConnectedGrassBlock(grassProperties(), getBlockTFC(toTFC(self), variant), getSpecialBlockTFC(DENSE_GRASS_PATH, variant), getBlockTFC(toTFC(FARMLAND), variant)));

    /*
     * Make some default properties functions to make the above a little more readable
     */
    public static Block.Properties packedMudProperties()
    {
        return dirtProperties().strength(1.4F, 3.0F).sound(SoundType.PACKED_MUD);
    }

    public static Block.Properties dirtProperties()
    {
        return Block.Properties.of().mapColor(MapColor.DIRT);
    }

    public static Block.Properties podzolProperties()
    {
        return Block.Properties.of().mapColor(MapColor.PODZOL).randomTicks().strength(1.8f).sound(SoundType.NETHER_SPROUTS).instrument(NoteBlockInstrument.SNARE);
    }

    public static Block.Properties grassProperties()
    {
        return Block.Properties.of().mapColor(MapColor.GRASS).randomTicks().strength(1.8f).sound(SoundType.GRASS);
    }

    public static final TerraNovaSoil[] VALUES = values();

    public static TerraNovaSoil valueOf(int i)
    {
        return i >= 0 && i < VALUES.length ? VALUES[i] : DIRT;
    }

    private final BiFunction<TerraNovaSoil, Variant, Block> TNFactory;
    private final BiFunction<TerraNovaSoil, SoilBlockType.Variant, Block> Factory;

    TerraNovaSoil(BiFunction<TerraNovaSoil, Variant, Block> TNFactory, BiFunction<TerraNovaSoil, SoilBlockType.Variant, Block> Factory)
    {
        this.TNFactory = TNFactory;
        this.Factory = Factory;
    }

    TerraNovaSoil(BiFunction<TerraNovaSoil, Variant, Block> TNFactory)
    {
        this.TNFactory = TNFactory;
        this.Factory = null;
    }

    public Block create(SoilBlockType.Variant variant)
    {
        return Factory.apply(this, variant);
    }

    public BiFunction<TerraNovaSoil, SoilBlockType.Variant, Block> getFactory()
    {
        return Factory;
    }

    public Block create(TerraNovaSoil.Variant variant)
    {
        return TNFactory.apply(this, variant);
    }

    public BiFunction<TerraNovaSoil, TerraNovaSoil.Variant, Block> getFactoryTN()
    {
        return TNFactory;
    }

    public static Supplier<? extends Block> getBlockTFC(SoilBlockType soil, SoilBlockType.Variant variant)
    {
        return TFCBlocks.SOIL.get(soil).get(variant);
    }

    public static Supplier<? extends Block> getSpecialBlockTFC(TerraNovaSoil soil, SoilBlockType.Variant variant)
    {
        return TerraNovaBlocks.SOIL_TFC.get(soil).get(variant);
    }

    public static Supplier<? extends Block> getBlockTN(TerraNovaSoil soil, TerraNovaSoil.Variant variant)
    {
        return TerraNovaBlocks.SOIL.get(soil).get(variant);
    }

    public TerraNovaSoil transform()
    {
        switch (this)
        {
            case DIRT:
                return GRASS;
            case GRASS:
            case GRASS_PATH:
            case FARMLAND:
            case ROOTED_DIRT:
            case MUD:
            case MUD_BRICKS:
            case DRYING_BRICKS:
            case MUDDY_ROOTS:
                return DIRT;
            case CLAY:
                return CLAY_GRASS;
            case CLAY_GRASS:
                return CLAY;
            default:
                return this;
        }
    }

    public TerraNovaSoil transformTN()
    {
        switch (this)
        {
            case PACKED_MUD:
            case MOSSY_PACKED_MUD:
                return MUD;
            case DIRT:
                return GRASS;
            case FARMLAND:
            case ROOTED_DIRT:
            case MUD:
            case MUD_BRICKS:
            case DRYING_BRICKS:
            case COMPACT_DIRT:
            case COARSE_DIRT:
            case MYCELIUM_DIRT:
            case PODZOL:
            case SPARSE_GRASS:
            case SPARSE_GRASS_PATH:
                return DIRT;
            case GRASS:
                return DENSE_GRASS;
            case GRASS_PATH:
                return DENSE_GRASS_PATH;
            case DENSE_GRASS:
                return SPARSE_GRASS;
            case DENSE_GRASS_PATH:
                return SPARSE_GRASS_PATH;
            case CLAY:
                return CLAY_GRASS;
            case CLAY_GRASS:
                return DENSE_CLAY_GRASS;
            case DENSE_CLAY_GRASS:
                return SPARSE_CLAY_GRASS;
            case SPARSE_CLAY_GRASS:
                return CLAY;
            default:
                return this;
        }
    }

    public static TerraNovaSoil fromTFC(SoilBlockType type)
    {
        switch (type)
        {
            case DIRT:
                return TerraNovaSoil.DIRT;
            case GRASS:
                return TerraNovaSoil.GRASS;
            case GRASS_PATH:
                return TerraNovaSoil.GRASS_PATH;
            case CLAY:
                return TerraNovaSoil.CLAY;
            case CLAY_GRASS:
                return TerraNovaSoil.CLAY_GRASS;
            case FARMLAND:
                return TerraNovaSoil.FARMLAND;
            case ROOTED_DIRT:
                return TerraNovaSoil.ROOTED_DIRT;
            case MUD:
                return TerraNovaSoil.MUD;
            case MUD_BRICKS:
                return TerraNovaSoil.MUD_BRICKS;
            case DRYING_BRICKS:
                return TerraNovaSoil.DRYING_BRICKS;
            case MUDDY_ROOTS:
                return TerraNovaSoil.MUDDY_ROOTS;
            default:
                return TerraNovaSoil.DIRT;
        }
    }

    public static SoilBlockType toTFC(TerraNovaSoil type)
    {
        switch (type)
        {
            case DIRT:
                return SoilBlockType.DIRT;
            case GRASS:
                return SoilBlockType.GRASS;
            case GRASS_PATH:
                return SoilBlockType.GRASS_PATH;
            case CLAY:
                return SoilBlockType.CLAY;
            case CLAY_GRASS:
                return SoilBlockType.CLAY_GRASS;
            case FARMLAND:
                return SoilBlockType.FARMLAND;
            case ROOTED_DIRT:
                return SoilBlockType.ROOTED_DIRT;
            case MUD:
                return SoilBlockType.MUD;
            case MUD_BRICKS:
                return SoilBlockType.MUD_BRICKS;
            case DRYING_BRICKS:
                return SoilBlockType.DRYING_BRICKS;
            case MUDDY_ROOTS:
                return SoilBlockType.MUDDY_ROOTS;
            default:
                return SoilBlockType.DIRT;
        }
    }

    public SoilBlockType toTFC()
    {
        switch (this)
        {
            case DIRT:
                return SoilBlockType.DIRT;
            case GRASS:
                return SoilBlockType.GRASS;
            case GRASS_PATH:
                return SoilBlockType.GRASS_PATH;
            case CLAY:
                return SoilBlockType.CLAY;
            case CLAY_GRASS:
                return SoilBlockType.CLAY_GRASS;
            case FARMLAND:
                return SoilBlockType.FARMLAND;
            case ROOTED_DIRT:
                return SoilBlockType.ROOTED_DIRT;
            case MUD:
                return SoilBlockType.MUD;
            case MUD_BRICKS:
                return SoilBlockType.MUD_BRICKS;
            case DRYING_BRICKS:
                return SoilBlockType.DRYING_BRICKS;
            case MUDDY_ROOTS:
                return SoilBlockType.MUDDY_ROOTS;
            default:
                return SoilBlockType.DIRT;
        }
    }

    public enum Variant implements RegistrySoilVariant
    {
        /*
         * Typical soil classifications
         * TFC already includes; Loam, Silt, Silty Loam and Sandy Loam
         */
        LOAMY_SAND, // Should probably depend more on the local sand color variant.
        SANDY_CLAY,
        SANDY_CLAY_LOAM,
        CLAY_LOAM,
        SILTY_CLAY_LOAM,
        SILTY_CLAY,

        /*
         * Atypical (unique) soil types
         */
        BOG_IRON, // Iron rich soil found in bogs
        TERRA_PETRA, // Tropical soil high in organic compounds and high in nutrients (high charcoal) -- possible volcanic origin
        TERRA_ROSSA, // Clayey to silty soil in karst rock regions
        CHERNOZEM, // Very fertile steppe or plains soil -- high phosphorus and ammonia
        HUMUS; // High organic concentration; high forest density

        public static final Variant[] VALUES = values();

        public static Variant valueOf(int i)
        {
            return i >= 0 && i < VALUES.length ? VALUES[i] : LOAMY_SAND;
        }

        @Override
        public Supplier<? extends Item> getDriedMudBrick()
        {
            return TerraNovaItems.MUD_BRICK.get(this);
        }

        @Override
        public Supplier<? extends Block> getBlock(SoilBlockType type)
        {
            return TerraNovaBlocks.SOIL.get(fromTFC(type)).get(this);
        }

        public boolean isOrganicSoil()
        {
            switch (this)
            {
                case TERRA_PETRA:
                case TERRA_ROSSA:
                case CHERNOZEM:
                case HUMUS:
                    return true;
                default:
                    return false;
            }
        }
    }
}
