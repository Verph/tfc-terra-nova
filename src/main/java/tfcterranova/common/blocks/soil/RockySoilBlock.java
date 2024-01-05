package tfcterranova.common.blocks.soil;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.items.ItemHandlerHelper;

import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrySoilVariant;

public class RockySoilBlock extends Block
{
    @Nullable private final Supplier<? extends Block> transformsTo;
    @Nullable private final Supplier<? extends Block> transformsFrom;
    @Nullable private final Supplier<? extends Block> soilVariantDrop;
    @Nullable private final Supplier<?> requiredItem;
    private final Boolean transformsToCobble;

    public RockySoilBlock(Properties properties, @Nullable Supplier<?> requiredItem, @Nullable Supplier<? extends Block> transformsTo)
    {
        super(properties);
        this.transformsTo = transformsTo;
        this.transformsFrom = null;
        this.soilVariantDrop = null;
        this.requiredItem = requiredItem;
        this.transformsToCobble = false;
    }

    public RockySoilBlock(Properties properties, @Nullable Supplier<?> requiredItem, @Nullable Supplier<? extends Block> transformsTo, @Nullable Supplier<? extends Block> transformsFrom)
    {
        super(properties);
        this.transformsTo = transformsTo;
        this.transformsFrom = transformsFrom;
        this.soilVariantDrop = null;
        this.requiredItem = requiredItem;
        this.transformsToCobble = false;
    }

    public RockySoilBlock(Properties properties, @Nullable Supplier<?> requiredItem, @Nullable Supplier<? extends Block> transformsTo, @Nullable Supplier<? extends Block> transformsFrom, Boolean transformsToCobble)
    {
        super(properties);
        this.transformsTo = transformsTo;
        this.transformsFrom = transformsFrom;
        this.soilVariantDrop = null;
        this.requiredItem = requiredItem;
        this.transformsToCobble = transformsToCobble;
    }

    public RockySoilBlock(Properties properties, @Nullable Supplier<?> requiredItem, @Nullable Supplier<? extends Block> transformsTo, @Nullable Supplier<? extends Block> transformsFrom, @Nullable Supplier<? extends Block> soilVariantDrop, Boolean transformsToCobble)
    {
        super(properties);
        this.transformsTo = transformsTo;
        this.transformsFrom = transformsFrom;
        this.soilVariantDrop = soilVariantDrop;
        this.requiredItem = requiredItem;
        this.transformsToCobble = transformsToCobble;
    }

    RockySoilBlock(Properties properties, @Nullable SoilBlockType dirtType, @Nullable RegistrySoilVariant variant, Boolean transformsToCobble)
    {
        this(properties, null, variant.getBlock(dirtType), variant.getBlock(dirtType), variant.getBlock(dirtType), transformsToCobble);
    }

    RockySoilBlock(Properties properties, @Nullable SoilBlockType dirtType, @Nullable RegistrySoilVariant variant)
    {
        this(properties, null, variant.getBlock(dirtType), variant.getBlock(dirtType), variant.getBlock(dirtType), false);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        final ItemStack stack = player.getItemInHand(hand);
        if (requiredItem instanceof Block)
        {
            if (Helpers.isItem(stack, ((Block) requiredItem.get()).asItem()) && transformsTo != null)
            {
                if (transformsToCobble && soilVariantDrop != null)
                {
                    stack.shrink(1);
                    final BlockState block = transformsTo.get().defaultBlockState();
                    level.setBlockAndUpdate(pos, block);
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(soilVariantDrop.get()));
                    Helpers.playSound(level, pos, SoundType.STONE.getPlaceSound());
                    return InteractionResult.SUCCESS;
                }
                else
                {
                    stack.shrink(1);
                    final BlockState block = transformsTo.get().defaultBlockState();
                    level.setBlockAndUpdate(pos, block);
                    Helpers.playSound(level, pos, SoundType.BASALT.getPlaceSound());
                    return InteractionResult.SUCCESS;
                }
            }
        }
        if (requiredItem instanceof Item)
        {
            if (Helpers.isItem(stack, ((Item) requiredItem.get()).asItem()) && transformsTo != null)
            {
                if (transformsToCobble && soilVariantDrop != null)
                {
                    stack.shrink(1);
                    final BlockState block = transformsTo.get().defaultBlockState();
                    level.setBlockAndUpdate(pos, block);
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(soilVariantDrop.get()));
                    Helpers.playSound(level, pos, SoundType.STONE.getPlaceSound());
                    return InteractionResult.SUCCESS;
                }
                else
                {
                    stack.shrink(1);
                    final BlockState block = transformsTo.get().defaultBlockState();
                    level.setBlockAndUpdate(pos, block);
                    Helpers.playSound(level, pos, SoundType.BASALT.getPlaceSound());
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction action, boolean simulate)
    {
        if ((context.getItemInHand().canPerformAction(ToolActions.SHOVEL_FLATTEN) || context.getItemInHand().canPerformAction(ToolActions.HOE_TILL) /*|| context.getItemInHand().getItem() instanceof WalkingCaneItem*/) && transformsFrom != null)
        {
            if (context.getPlayer() != null)
            {
                if (requiredItem instanceof Block)
                {
                    ItemHandlerHelper.giveItemToPlayer(context.getPlayer(), new ItemStack(((Block) requiredItem.get()).asItem()));
                }
                else if (requiredItem instanceof Item)
                {
                    ItemHandlerHelper.giveItemToPlayer(context.getPlayer(), new ItemStack(((Item) requiredItem.get()).asItem()));
                }
            }
            else
            {
                context.getLevel().addFreshEntity(new ItemStack(soilVariantDrop.get()).getEntityRepresentation());
            }
            Helpers.playSound(context.getLevel(), context.getClickedPos(), SoundType.GRAVEL.getBreakSound());
            return transformsFrom.get().defaultBlockState();
        }
        return null;
    }
}
