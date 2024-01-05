package tfcterranova.client;

import java.util.function.Predicate;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.dries007.tfc.common.blocks.OreDeposit;
import net.dries007.tfc.common.blocks.SandstoneBlockType;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;

import tfcterranova.client.screen.TNAnvilPlanScreen;
import tfcterranova.client.screen.TNAnvilScreen;
import tfcterranova.common.TNCreativeTabs;
import tfcterranova.common.blocks.TerraNovaBlocks;
import tfcterranova.common.blocks.rock.TerraNovaRock;
import tfcterranova.common.blocks.soil.Colors;
import tfcterranova.common.blocks.soil.RockSand;
import tfcterranova.common.blocks.soil.RockSoil;
import tfcterranova.common.blocks.soil.TNSandstoneBlockType;
import tfcterranova.common.blocks.soil.TerraNovaSoil;
import tfcterranova.common.container.*;

public class ClientEventHandler
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEventHandler::registerEntityRenderers);
        bus.addListener(ClientEventHandler::registerLayerDefinitions);
        bus.addListener(TNCreativeTabs::onBuildCreativeTab);
    }

    @SuppressWarnings("deprecation")
    public static void clientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {

            // Screens
            MenuScreens.register(TNContainerTypes.ANVIL.get(), TNAnvilScreen::new);
            MenuScreens.register(TNContainerTypes.ANVIL_PLAN.get(), TNAnvilPlanScreen::new);
        });

        // Render Types
        final RenderType solid = RenderType.solid();
        final RenderType cutout = RenderType.cutout();
        final RenderType cutoutMipped = RenderType.cutoutMipped();
        final RenderType translucent = RenderType.translucent();
        final Predicate<RenderType> ghostBlock = rt -> rt == cutoutMipped || rt == Sheets.translucentCullBlockSheet();

        // Soil blocks
        for (TerraNovaSoil.Variant variant : TerraNovaSoil.Variant.values())
        {
            ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.MUD_BRICK_DECORATIONS.get(variant).slab().get(), cutoutMipped);
            ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.MUD_BRICK_DECORATIONS.get(variant).stair().get(), cutoutMipped);
            ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.MUD_BRICK_DECORATIONS.get(variant).wall().get(), cutoutMipped);
        }
        for (TerraNovaSoil type : TerraNovaSoil.values())
        {
            for (SoilBlockType.Variant variant : SoilBlockType.Variant.values())
            {
                if (TerraNovaBlocks.SOIL_TFC.get(type).get(variant) != null)
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SOIL_TFC.get(type).get(variant).get(), cutoutMipped);
                }
            }
            for (TerraNovaSoil.Variant variant : TerraNovaSoil.Variant.values())
            {
                if (TerraNovaBlocks.SOIL.get(type).get(variant) != null)
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SOIL.get(type).get(variant).get(), cutoutMipped);
                }
            }
        }
        for (RockSoil type : RockSoil.values())
        {
            for (SoilBlockType.Variant variant : SoilBlockType.Variant.values())
            {
                for (Rock rock : Rock.values())
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCKY_SOIL_TFC_SV.get(type).get(variant).get(rock).get(), cutoutMipped);
                }
                for (TerraNovaRock rock : TerraNovaRock.values())
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCKY_SOIL_SV.get(type).get(variant).get(rock).get(), cutoutMipped);
                }
            }
            for (TerraNovaSoil.Variant variant : TerraNovaSoil.Variant.values())
            {
                for (Rock rock : Rock.values())
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCKY_SOIL_TFC_TV.get(type).get(variant).get(rock).get(), cutoutMipped);
                }
                for (TerraNovaRock rock : TerraNovaRock.values())
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCKY_SOIL_TV.get(type).get(variant).get(rock).get(), cutoutMipped);
                }
            }
        }

        // Rock blocks
        for (TerraNovaRock rock : TerraNovaRock.values())
        {
            if (TerraNovaBlocks.ROCK_ANVILS.get(rock) != null)
            {
                ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCK_ANVILS.get(rock).get(), cutoutMipped);
            }
            if (TerraNovaBlocks.MAGMA_BLOCKS.get(rock) != null)
            {
                ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.MAGMA_BLOCKS.get(rock).get(), cutoutMipped);
            }
            for (Rock.BlockType type : Rock.BlockType.values())
            {
                if (TerraNovaBlocks.ROCK_BLOCKS.get(rock).get(type) != null)
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCK_BLOCKS.get(rock).get(type).get(), cutoutMipped);
                }
                if (TerraNovaBlocks.ROCK_DECORATIONS.get(rock).get(type) == null)
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCK_DECORATIONS.get(rock).get(type).slab().get(), cutoutMipped);
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCK_DECORATIONS.get(rock).get(type).stair().get(), cutoutMipped);
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCK_DECORATIONS.get(rock).get(type).wall().get(), cutoutMipped);
                }
            }
            for (TerraNovaRock.TNBlockType type : TerraNovaRock.TNBlockType.values())
            {
                if (TerraNovaBlocks.ROCK_BLOCKS_SPECIAL.get(rock).get(type) == null)
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCK_BLOCKS_SPECIAL.get(rock).get(type).get(), cutoutMipped);
                }
                if (TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL.get(rock).get(type) == null)
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL.get(rock).get(type).slab().get(), cutoutMipped);
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL.get(rock).get(type).stair().get(), cutoutMipped);
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL.get(rock).get(type).wall().get(), cutoutMipped);
                }
            }
        }
        for (Rock rock : Rock.values())
        {
            for (TerraNovaRock.TNBlockType type : TerraNovaRock.TNBlockType.values())
            {
                if (TerraNovaBlocks.ROCK_BLOCKS_SPECIAL_TFC.get(rock).get(type) == null)
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCK_BLOCKS_SPECIAL_TFC.get(rock).get(type).get(), cutoutMipped);
                }
                if (TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL_TFC.get(rock).get(type) == null)
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL_TFC.get(rock).get(type).slab().get(), cutoutMipped);
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL_TFC.get(rock).get(type).stair().get(), cutoutMipped);
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL_TFC.get(rock).get(type).wall().get(), cutoutMipped);
                }
            }
        }

        // Ore blocks
        for (TerraNovaRock rock : TerraNovaRock.values())
        {
            for (Ore ore : Ore.values())
            {
                if (TerraNovaBlocks.ORES.get(rock).get(ore) != null)
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ORES.get(rock).get(ore).get(), cutoutMipped);
                }
                for (Ore.Grade grade : Ore.Grade.values())
                {
                    if (TerraNovaBlocks.GRADED_ORES.get(rock).get(ore).get(grade) != null)
                    {
                        ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.GRADED_ORES.get(rock).get(ore).get(grade).get(), cutoutMipped);
                    }
                }
            }
            for (OreDeposit deposit : OreDeposit.values())
            {
                if (TerraNovaBlocks.ORE_DEPOSITS.get(rock).get(deposit) != null)
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ORE_DEPOSITS.get(rock).get(deposit).get(), cutoutMipped);
                }
            }
        }

        // Sand blocks
        for (Colors color : Colors.values())
        {
            ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SPARSE_SAND_GRASS.get(color).get(), cutoutMipped);
            ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.DENSE_SAND_GRASS.get(color).get(), cutoutMipped);
            ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SAND_GRASS.get(color).get(), cutoutMipped);
            ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SAND_LAYERS.get(color).get(), cutoutMipped);
            if (color.hasSandNew)
            {
                ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SAND.get(color).get(), cutoutMipped);
                for (SandstoneBlockType type : SandstoneBlockType.values())
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SANDSTONE_TFC.get(color).get(type).get(), cutoutMipped);
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SANDSTONE_TFC_DECORATIONS.get(color).get(type).slab().get(), cutoutMipped);
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SANDSTONE_TFC_DECORATIONS.get(color).get(type).stair().get(), cutoutMipped);
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SANDSTONE_TFC_DECORATIONS.get(color).get(type).wall().get(), cutoutMipped);
                }
            }
            for (TNSandstoneBlockType type : TNSandstoneBlockType.values())
            {
                ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SANDSTONE.get(color).get(type).get(), cutoutMipped);
                ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SANDSTONE_DECORATIONS.get(color).get(type).slab().get(), cutoutMipped);
                ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SANDSTONE_DECORATIONS.get(color).get(type).stair().get(), cutoutMipped);
                ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.SANDSTONE_DECORATIONS.get(color).get(type).wall().get(), cutoutMipped);
            }
            for (RockSand type : RockSand.values())
            {
                for (Rock rock : Rock.values())
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCKY_SAND_TFC.get(type).get(color).get(rock).get(), cutoutMipped);
                }
                for (TerraNovaRock rock : TerraNovaRock.values())
                {
                    ItemBlockRenderTypes.setRenderLayer(TerraNovaBlocks.ROCKY_SAND.get(type).get(color).get(rock).get(), cutoutMipped);
                }
            }
        }
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
    }
}
