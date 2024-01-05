package tfcterranova.common.blocks.soil;

import java.util.function.Function;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public enum TNSandstoneBlockType
{
    WEATHERED_TERRACOTTA(color -> BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).mapColor(color.getMaterialColor()).instrument(NoteBlockInstrument.BIT).requiresCorrectToolForDrops().strength(1.25F, 4.2F)),
    LAYERED(color -> BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(color.getMaterialColor()).instrument(NoteBlockInstrument.BANJO).requiresCorrectToolForDrops().strength(0.8F, 3F));

    public final Function<Colors, BlockBehaviour.Properties> factory;

    TNSandstoneBlockType(Function<Colors, BlockBehaviour.Properties> factory)
    {
        this.factory = factory;
    }

    public BlockBehaviour.Properties properties(Colors color)
    {
        return factory.apply(color);
    }
}
