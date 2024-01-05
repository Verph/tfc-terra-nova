package tfcterranova.common.blocks.soil;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

import org.apache.commons.lang3.function.TriFunction;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.util.registry.RegistryRock;

import tfcterranova.common.blocks.TerraNovaBlocks;
import tfcterranova.common.blocks.rock.TerraNovaRock;
import tfcterranova.common.items.TerraNovaItems;

public enum RockSoil
{
    PEBBLE_COMPACT_DIRT((self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.DIRT).mapColor(MapColor.DIRT).strength(0.8F).sound(SoundType.GRAVEL).requiresCorrectToolForDrops(), getRockBlockSource(rock, Rock.BlockType.LOOSE), getRockBlockSource(self.transform(), variant, rock), getSoilBlockSource(variant, SoilBlockType.DIRT)),
                        (self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.DIRT).mapColor(MapColor.DIRT).strength(0.8F).sound(SoundType.GRAVEL).requiresCorrectToolForDrops(), getRockBlockSource(rock, Rock.BlockType.LOOSE), getRockBlockSource(self.transform(), variant, rock), getSoilBlockSource(variant, TerraNovaSoil.DIRT))),
    ROCKY_COMPACT_DIRT((self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.DIRT).mapColor(MapColor.DIRT).strength(0.85F).sound(SoundType.GRAVEL).requiresCorrectToolForDrops(), getRockBlockSource(rock, Rock.BlockType.LOOSE), getRockBlockSource(self.transform(), variant, rock), getRockBlockSource(PEBBLE_COMPACT_DIRT, variant, rock)),
                        (self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.DIRT).mapColor(MapColor.DIRT).strength(0.85F).sound(SoundType.GRAVEL).requiresCorrectToolForDrops(), getRockBlockSource(rock, Rock.BlockType.LOOSE), getRockBlockSource(self.transform(), variant, rock), getRockBlockSource(PEBBLE_COMPACT_DIRT, variant, rock))),
    ROCKIER_COMPACT_DIRT((self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.DIRT).mapColor(MapColor.DIRT).strength(0.9F).sound(SoundType.TUFF).requiresCorrectToolForDrops(), getRockBlockSource(rock, Rock.BlockType.LOOSE), getRockBlockSource(self.transform(), variant, rock), getRockBlockSource(ROCKY_COMPACT_DIRT, variant, rock)),
                        (self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.DIRT).mapColor(MapColor.DIRT).strength(0.9F).sound(SoundType.TUFF).requiresCorrectToolForDrops(), getRockBlockSource(rock, Rock.BlockType.LOOSE), getRockBlockSource(self.transform(), variant, rock), getRockBlockSource(ROCKY_COMPACT_DIRT, variant, rock))),
    ROCKIEST_COMPACT_DIRT((self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.DIRT).mapColor(MapColor.DIRT).strength(1F).sound(SoundType.BASALT).requiresCorrectToolForDrops(), getRockBlockSource(rock, Rock.BlockType.LOOSE), getRockBlockSource(rock, Rock.BlockType.COBBLE), getRockBlockSource(ROCKIER_COMPACT_DIRT, variant, rock), getSoilBlockSource(variant, SoilBlockType.DIRT), true),
                        (self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.DIRT).mapColor(MapColor.DIRT).strength(1F).sound(SoundType.BASALT).requiresCorrectToolForDrops(), getRockBlockSource(rock, Rock.BlockType.LOOSE), getRockBlockSource(rock, Rock.BlockType.COBBLE), getRockBlockSource(ROCKIER_COMPACT_DIRT, variant, rock), getSoilBlockSource(variant, TerraNovaSoil.DIRT), true)),

    DIRTIEST_STONE_TILES((self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.STONE).mapColor(MapColor.DIRT).strength(1.25f, 8).sound(SoundType.TUFF).requiresCorrectToolForDrops(), getSoilItem(variant), getSoilBlockSource(variant, SoilBlockType.DIRT), getRockBlockSource(rock, TerraNovaRock.TNBlockType.STONE_TILES), true),
                        (self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.STONE).mapColor(MapColor.DIRT).strength(1.25f, 8).sound(SoundType.TUFF).requiresCorrectToolForDrops(), getSoilItem(variant), getSoilBlockSource(variant, TerraNovaSoil.DIRT), getRockBlockSource(rock, TerraNovaRock.TNBlockType.STONE_TILES), true)),
    DIRTIER_STONE_TILES((self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.STONE).mapColor(MapColor.DIRT).strength(1.35f, 9).sound(SoundType.TUFF).requiresCorrectToolForDrops(), getSoilItem(variant), getRockBlockSource(self.transform(), variant, rock)),
                        (self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.STONE).mapColor(MapColor.DIRT).strength(1.35f, 9).sound(SoundType.TUFF).requiresCorrectToolForDrops(), getSoilItem(variant), getRockBlockSource(self.transform(), variant, rock))),
    DIRTY_STONE_TILES((self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.STONE).mapColor(MapColor.DIRT).strength(1.5f, 10).sound(SoundType.TUFF).requiresCorrectToolForDrops(), getSoilItem(variant), getRockBlockSource(self.transform(), variant, rock)),
                        (self, variant, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.STONE).mapColor(MapColor.DIRT).strength(1.5f, 10).sound(SoundType.TUFF).requiresCorrectToolForDrops(), getSoilItem(variant), getRockBlockSource(self.transform(), variant, rock)));

    public static final RockSoil[] VALUES = values();

    public static RockSoil valueOf(int i)
    {
        return i >= 0 && i < VALUES.length ? VALUES[i] : PEBBLE_COMPACT_DIRT;
    }

    public final TriFunction<RockSoil, SoilBlockType.Variant, RegistryRock, Block> factoryTFC;
    public final TriFunction<RockSoil, TerraNovaSoil.Variant, RegistryRock, Block> factoryTN;

    RockSoil(TriFunction<RockSoil, SoilBlockType.Variant, RegistryRock, Block> factoryTFC, TriFunction<RockSoil, TerraNovaSoil.Variant, RegistryRock, Block> factoryTN)
    {
        this.factoryTFC = factoryTFC;
        this.factoryTN = factoryTN;
    }

    public Block create(TerraNovaSoil.Variant variant, RegistryRock rock)
    {
        return factoryTN.apply(this, variant, rock);
    }

    public TriFunction<RockSoil, TerraNovaSoil.Variant, RegistryRock, Block> getFactoryTN()
    {
        return factoryTN;
    }

    public Block create(SoilBlockType.Variant variant, RegistryRock rock)
    {
        return factoryTFC.apply(this, variant, rock);
    }

    public TriFunction<RockSoil, SoilBlockType.Variant, RegistryRock, Block> getFactoryTFC()
    {
        return factoryTFC;
    }

    public RockSoil transform()
    {
        switch (this)
        {
            case DIRTY_STONE_TILES:
                return DIRTIER_STONE_TILES;
            case DIRTIER_STONE_TILES:
                return DIRTIEST_STONE_TILES;
            case DIRTIEST_STONE_TILES:
                return ROCKY_COMPACT_DIRT;
            case PEBBLE_COMPACT_DIRT:
                return ROCKY_COMPACT_DIRT;
            case ROCKY_COMPACT_DIRT:
                return ROCKIER_COMPACT_DIRT;
            case ROCKIER_COMPACT_DIRT:
                return ROCKIEST_COMPACT_DIRT;
            default:
                return this;
        }
    }

    public static RegistryObject<Item> getSoilItem(Object objectSoil)
    {
        if (objectSoil instanceof SoilBlockType.Variant soil)
        {
            return TerraNovaItems.SOIL_PILE_TFC.get(soil);
        }
        else if (objectSoil instanceof TerraNovaSoil.Variant soil)
        {
            return TerraNovaItems.SOIL_PILE.get(soil);
        }
        return null;
    }

    public static RegistryObject<Block> getSoilBlockSource(Object objectType, Object objectSoil)
    {
        if (objectSoil instanceof SoilBlockType.Variant soil)
        {
            if (objectType instanceof SoilBlockType type)
            {
                return TFCBlocks.SOIL.get(type).get(soil);
            }
            else if (objectType instanceof TerraNovaSoil type)
            {
                return TerraNovaBlocks.SOIL_TFC.get(type).get(soil);
            }
        }
        else if (objectType instanceof TerraNovaSoil type && objectSoil instanceof TerraNovaSoil.Variant soil)
        {
            return TerraNovaBlocks.SOIL.get(type).get(soil);
        }
        return null;
    }

    public static RegistryObject<Block> getRockBlockSource(RockSoil type, Object objectSoil, RegistryRock rock)
    {
        if (rock instanceof Rock)
        {
            if (objectSoil instanceof SoilBlockType.Variant soil)
            {
                return TerraNovaBlocks.ROCKY_SOIL_TFC_SV.get(type).get(soil).get(rock);
            }
            else if (objectSoil instanceof TerraNovaSoil.Variant soil)
            {
                return TerraNovaBlocks.ROCKY_SOIL_TFC_TV.get(type).get(soil).get(rock);
            }
        }
        else if (rock instanceof TerraNovaRock)
        {
            if (objectSoil instanceof SoilBlockType.Variant soil)
            {
                return TerraNovaBlocks.ROCKY_SOIL_SV.get(type).get(soil).get(rock);
            }
            else if (objectSoil instanceof TerraNovaSoil.Variant soil)
            {
                return TerraNovaBlocks.ROCKY_SOIL_TV.get(type).get(soil).get(rock);
            }
        }
        return null;
    }

    public static RegistryObject<Block> getRockBlockSource(RegistryRock rock, Object objectType)
    {
        if (rock instanceof Rock)
        {
            if (objectType instanceof Rock.BlockType type)
            {
                return TFCBlocks.ROCK_BLOCKS.get(rock).get(type);
            }
            else if (objectType instanceof TerraNovaRock.TNBlockType type)
            {
                return TerraNovaBlocks.ROCK_BLOCKS_SPECIAL_TFC.get(rock).get(type);
            }
        }
        else if (rock instanceof TerraNovaRock)
        {
            if (objectType instanceof Rock.BlockType type)
            {
                return TerraNovaBlocks.ROCK_BLOCKS.get(rock).get(type);
            }
            else if (objectType instanceof TerraNovaRock.TNBlockType type)
            {
                return TerraNovaBlocks.ROCK_BLOCKS_SPECIAL.get(rock).get(type);
            }
        }
        return null;
    }
}
