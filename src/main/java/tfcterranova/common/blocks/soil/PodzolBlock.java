package tfcterranova.common.blocks.soil;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.plant.PlantRegrowth;
import net.dries007.tfc.common.blocks.soil.*;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import org.jetbrains.annotations.Nullable;

public class PodzolBlock extends ConnectedGrassBlock
{
    private final Supplier<? extends Block> dirt;
    @Nullable private final Supplier<? extends Block> path;
    @Nullable private final Supplier<? extends Block> farmland;

    public PodzolBlock(Properties properties, SoilBlockType dirtType, SoilBlockType.Variant variant)
    {
        this(properties, TFCBlocks.SOIL.get(dirtType).get(variant), TFCBlocks.SOIL.get(SoilBlockType.GRASS_PATH).get(variant), TFCBlocks.SOIL.get(SoilBlockType.FARMLAND).get(variant));
    }

    public PodzolBlock(Properties properties, Supplier<? extends Block> dirt, @Nullable Supplier<? extends Block> path, @Nullable Supplier<? extends Block> farmland)
    {
        super(properties.hasPostProcess(TFCBlocks::always), dirt, path, farmland);

        this.dirt = dirt;
        this.path = path;
        this.farmland = farmland;

        registerDefaultState(stateDefinition.any().setValue(SOUTH, false).setValue(EAST, false).setValue(NORTH, false).setValue(WEST, false).setValue(SNOWY, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
    {
        if (facing == Direction.UP)
        {
            return stateIn.setValue(SNOWY, Helpers.isBlock(facingState, TFCTags.Blocks.SNOW));
        }
        else if (facing != Direction.DOWN)
        {
            return updateStateFromDirection(level, currentPos, stateIn, facing);
        }
        return stateIn;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        level.scheduleTick(pos, this, 0);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            level.scheduleTick(pos.relative(direction).above(), this, 0);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            level.scheduleTick(pos.relative(direction).above(), this, 0);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (!canBeGrass(state, level, pos))
        {
            if (level.isAreaLoaded(pos, 3))
            {
                // Turn to not-grass
                level.setBlockAndUpdate(pos, getDirt());
            }
        }
        else
        {
            PlantRegrowth.placeRisingRock(level, pos.above(), random);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
    {
        level.setBlock(pos, updateStateFromNeighbors(level, pos, state), 2);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState stateUp = context.getLevel().getBlockState(context.getClickedPos().above());
        return updateStateFromNeighbors(context.getLevel(), context.getClickedPos(), defaultBlockState()).setValue(SNOWY, Helpers.isBlock(stateUp, Blocks.SNOW_BLOCK) || Helpers.isBlock(stateUp, Blocks.SNOW));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(NORTH, EAST, SOUTH, WEST, SNOWY);
    }

    @Override
    public BlockState getDirt()
    {
        return dirt.get().defaultBlockState();
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction action, boolean simulate)
    {
        if (context.getItemInHand().canPerformAction(action))
        {
            if (action == ToolActions.SHOVEL_FLATTEN && TFCConfig.SERVER.enableGrassPathCreation.get() && path != null)
            {
                return path.get().defaultBlockState();
            }
            if (action == ToolActions.HOE_TILL && farmland != null && TFCConfig.SERVER.enableFarmlandCreation.get() && HoeItem.onlyIfAirAbove(context))
            {
                final BlockState farmlandState = farmland.get().defaultBlockState();
                HoeItem.changeIntoState(farmlandState).accept(context);
                return farmlandState;
            }
        }
        return null;
    }
}