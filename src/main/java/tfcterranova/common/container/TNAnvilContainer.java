package tfcterranova.common.container;

import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.capabilities.forge.ForgeStep;
import net.dries007.tfc.common.capabilities.forge.Forging;
import net.dries007.tfc.common.capabilities.forge.ForgingCapability;
import net.dries007.tfc.common.container.BlockEntityContainer;
import net.dries007.tfc.common.container.ButtonHandlerContainer;
import net.dries007.tfc.common.container.CallbackSlot;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.dries007.tfc.util.Helpers;

import tfcterranova.common.blockentities.TerraNovaAnvilBlockEntity;

public class TNAnvilContainer extends BlockEntityContainer<TerraNovaAnvilBlockEntity> implements ButtonHandlerContainer
{
    // IDs [0, 7] indicate step buttons
    public static final int PLAN_ID = 8;

    public static TNAnvilContainer create(TerraNovaAnvilBlockEntity anvil, Inventory playerInv, int windowId)
    {
        return new TNAnvilContainer(windowId, anvil).init(playerInv, 41);
    }

    protected TNAnvilContainer(int windowId, TerraNovaAnvilBlockEntity anvil)
    {
        super(TNContainerTypes.ANVIL.get(), windowId, anvil);
    }

    @Override
    public void onButtonPress(int buttonID, @Nullable CompoundTag extraNBT)
    {
        if (buttonID == PLAN_ID)
        {
            final Level level = blockEntity.getLevel();
            if (level != null)
            {
                final ItemStack stack = getSlot(TerraNovaAnvilBlockEntity.SLOT_INPUT_MAIN).getItem();
                if (AnvilRecipe.hasAny(level, stack, blockEntity.getTier()) && player instanceof ServerPlayer serverPlayer)
                {
                    Helpers.openScreen(serverPlayer, blockEntity.planProvider(), blockEntity.getBlockPos());
                }
            }
        }
        else
        {
            final ForgeStep step = ForgeStep.valueOf(buttonID);
            if (player instanceof ServerPlayer serverPlayer && step != null)
            {
                blockEntity.work(serverPlayer, step);
            }
        }
    }

    @Override
    protected void addContainerSlots()
    {
        blockEntity.getCapability(Capabilities.ITEM).ifPresent(handler -> {
            addSlot(new CallbackSlot(blockEntity, handler, TerraNovaAnvilBlockEntity.SLOT_INPUT_MAIN, 31, 68));
            addSlot(new CallbackSlot(blockEntity, handler, TerraNovaAnvilBlockEntity.SLOT_INPUT_SECOND, 13, 68));
            addSlot(new CallbackSlot(blockEntity, handler, TerraNovaAnvilBlockEntity.SLOT_HAMMER, 129, 68));
            addSlot(new CallbackSlot(blockEntity, handler, TerraNovaAnvilBlockEntity.SLOT_CATALYST, 147, 68));
        });
    }

    @Override
    protected boolean moveStack(ItemStack stack, int slotIndex)
    {
        return switch (typeOf(slotIndex))
            {
                case MAIN_INVENTORY, HOTBAR -> !moveItemStackTo(stack, TerraNovaAnvilBlockEntity.SLOT_HAMMER, TerraNovaAnvilBlockEntity.SLOT_CATALYST + 1, false)
                    && !moveItemStackTo(stack, TerraNovaAnvilBlockEntity.SLOT_INPUT_MAIN, TerraNovaAnvilBlockEntity.SLOT_INPUT_SECOND + 1, false);
                case CONTAINER -> {
                    final Level level = blockEntity.getLevel();
                    final Forging forge = ForgingCapability.get(stack);

                    // Shift clicking needs to attempt to clear the recipe on the stack, then restore it if we fail to transfer out
                    AnvilRecipe recipe = null;
                    int target = -1;

                    if (forge != null && level != null)
                    {
                        recipe = forge.getRecipe(level);
                        target = forge.getWorkTarget();
                        forge.clearRecipeIfNotWorked();
                    }

                    // Do the stack movement
                    final boolean result = !moveItemStackTo(stack, containerSlots, slots.size(), false);

                    // And then restore the stack
                    if (!stack.isEmpty() && recipe != null)
                    {
                        forge.setRecipe(recipe, target);
                    }
                    yield result;
                }
            };
    }
}