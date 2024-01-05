package tfcterranova.common.blocks.soil;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MudBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.util.registry.RegistrySoilVariant;

public class PackedSoilBlock extends MudBlock
{
    @Nullable public final Supplier<? extends Block> mud;

    public PackedSoilBlock(Properties properties, @Nullable Supplier<? extends Block> mud)
    {
        super(properties);
        this.mud = mud;
    }

    PackedSoilBlock(Properties properties, SoilBlockType soil, RegistrySoilVariant variant)
    {
        this(properties, variant.getBlock(soil));
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction action, boolean simulate)
    {
        if (context.getItemInHand().canPerformAction(action))
        {
            if (action == ToolActions.HOE_TILL && mud != null)
            {
                return mud.get().defaultBlockState();
            }
        }
        return null;
    }
}
