package tfcterranova.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;

public class TerraNovaFarmlandBlockEntity extends FarmlandBlockEntity
{
    public TerraNovaFarmlandBlockEntity(BlockPos pos, BlockState state)
    {
        super(pos, state);
    }

    protected TerraNovaFarmlandBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType()
    {
        return TerraNovaBlockEntities.FARMLAND.get();
    }
}
