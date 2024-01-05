package tfcterranova.common.blocks.soil;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

import org.apache.commons.lang3.function.TriFunction;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.util.registry.RegistryRock;

import tfcterranova.common.blocks.TerraNovaBlocks;
import tfcterranova.common.blocks.rock.TerraNovaRock;

public enum RockSand
{
    PEBBLE((self, sandColor, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.SAND).mapColor(sandColor.getMaterialColor()).strength(0.8F).sound(SoundType.GRAVEL).requiresCorrectToolForDrops(), getRockBlockSource(rock).get(Rock.BlockType.LOOSE), getRockySandBlockSource(self.transform(), sandColor, rock), getSandBlockSource(sandColor))),
    ROCKY((self, sandColor, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.SAND).mapColor(sandColor.getMaterialColor()).strength(0.85F).sound(SoundType.GRAVEL).requiresCorrectToolForDrops(), getRockBlockSource(rock).get(Rock.BlockType.LOOSE), getRockySandBlockSource(self.transform(), sandColor, rock), getRockySandBlockSource(self.transformBack(), sandColor, rock))),
    ROCKIER((self, sandColor, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.SAND).mapColor(sandColor.getMaterialColor()).strength(0.9F).sound(SoundType.TUFF).requiresCorrectToolForDrops(), getRockBlockSource(rock).get(Rock.BlockType.LOOSE), getRockySandBlockSource(self.transform(), sandColor, rock), getRockySandBlockSource(self.transformBack(), sandColor, rock))),
    ROCKIEST((self, sandColor, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.SAND).mapColor(sandColor.getMaterialColor()).strength(1F).sound(SoundType.BASALT).requiresCorrectToolForDrops(), getRockBlockSource(rock).get(Rock.BlockType.LOOSE), getRockBlockSource(rock).get(Rock.BlockType.COBBLE), getRockySandBlockSource(self.transformBack(), sandColor, rock), getSandBlockSource(sandColor), true)),

    SANDIEST_TILES((self, sandColor, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.STONE).mapColor(sandColor.getMaterialColor()).strength(1.25f, 8).sound(SoundType.TUFF).requiresCorrectToolForDrops(), TerraNovaBlocks.SAND_LAYERS.get(sandColor), getSandBlockSource(sandColor), null, getTNRockBlockSource(rock).get(TerraNovaRock.TNBlockType.STONE_TILES), true)),
    SANDIER_TILES((self, sandColor, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.STONE).mapColor(sandColor.getMaterialColor()).strength(1.35f, 9).sound(SoundType.TUFF).requiresCorrectToolForDrops(), TerraNovaBlocks.SAND_LAYERS.get(sandColor), getRockySandBlockSource(self.transform(), sandColor, rock))),
    SANDY_TILES((self, sandColor, rock) -> new RockySoilBlock(Block.Properties.copy(Blocks.STONE).mapColor(sandColor.getMaterialColor()).strength(1.5f, 10).sound(SoundType.TUFF).requiresCorrectToolForDrops(), TerraNovaBlocks.SAND_LAYERS.get(sandColor), getRockySandBlockSource(self.transform(), sandColor, rock)));

    public static final RockSand[] VALUES = values();

    public static RockSand valueOf(int i)
    {
        return i >= 0 && i < VALUES.length ? VALUES[i] : PEBBLE;
    }

    public final TriFunction<RockSand, Colors, RegistryRock, Block> factory;

    RockSand(TriFunction<RockSand, Colors, RegistryRock, Block> factory)
    {
        this.factory = factory;
    }

    public Block create(Colors sandColor, RegistryRock rock)
    {
        return factory.apply(this, sandColor, rock);
    }

    public TriFunction<RockSand, Colors, RegistryRock, Block> getFactory()
    {
        return factory;
    }

    public RockSand transform()
    {
        switch (this)
        {
            case SANDY_TILES:
                return SANDIER_TILES;
            case SANDIER_TILES:
                return SANDIEST_TILES;
            case SANDIEST_TILES:
                return ROCKY;
            case PEBBLE:
                return ROCKY;
            case ROCKY:
                return ROCKIER;
            case ROCKIER:
                return ROCKIEST;
            default:
                return this;
        }
    }

    public RockSand transformBack()
    {
        switch (this)
        {
            case ROCKIEST:
                return ROCKIER;
            case ROCKIER:
                return ROCKY;
            case ROCKY:
                return PEBBLE;
            default:
                return this;
        }
    }

    public static RegistryObject<Block> getRockySandBlockSource(RockSand type, Colors color, RegistryRock rock)
    {
        if (rock instanceof Rock)
        {
            return TerraNovaBlocks.ROCKY_SAND_TFC.get(type).get(color).get(rock);
        }
        else
        {
            return TerraNovaBlocks.ROCKY_SAND.get(type).get(color).get(rock);
        }
    }

    public static Map<?, RegistryObject<Block>> getRockBlockSource(RegistryRock rock)
    {
        if (rock instanceof Rock)
        {
            return TFCBlocks.ROCK_BLOCKS.get(rock);
        }
        else
        {
            return TerraNovaBlocks.ROCK_BLOCKS.get(rock);
        }
    }

    public static Map<?, RegistryObject<Block>> getTNRockBlockSource(RegistryRock rock)
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

    public static RegistryObject<Block> getSandBlockSource(Colors sandColor)
    {
        if (sandColor.hasSandTFC())
        {
            return TFCBlocks.SAND.get(sandColor.toSandTFC(true));
        }
        else
        {
            return TerraNovaBlocks.SAND.get(sandColor);
        }
    }
}
