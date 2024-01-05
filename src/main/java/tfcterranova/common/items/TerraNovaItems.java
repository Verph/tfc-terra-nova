package tfcterranova.common.items;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import tfcterranova.common.blocks.soil.TerraNovaSoil;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Helpers;

import static tfcterranova.TFCTerraNova.MOD_ID;

@SuppressWarnings("unused")
public final class TerraNovaItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);

    // Soil stuff

    public static final Map<SoilBlockType.Variant, RegistryObject<Item>> SOIL_PILE_TFC = Helpers.mapOfKeys(SoilBlockType.Variant.class, variant ->
        register("soil/pile/" + variant.name())
    );

    public static final Map<TerraNovaSoil.Variant, RegistryObject<Item>> SOIL_PILE = Helpers.mapOfKeys(TerraNovaSoil.Variant.class, variant ->
        register("soil/pile/" + variant.name())
    );

    public static final Map<TerraNovaSoil.Variant, RegistryObject<Item>> MUD_BRICK = Helpers.mapOfKeys(TerraNovaSoil.Variant.class, variant -> register("mud_brick/" + variant.name()));

    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
