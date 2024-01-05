package tfcterranova.common.blockentities;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistrationHelpers;

import tfcterranova.common.blocks.TerraNovaBlocks;
import tfcterranova.common.blocks.soil.TerraNovaSoil;

import static tfcterranova.TFCTerraNova.MOD_ID;

@SuppressWarnings("unused")
public final class TerraNovaBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);

    public static final RegistryObject<BlockEntityType<SandPileBlockEntity>> SAND_PILE = register("sand_pile", SandPileBlockEntity::new, Stream.of(TerraNovaBlocks.SAND_LAYERS).flatMap(sandType -> sandType.values().stream()));
    public static final RegistryObject<BlockEntityType<TerraNovaFarmlandBlockEntity>> FARMLAND = register("farmland", TerraNovaFarmlandBlockEntity::new, TerraNovaBlocks.SOIL.get(TerraNovaSoil.FARMLAND).values().stream());
    public static final RegistryObject<BlockEntityType<TerraNovaAnvilBlockEntity>> ANVIL = register("anvil", TerraNovaAnvilBlockEntity::new, Stream.of(TerraNovaBlocks.ROCK_ANVILS).flatMap(rockType -> rockType.values().stream()));

    public static final RegistryObject<BlockEntityType<TerraNovaTickCounterBlockEntity>> TICK_COUNTER = register("tick_counter", TerraNovaTickCounterBlockEntity::new, Stream.of(
            TerraNovaBlocks.SOIL.get(TerraNovaSoil.DRYING_BRICKS).values()
        ).<Supplier<? extends Block>>flatMap(Helpers::flatten)
    );

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<? extends Block> block)
    {
        return RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, block);
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Stream<? extends Supplier<? extends Block>> blocks)
    {
        return RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, blocks);
    }
}
