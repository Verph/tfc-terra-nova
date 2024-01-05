package tfcterranova.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;

public class TerraNovaTickCounterBlockEntity extends TickCounterBlockEntity
{
    public static void reset(Level level, BlockPos pos)
    {
        level.getBlockEntity(pos, TerraNovaBlockEntities.TICK_COUNTER.get()).ifPresent(TerraNovaTickCounterBlockEntity::resetCounter);
    }

    public TerraNovaTickCounterBlockEntity(BlockPos pos, BlockState state)
    {
        super(pos, state);
    }

    protected TerraNovaTickCounterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType()
    {
        return TerraNovaBlockEntities.TICK_COUNTER.get();
    }
}
