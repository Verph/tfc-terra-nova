package tfcterranova.common.blocks;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.DecorationBlockRegistryObject;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.dries007.tfc.common.blocks.OreDeposit;
import net.dries007.tfc.common.blocks.OreDepositBlock;
import net.dries007.tfc.common.blocks.SandstoneBlockType;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.TFCMagmaBlock;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockAnvilBlock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.fluids.IFluidLoggable;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistrationHelpers;

import tfcterranova.common.blocks.rock.TerraNovaRock;
import tfcterranova.common.blocks.soil.Colors;
import tfcterranova.common.blocks.soil.RockSand;
import tfcterranova.common.blocks.soil.RockSoil;
import tfcterranova.common.blocks.soil.TNSandstoneBlockType;
import tfcterranova.common.blocks.soil.TerraNovaSoil;
import tfcterranova.common.items.TerraNovaItems;

import static tfcterranova.TFCTerraNova.MOD_ID;

@SuppressWarnings("unused")
public final class TerraNovaBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MOD_ID);

    // Earth

    public static final Map<TerraNovaSoil, Map<SoilBlockType.Variant, RegistryObject<Block>>> SOIL_TFC = Helpers.mapOfKeys(TerraNovaSoil.class, soil -> soil.getFactory() != null, soil ->
        Helpers.mapOfKeys(SoilBlockType.Variant.class, variant ->
            register("soil/" + soil.name() + "/" + variant.name(), () -> soil.create(variant))
        )
    );

    public static final Map<TerraNovaSoil, Map<TerraNovaSoil.Variant, RegistryObject<Block>>> SOIL = Helpers.mapOfKeys(TerraNovaSoil.class, soil -> soil.getFactoryTN() != null, soil ->
        Helpers.mapOfKeys(TerraNovaSoil.Variant.class, variant ->
            register("soil/" + soil.name() + "/" + variant.name(), () -> soil.create(variant))
        )
    );

    public static final Map<TerraNovaSoil.Variant, DecorationBlockRegistryObject> MUD_BRICK_DECORATIONS = Helpers.mapOfKeys(TerraNovaSoil.Variant.class, variant -> new DecorationBlockRegistryObject(
        register(("soil/mud_bricks/" + variant.name() + "_slab"), () -> new SlabBlock(Properties.of().mapColor(MapColor.DIRT).strength(2.6f).sound(SoundType.WART_BLOCK))),
        register(("soil/mud_bricks/" + variant.name() + "_stairs"), () -> new StairBlock(() -> SOIL.get(TerraNovaSoil.MUD_BRICKS).get(variant).get().defaultBlockState(), Properties.of().mapColor(MapColor.DIRT).strength(2.6f).sound(SoundType.WART_BLOCK).instrument(NoteBlockInstrument.BASEDRUM))),
        register(("soil/mud_bricks/" + variant.name() + "_wall"), () -> new WallBlock(Properties.of().mapColor(MapColor.DIRT).strength(2.6f).sound(SoundType.WART_BLOCK)))
    ));

    public static final Map<RockSoil, Map<SoilBlockType.Variant, Map<Rock, RegistryObject<Block>>>> ROCKY_SOIL_TFC_SV = Helpers.mapOfKeys(RockSoil.class, type -> type.getFactoryTFC() != null, type ->
        Helpers.mapOfKeys(SoilBlockType.Variant.class, variant ->
            Helpers.mapOfKeys(Rock.class, rock ->
                register(("soil/" + type.name() + "/" + variant.name() + "/" + rock.name()).toLowerCase(Locale.ROOT), () -> type.create(variant, rock))
            )
        )
    );

    public static final Map<RockSoil, Map<TerraNovaSoil.Variant, Map<Rock, RegistryObject<Block>>>> ROCKY_SOIL_TFC_TV = Helpers.mapOfKeys(RockSoil.class, type -> type.getFactoryTFC() != null, type ->
        Helpers.mapOfKeys(TerraNovaSoil.Variant.class, variant ->
            Helpers.mapOfKeys(Rock.class, rock ->
                register(("soil/" + type.name() + "/" + variant.name() + "/" + rock.name()).toLowerCase(Locale.ROOT), () -> type.create(variant, rock))
            )
        )
    );

    public static final Map<RockSoil, Map<SoilBlockType.Variant, Map<TerraNovaRock, RegistryObject<Block>>>> ROCKY_SOIL_SV = Helpers.mapOfKeys(RockSoil.class, type -> type.getFactoryTN() != null, type ->
        Helpers.mapOfKeys(SoilBlockType.Variant.class, variant ->
            Helpers.mapOfKeys(TerraNovaRock.class, TerraNovaRock::isEnabled, rock ->
                register(("soil/" + type.name() + "/" + variant.name() + "/" + rock.name()).toLowerCase(Locale.ROOT), () -> type.create(variant, rock))
            )
        )
    );

    public static final Map<RockSoil, Map<TerraNovaSoil.Variant, Map<TerraNovaRock, RegistryObject<Block>>>> ROCKY_SOIL_TV = Helpers.mapOfKeys(RockSoil.class, type -> type.getFactoryTN() != null, type ->
        Helpers.mapOfKeys(TerraNovaSoil.Variant.class, variant ->
            Helpers.mapOfKeys(TerraNovaRock.class, TerraNovaRock::isEnabled, rock ->
                register(("soil/" + type.name() + "/" + variant.name() + "/" + rock.name()).toLowerCase(Locale.ROOT), () -> type.create(variant, rock))
            )
        )
    );

    // Sand

    public static final Map<Colors, RegistryObject<Block>> SPARSE_SAND_GRASS = Helpers.mapOfKeys(Colors.class, type -> register(("sand/sparse_grass/" + type.name()), () -> new ConnectedGrassBlock(Properties.of().mapColor(MapColor.GRASS).randomTicks().strength(3.0F).sound(SoundType.BIG_DRIPLEAF), type.hasSandTFC() ? TFCBlocks.SAND.get(type.toSandTFC(true)) : TerraNovaBlocks.SAND.get(type), null, null)));
    public static final Map<Colors, RegistryObject<Block>> DENSE_SAND_GRASS = Helpers.mapOfKeys(Colors.class, type -> register(("sand/dense_grass/" + type.name()), () -> new ConnectedGrassBlock(Properties.of().mapColor(MapColor.GRASS).randomTicks().strength(3.0F).sound(SoundType.BIG_DRIPLEAF), SPARSE_SAND_GRASS.get(type), null, null)));
    public static final Map<Colors, RegistryObject<Block>> SAND_GRASS = Helpers.mapOfKeys(Colors.class, type -> register(("sand/grass/" + type.name()), () -> new ConnectedGrassBlock(Properties.of().mapColor(MapColor.GRASS).randomTicks().strength(3.0F).sound(SoundType.BIG_DRIPLEAF), DENSE_SAND_GRASS.get(type), null, null)));

    public static final Map<Colors, RegistryObject<Block>> SAND = Helpers.mapOfKeys(Colors.class, Colors::hasSandNew, color ->
            register(("sand/" + color.name()), color::create)
    );

    public static final Map<Colors, RegistryObject<Block>> SAND_LAYERS = Helpers.mapOfKeys(Colors.class, color ->
            register(("sand/sand_layer/" + color.name()), color::createSandLayer)
    );

    public static final Map<Colors, Map<SandstoneBlockType, RegistryObject<Block>>> SANDSTONE_TFC = Helpers.mapOfKeys(Colors.class, Colors::hasSandNew, color ->
        Helpers.mapOfKeys(SandstoneBlockType.class, type ->
            register(("sandstone/" + type.name() + "/" + color.name()), () -> new Block(type.properties(color.toSandTFC(true)).mapColor(color.getMaterialColor())))
        )
    );

    public static final Map<Colors, Map<SandstoneBlockType, DecorationBlockRegistryObject>> SANDSTONE_TFC_DECORATIONS = Helpers.mapOfKeys(Colors.class, Colors::hasSandNew, color ->
        Helpers.mapOfKeys(SandstoneBlockType.class, type -> new DecorationBlockRegistryObject(
            register(("sandstone/" + type.name() + "/slab/" + color.name()), () -> new SlabBlock(type.properties(color.toSandTFC(true)).mapColor(color.getMaterialColor()))),
            register(("sandstone/" + type.name() + "/stairs/" + color.name()), () -> new StairBlock(() -> SANDSTONE_TFC.get(color).get(type).get().defaultBlockState(), type.properties(color.toSandTFC(true)).mapColor(color.getMaterialColor()))),
            register(("sandstone/" + type.name() + "/wall/" + color.name()), () -> new WallBlock(type.properties(color.toSandTFC(true)).mapColor(color.getMaterialColor())))
        ))
    );

    public static final Map<Colors, Map<TNSandstoneBlockType, RegistryObject<Block>>> SANDSTONE = Helpers.mapOfKeys(Colors.class, color ->
        Helpers.mapOfKeys(TNSandstoneBlockType.class, type ->
            register(("sandstone/" + type.name() + "/" + color.name()), () -> new Block(type.properties(color).sound(SoundType.ANCIENT_DEBRIS)))
        )
    );

    public static final Map<Colors, Map<TNSandstoneBlockType, DecorationBlockRegistryObject>> SANDSTONE_DECORATIONS = Helpers.mapOfKeys(Colors.class, color ->
        Helpers.mapOfKeys(TNSandstoneBlockType.class, type -> new DecorationBlockRegistryObject(
            register(("sandstone/" + type.name() + "/slab/" + color.name()), () -> new SlabBlock(type.properties(color).sound(SoundType.ANCIENT_DEBRIS))),
            register(("sandstone/" + type.name() + "/stairs/" + color.name()), () -> new StairBlock(() -> SANDSTONE.get(color).get(type).get().defaultBlockState(), type.properties(color).sound(SoundType.ANCIENT_DEBRIS))),
            register(("sandstone/" + type.name() + "/wall/" + color.name()), () -> new WallBlock(type.properties(color).sound(SoundType.ANCIENT_DEBRIS)))
        ))
    );

    public static final Map<RockSand, Map<Colors, Map<Rock, RegistryObject<Block>>>> ROCKY_SAND_TFC = Helpers.mapOfKeys(RockSand.class, type -> type.getFactory() != null, type ->
        Helpers.mapOfKeys(Colors.class, color ->
            Helpers.mapOfKeys(Rock.class, rock ->
                register(("sand/" + type.name() + "/" + color.name() + "/" + rock.name()).toLowerCase(Locale.ROOT), () -> type.create(color, rock))
            )
        )
    );

    public static final Map<RockSand, Map<Colors, Map<TerraNovaRock, RegistryObject<Block>>>> ROCKY_SAND = Helpers.mapOfKeys(RockSand.class, type -> type.getFactory() != null, type ->
        Helpers.mapOfKeys(Colors.class, color ->
            Helpers.mapOfKeys(TerraNovaRock.class, TerraNovaRock::isEnabled, rock ->
                register(("sand/" + type.name() + "/" + color.name() + "/" + rock.name()).toLowerCase(Locale.ROOT), () -> type.create(color, rock))
            )
        )
    );

    // Ores

    public static final Map<TerraNovaRock, Map<Ore, RegistryObject<Block>>> ORES = Helpers.mapOfKeys(TerraNovaRock.class, TerraNovaRock::isEnabled, rock ->
        Helpers.mapOfKeys(Ore.class, ore -> !ore.isGraded(), ore ->
            register(("ore/" + ore.name() + "/" + rock.name()), () -> ore.create(rock))
        )
    );

    public static final Map<TerraNovaRock, Map<Ore, Map<Ore.Grade, RegistryObject<Block>>>> GRADED_ORES = Helpers.mapOfKeys(TerraNovaRock.class, TerraNovaRock::isEnabled, rock ->
        Helpers.mapOfKeys(Ore.class, Ore::isGraded, ore ->
            Helpers.mapOfKeys(Ore.Grade.class, grade ->
                register(("ore/" + grade.name() + "/" + ore.name() + "/" + rock.name()), () -> ore.create(rock))
            )
        )
    );

    public static final Map<TerraNovaRock, Map<OreDeposit, RegistryObject<Block>>> ORE_DEPOSITS = Helpers.mapOfKeys(TerraNovaRock.class, TerraNovaRock::isEnabled, rock ->
        Helpers.mapOfKeys(OreDeposit.class, ore ->
            register("deposit/" + ore.name() + "/" + rock.name(), () -> new Block(Block.Properties.of().mapColor(MapColor.STONE).sound(SoundType.GRAVEL).strength(rock.category().hardness(2.0f))))
        )
    );

    // Rock Stuff

    public static final Map<TerraNovaRock, Map<Rock.BlockType, RegistryObject<Block>>> ROCK_BLOCKS = Helpers.mapOfKeys(TerraNovaRock.class, TerraNovaRock::isEnabled, rock ->
        Helpers.mapOfKeys(Rock.BlockType.class, type ->
            register(("rock/" + type.name() + "/" + rock.name()), () -> type.create(rock))
        )
    );

    public static final Map<TerraNovaRock, Map<Rock.BlockType, DecorationBlockRegistryObject>> ROCK_DECORATIONS = Helpers.mapOfKeys(TerraNovaRock.class, TerraNovaRock::isEnabled, rock ->
        Helpers.mapOfKeys(Rock.BlockType.class, Rock.BlockType::hasVariants, type -> new DecorationBlockRegistryObject(
            register(("rock/" + type.name() + "/" + rock.name()) + "_slab", () -> type.createSlab(rock)),
            register(("rock/" + type.name() + "/" + rock.name()) + "_stairs", () -> type.createStairs(rock)),
            register(("rock/" + type.name() + "/" + rock.name()) + "_wall", () -> type.createWall(rock))
        ))
    );

    public static final Map<TerraNovaRock, RegistryObject<Block>> ROCK_ANVILS = Helpers.mapOfKeys(TerraNovaRock.class, rock -> (rock.category() == RockCategory.IGNEOUS_EXTRUSIVE || rock.category() == RockCategory.IGNEOUS_INTRUSIVE) && rock.isEnabled(), rock ->
        register("rock/anvil/" + rock.name(), () -> new RockAnvilBlock(ExtendedProperties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(2, 10).requiresCorrectToolForDrops().blockEntity(TFCBlockEntities.ANVIL), ROCK_BLOCKS.get(rock).get(Rock.BlockType.RAW)))
    );

    public static final Map<TerraNovaRock, RegistryObject<Block>> MAGMA_BLOCKS = Helpers.mapOfKeys(TerraNovaRock.class, rock -> (rock.category() == RockCategory.IGNEOUS_EXTRUSIVE || rock.category() == RockCategory.IGNEOUS_INTRUSIVE) && rock.isEnabled(), rock ->
        register("rock/magma/" + rock.name(), () -> new TFCMagmaBlock(Properties.of().mapColor(MapColor.NETHER).requiresCorrectToolForDrops().lightLevel(s -> 6).randomTicks().strength(0.5F).isValidSpawn((state, level, pos, type) -> type.fireImmune()).hasPostProcess(TFCBlocks::always)))
    );

    public static final Map<Rock, Map<TerraNovaRock.TNBlockType, RegistryObject<Block>>> ROCK_BLOCKS_SPECIAL_TFC = Helpers.mapOfKeys(Rock.class, rock ->
        Helpers.mapOfKeys(TerraNovaRock.TNBlockType.class, type ->
            register(("rock/" + type.name() + "/" + rock.name()), () -> type.create(rock))
        )
    );

    public static final Map<Rock, Map<TerraNovaRock.TNBlockType, DecorationBlockRegistryObject>> ROCK_DECORATIONS_SPECIAL_TFC = Helpers.mapOfKeys(Rock.class, rock ->
        Helpers.mapOfKeys(TerraNovaRock.TNBlockType.class, TerraNovaRock.TNBlockType::hasVariants, type -> new DecorationBlockRegistryObject(
            register(("rock/" + type.name() + "/" + rock.name()) + "_slab", () -> type.createSlab(rock)),
            register(("rock/" + type.name() + "/" + rock.name()) + "_stairs", () -> type.createStairs(rock)),
            register(("rock/" + type.name() + "/" + rock.name()) + "_wall", () -> type.createWall(rock))
        ))
    );

    public static final Map<TerraNovaRock, Map<TerraNovaRock.TNBlockType, RegistryObject<Block>>> ROCK_BLOCKS_SPECIAL = Helpers.mapOfKeys(TerraNovaRock.class, TerraNovaRock::isEnabled, rock ->
        Helpers.mapOfKeys(TerraNovaRock.TNBlockType.class, type ->
            register(("rock/" + type.name() + "/" + rock.name()), () -> type.create(rock))
        )
    );

    public static final Map<TerraNovaRock, Map<TerraNovaRock.TNBlockType, DecorationBlockRegistryObject>> ROCK_DECORATIONS_SPECIAL = Helpers.mapOfKeys(TerraNovaRock.class, TerraNovaRock::isEnabled, rock ->
        Helpers.mapOfKeys(TerraNovaRock.TNBlockType.class, TerraNovaRock.TNBlockType::hasVariants, type -> new DecorationBlockRegistryObject(
            register(("rock/" + type.name() + "/" + rock.name()) + "_slab", () -> type.createSlab(rock)),
            register(("rock/" + type.name() + "/" + rock.name()) + "_stairs", () -> type.createStairs(rock)),
            register(("rock/" + type.name() + "/" + rock.name()) + "_wall", () -> type.createWall(rock))
        ))
    );

    public static boolean always(BlockState state, BlockGetter level, BlockPos pos)
    {
        return true;
    }

    public static boolean never(BlockState state, BlockGetter level, BlockPos pos)
    {
        return false;
    }

    public static boolean never(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type)
    {
        return false;
    }

    public static ToIntFunction<BlockState> lavaLoggedBlockEmission()
    {
        // This is resolved only at registration time, so we can't use the fast check (.getFluid() == Fluids.LAVA) and we have to use the slow check instead
        return state -> state.getValue(TFCBlockStateProperties.WATER_AND_LAVA).is(((IFluidLoggable) state.getBlock()).getFluidProperty().keyFor(Fluids.LAVA)) ? 15 : 0;
    }

    public static ToIntFunction<BlockState> litBlockEmission(int lightValue)
    {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return RegistrationHelpers.registerBlock(TerraNovaBlocks.BLOCKS, TerraNovaItems.ITEMS, name, blockSupplier, blockItemFactory);
    }
}
