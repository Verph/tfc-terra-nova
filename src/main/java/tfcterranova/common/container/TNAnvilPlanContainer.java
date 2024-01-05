package tfcterranova.common.container;

import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;

import net.dries007.tfc.common.container.BlockEntityContainer;
import net.dries007.tfc.common.container.ButtonHandlerContainer;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;
import net.dries007.tfc.util.Helpers;

import tfcterranova.common.blockentities.TerraNovaAnvilBlockEntity;

public class TNAnvilPlanContainer extends BlockEntityContainer<TerraNovaAnvilBlockEntity> implements ButtonHandlerContainer
{
    public static TNAnvilPlanContainer create(TerraNovaAnvilBlockEntity anvil, Inventory playerInventory, int windowId)
    {
        return new TNAnvilPlanContainer(windowId, anvil).init(playerInventory, 0);
    }

    protected TNAnvilPlanContainer(int windowId, TerraNovaAnvilBlockEntity anvil)
    {
        super(TNContainerTypes.ANVIL_PLAN.get(), windowId, anvil);
    }

    @Override
    public void onButtonPress(int buttonID, @Nullable CompoundTag extraNBT)
    {
        if (extraNBT != null && player != null)
        {
            final ResourceLocation recipeId = new ResourceLocation(extraNBT.getString("recipe"));
            final AnvilRecipe recipe = Helpers.getRecipes(player.level(), TFCRecipeTypes.ANVIL).get(recipeId);

            blockEntity.chooseRecipe(recipe);

            if (player instanceof ServerPlayer serverPlayer)
            {
                Helpers.openScreen(serverPlayer, blockEntity.anvilProvider(), blockEntity.getBlockPos());
            }
        }
    }
}