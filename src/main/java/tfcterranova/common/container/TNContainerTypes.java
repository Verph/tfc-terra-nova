package tfcterranova.common.container;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.container.BlockEntityContainer;
import net.dries007.tfc.common.container.ItemStackContainer;
import net.dries007.tfc.util.registry.RegistrationHelpers;

import tfcterranova.common.blockentities.TerraNovaAnvilBlockEntity;
import tfcterranova.common.blockentities.TerraNovaBlockEntities;

import static tfcterranova.TFCTerraNova.MOD_ID;

import java.util.function.Supplier;

public class TNContainerTypes
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);

    public static final RegistryObject<MenuType<TNAnvilContainer>> ANVIL = TNContainerTypes.<TerraNovaAnvilBlockEntity, TNAnvilContainer>registerBlock("anvil", TerraNovaBlockEntities.ANVIL, TNAnvilContainer::create);
    public static final RegistryObject<MenuType<TNAnvilPlanContainer>> ANVIL_PLAN = TNContainerTypes.<TerraNovaAnvilBlockEntity, TNAnvilPlanContainer>registerBlock("anvil_plan", TerraNovaBlockEntities.ANVIL, TNAnvilPlanContainer::create);

    private static <T extends InventoryBlockEntity<?>, C extends BlockEntityContainer<T>> RegistryObject<MenuType<C>> registerBlock(String name, Supplier<BlockEntityType<T>> type, BlockEntityContainer.Factory<T, C> factory)
    {
        return RegistrationHelpers.registerBlockEntityContainer(CONTAINERS, name, type, factory);
    }

    private static <C extends ItemStackContainer> RegistryObject<MenuType<C>> registerItem(String name, ItemStackContainer.Factory<C> factory)
    {
        return RegistrationHelpers.registerItemStackContainer(CONTAINERS, name, factory);
    }
}
