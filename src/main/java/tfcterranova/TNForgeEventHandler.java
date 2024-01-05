package tfcterranova;

import java.io.IOException;
import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.util.Helpers;

import tfcterranova.common.blocks.TerraNovaBlocks;
import tfcterranova.common.blocks.rock.TerraNovaRock;
import tfcterranova.common.blocks.soil.Colors;
import tfcterranova.common.blocks.soil.RockSand;
import tfcterranova.common.blocks.soil.RockSoil;
import tfcterranova.common.blocks.soil.TerraNovaSoil;
import tfcterranova.common.items.TerraNovaItems;

import static tfcterranova.TFCTerraNova.*;

public class TNForgeEventHandler
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;
        final IEventBus loader = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(TNForgeEventHandler::onPlayerRightClickBlock);
        loader.addListener(TNForgeEventHandler::onPackFinder);
    }

    public static void onPackFinder(AddPackFindersEvent event)
    {
        try
        {
            if (event.getPackType() == PackType.CLIENT_RESOURCES)
            {
                final IModFile modFile = ModList.get().getModFileById(MOD_ID).getFile();
                final Path resourcePath = modFile.getFilePath();
                try (PathPackResources pack = new PathPackResources(modFile.getFileName() + ":overload", true, resourcePath){

                    private final IModFile file = ModList.get().getModFileById(MOD_ID).getFile();

                    @NotNull
                    @Override
                    protected Path resolve(String @NotNull ... paths)
                    {
                        return file.findResource(paths);
                    }
                })
                {
                    final PackMetadataSection metadata = pack.getMetadataSection(PackMetadataSection.TYPE);
                    if (metadata != null)
                    {
                        LOGGER.info("Injecting TFC Terra Nova override pack");
                        event.addRepositorySource(consumer ->
                            consumer.accept(Pack.readMetaAndCreate("firmalife_data", Component.literal("TFC Terra Nova Resources"), true, id -> pack, PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN))
                        );
                    }
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        final Level level = event.getLevel();
        final BlockPos pos = event.getPos();
        final BlockState state = level.getBlockState(pos);
        final Block block = state.getBlock();

        for (SoilBlockType.Variant variant : SoilBlockType.Variant.values())
        {
            // Mud --> Compact Mud
            if (block == TFCBlocks.SOIL.get(SoilBlockType.MUD).get(variant).get())
            {
                if (event.getHand() == InteractionHand.MAIN_HAND && event.getItemStack().canPerformAction(ToolActions.SHOVEL_FLATTEN))
                {
                    level.setBlockAndUpdate(pos, TerraNovaBlocks.SOIL_TFC.get(TerraNovaSoil.PACKED_MUD).get(variant).get().defaultBlockState());
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            }
            // Soil --> Compact Soil
            if (block == TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(variant).get())
            {
                if (event.getHand() == InteractionHand.MAIN_HAND && event.getItemStack().canPerformAction(ToolActions.SHOVEL_FLATTEN))
                {
                    level.setBlockAndUpdate(pos, TerraNovaBlocks.SOIL_TFC.get(TerraNovaSoil.COMPACT_DIRT).get(variant).get().defaultBlockState());
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            }
        }
        for (TerraNovaSoil.Variant variant : TerraNovaSoil.Variant.values())
        {
            // Soil --> Compact Soil
            if (block == TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(variant).get())
            {
                if (event.getHand() == InteractionHand.MAIN_HAND && event.getItemStack().canPerformAction(ToolActions.SHOVEL_FLATTEN))
                {
                    level.setBlockAndUpdate(pos, TerraNovaBlocks.SOIL.get(TerraNovaSoil.COMPACT_DIRT).get(variant).get().defaultBlockState());
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            }
        }

        // Soil --> Compact Soil
        for (Rock rock : Rock.values())
        {
            // Soil
            for (SoilBlockType.Variant variant : SoilBlockType.Variant.values())
            {
                if (block == TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(variant).get())
                {
                    if (Helpers.isItem(event.getItemStack(), TFCBlocks.ROCK_BLOCKS.get(rock).get(Rock.BlockType.LOOSE).get().asItem()))
                    {
                        event.getItemStack().shrink(1);
                        final BlockState placedBlock = TerraNovaBlocks.ROCKY_SOIL_TFC_SV.get(RockSoil.PEBBLE_COMPACT_DIRT).get(variant).get(rock).get().defaultBlockState();
                        level.setBlockAndUpdate(pos, placedBlock);
                        Helpers.playSound(level, pos, SoundType.BASALT.getPlaceSound());
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        break;
                    }
                }
                if (block == TerraNovaBlocks.ROCK_BLOCKS_SPECIAL_TFC.get(rock).get(TerraNovaRock.TNBlockType.STONE_TILES).get())
                {
                    if (Helpers.isItem(event.getItemStack(), TerraNovaItems.SOIL_PILE_TFC.get(variant).get()))
                    {
                        event.getItemStack().shrink(1);
                        final BlockState placedBlock = TerraNovaBlocks.ROCKY_SOIL_TFC_SV.get(RockSoil.DIRTY_STONE_TILES).get(variant).get(rock).get().defaultBlockState();
                        level.setBlockAndUpdate(pos, placedBlock);
                        Helpers.playSound(level, pos, SoundType.ROOTS.getPlaceSound());
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        break;
                    }
                }
            }
            for (TerraNovaSoil.Variant variant : TerraNovaSoil.Variant.values())
            {
                if (block == TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(variant).get())
                {
                    if (Helpers.isItem(event.getItemStack(), TFCBlocks.ROCK_BLOCKS.get(rock).get(Rock.BlockType.LOOSE).get().asItem()))
                    {
                        event.getItemStack().shrink(1);
                        final BlockState placedBlock = TerraNovaBlocks.ROCKY_SOIL_TFC_TV.get(RockSoil.PEBBLE_COMPACT_DIRT).get(variant).get(rock).get().defaultBlockState();
                        level.setBlockAndUpdate(pos, placedBlock);
                        Helpers.playSound(level, pos, SoundType.BASALT.getPlaceSound());
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        break;
                    }
                }
                if (block == TerraNovaBlocks.ROCK_BLOCKS_SPECIAL_TFC.get(rock).get(TerraNovaRock.TNBlockType.STONE_TILES).get())
                {
                    if (Helpers.isItem(event.getItemStack(), TerraNovaItems.SOIL_PILE.get(variant).get()))
                    {
                        event.getItemStack().shrink(1);
                        final BlockState placedBlock = TerraNovaBlocks.ROCKY_SOIL_TFC_TV.get(RockSoil.DIRTY_STONE_TILES).get(variant).get(rock).get().defaultBlockState();
                        level.setBlockAndUpdate(pos, placedBlock);
                        Helpers.playSound(level, pos, SoundType.ROOTS.getPlaceSound());
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        break;
                    }
                }
            }

            // Sand
            for (Colors sandColor : Colors.values())
            {
                if (block == TFCBlocks.SAND.get(sandColor.toSandTFC(true)).get() || block == TerraNovaBlocks.SAND.get(sandColor.nonTFC()).get())
                {
                    if (Helpers.isItem(event.getItemStack(), TFCBlocks.ROCK_BLOCKS.get(rock).get(Rock.BlockType.LOOSE).get().asItem()))
                    {
                        event.getItemStack().shrink(1);
                        final BlockState placedBlock = TerraNovaBlocks.ROCKY_SAND_TFC.get(RockSand.PEBBLE).get(sandColor).get(rock).get().defaultBlockState();
                        level.setBlockAndUpdate(pos, placedBlock);
                        Helpers.playSound(level, pos, SoundType.BASALT.getPlaceSound());
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        break;
                    }
                }
                else if (block == TerraNovaBlocks.ROCK_BLOCKS_SPECIAL_TFC.get(rock).get(TerraNovaRock.TNBlockType.STONE_TILES).get())
                {
                    if (Helpers.isItem(event.getItemStack(), TerraNovaBlocks.SAND_LAYERS.get(sandColor).get().asItem()))
                    {
                        event.getItemStack().shrink(1);
                        final BlockState placedBlock = TerraNovaBlocks.ROCKY_SAND_TFC.get(RockSand.SANDY_TILES).get(sandColor).get(rock).get().defaultBlockState();
                        level.setBlockAndUpdate(pos, placedBlock);
                        Helpers.playSound(level, pos, SoundType.ROOTS.getPlaceSound());
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        break;
                    }
                }
            }
        }
        for (TerraNovaRock rock : TerraNovaRock.values())
        {
            for (SoilBlockType.Variant variant : SoilBlockType.Variant.values())
            {
                if (block == TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(variant).get())
                {
                    if (Helpers.isItem(event.getItemStack(), TerraNovaBlocks.ROCK_BLOCKS.get(rock).get(Rock.BlockType.LOOSE).get().asItem()))
                    {
                        event.getItemStack().shrink(1);
                        final BlockState placedBlock = TerraNovaBlocks.ROCKY_SOIL_SV.get(RockSoil.PEBBLE_COMPACT_DIRT).get(variant).get(rock).get().defaultBlockState();
                        level.setBlockAndUpdate(pos, placedBlock);
                        Helpers.playSound(level, pos, SoundType.BASALT.getPlaceSound());
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        break;
                    }
                }
                if (block == TerraNovaBlocks.ROCK_BLOCKS_SPECIAL.get(rock).get(TerraNovaRock.TNBlockType.STONE_TILES).get())
                {
                    if (Helpers.isItem(event.getItemStack(), TerraNovaItems.SOIL_PILE_TFC.get(variant).get()))
                    {
                        event.getItemStack().shrink(1);
                        final BlockState placedBlock = TerraNovaBlocks.ROCKY_SOIL_SV.get(RockSoil.DIRTY_STONE_TILES).get(variant).get(rock).get().defaultBlockState();
                        level.setBlockAndUpdate(pos, placedBlock);
                        Helpers.playSound(level, pos, SoundType.ROOTS.getPlaceSound());
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        break;
                    }
                }
            }
            for (TerraNovaSoil.Variant variant : TerraNovaSoil.Variant.values())
            {
                if (block == TerraNovaBlocks.SOIL.get(TerraNovaSoil.DIRT).get(variant).get())
                {
                    if (Helpers.isItem(event.getItemStack(), TerraNovaBlocks.ROCK_BLOCKS.get(rock).get(Rock.BlockType.LOOSE).get().asItem()))
                    {
                        event.getItemStack().shrink(1);
                        final BlockState placedBlock = TerraNovaBlocks.ROCKY_SOIL_TV.get(RockSoil.PEBBLE_COMPACT_DIRT).get(variant).get(rock).get().defaultBlockState();
                        level.setBlockAndUpdate(pos, placedBlock);
                        Helpers.playSound(level, pos, SoundType.BASALT.getPlaceSound());
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        break;
                    }
                }
                if (block == TerraNovaBlocks.ROCK_BLOCKS_SPECIAL.get(rock).get(TerraNovaRock.TNBlockType.STONE_TILES).get())
                {
                    if (Helpers.isItem(event.getItemStack(), TerraNovaItems.SOIL_PILE.get(variant).get()))
                    {
                        event.getItemStack().shrink(1);
                        final BlockState placedBlock = TerraNovaBlocks.ROCKY_SOIL_TV.get(RockSoil.DIRTY_STONE_TILES).get(variant).get(rock).get().defaultBlockState();
                        level.setBlockAndUpdate(pos, placedBlock);
                        Helpers.playSound(level, pos, SoundType.ROOTS.getPlaceSound());
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        break;
                    }
                }
            }

            // Sand
            for (Colors sandColor : Colors.values())
            {
                if (block == TFCBlocks.SAND.get(sandColor.toSandTFC(true)).get() || block == TerraNovaBlocks.SAND.get(sandColor.nonTFC()).get())
                {
                    if (Helpers.isItem(event.getItemStack(), TerraNovaBlocks.ROCK_BLOCKS.get(rock).get(Rock.BlockType.LOOSE).get().asItem()))
                    {
                        event.getItemStack().shrink(1);
                        final BlockState placedBlock = TerraNovaBlocks.ROCKY_SAND.get(RockSand.PEBBLE).get(sandColor).get(rock).get().defaultBlockState();
                        level.setBlockAndUpdate(pos, placedBlock);
                        Helpers.playSound(level, pos, SoundType.BASALT.getPlaceSound());
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        break;
                    }
                }
                else if (block == TerraNovaBlocks.ROCK_BLOCKS_SPECIAL.get(rock).get(TerraNovaRock.TNBlockType.STONE_TILES).get())
                {
                    if (Helpers.isItem(event.getItemStack(), TerraNovaBlocks.SAND_LAYERS.get(sandColor).get().asItem()))
                    {
                        event.getItemStack().shrink(1);
                        final BlockState placedBlock = TerraNovaBlocks.ROCKY_SAND.get(RockSand.SANDY_TILES).get(sandColor).get(rock).get().defaultBlockState();
                        level.setBlockAndUpdate(pos, placedBlock);
                        Helpers.playSound(level, pos, SoundType.ROOTS.getPlaceSound());
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        break;
                    }
                }
            }
        }
        event.setCancellationResult(InteractionResult.PASS);
    }
}
