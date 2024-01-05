package tfcterranova.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.dries007.tfc.common.TFCCreativeTabs;
import net.dries007.tfc.common.blocks.DecorationBlockRegistryObject;
import net.dries007.tfc.common.blocks.OreDeposit;
import net.dries007.tfc.common.blocks.SandstoneBlockType;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.SelfTests;

import tfcterranova.TFCTerraNova;
import tfcterranova.common.blocks.TerraNovaBlocks;
import tfcterranova.common.blocks.rock.TerraNovaRock;
import tfcterranova.common.blocks.soil.Colors;
import tfcterranova.common.blocks.soil.RockSand;
import tfcterranova.common.blocks.soil.RockSoil;
import tfcterranova.common.blocks.soil.TNSandstoneBlockType;
import tfcterranova.common.blocks.soil.TerraNovaSoil;

@SuppressWarnings("unused")
public class TNCreativeTabs
{
    public static void onBuildCreativeTab(BuildCreativeModeTabContentsEvent out)
    {
        if (out.getTab() == TFCCreativeTabs.EARTH.tab().get())
        {
            for (TerraNovaSoil type : TerraNovaSoil.values())
            {
                for (SoilBlockType.Variant variant : SoilBlockType.Variant.values())
                {
                    if (type.getFactory() != null)
                    {
                        accept(out, TerraNovaBlocks.SOIL_TFC.get(type).get(variant));
                    }
                }
                for (TerraNovaSoil.Variant variant : TerraNovaSoil.Variant.values())
                {
                    if (type.getFactoryTN() != null)
                    {
                        accept(out, TerraNovaBlocks.SOIL.get(type).get(variant));
                        if (type == TerraNovaSoil.MUD_BRICKS)
                        {
                            accept(out, TerraNovaBlocks.MUD_BRICK_DECORATIONS.get(variant));
                        }
                    }
                }
            }
            for (RockSoil type : RockSoil.values())
            {
                for (SoilBlockType.Variant variant : SoilBlockType.Variant.values())
                {
                    for (Rock rock : Rock.values())
                    {
                        accept(out, TerraNovaBlocks.ROCKY_SOIL_TFC_SV.get(type).get(variant).get(rock));
                    }
                    for (TerraNovaRock rock : TerraNovaRock.values())
                    {
                        if (!rock.isEnabled()) continue;

                        accept(out, TerraNovaBlocks.ROCKY_SOIL_SV.get(type).get(variant).get(rock));
                    }
                }
                for (TerraNovaSoil.Variant variant : TerraNovaSoil.Variant.values())
                {
                    for (Rock rock : Rock.values())
                    {
                        accept(out, TerraNovaBlocks.ROCKY_SOIL_TFC_TV.get(type).get(variant).get(rock));
                    }
                    for (TerraNovaRock rock : TerraNovaRock.values())
                    {
                        if (!rock.isEnabled()) continue;

                        accept(out, TerraNovaBlocks.ROCKY_SOIL_TV.get(type).get(variant).get(rock));
                    }
                }
            }
            for (Colors color : Colors.values())
            {
                if (color.hasSandNew())
                {
                    accept(out, TerraNovaBlocks.SAND.get(color));
                }
                accept(out, TerraNovaBlocks.SAND_LAYERS.get(color));
                accept(out, TerraNovaBlocks.SPARSE_SAND_GRASS.get(color));
                accept(out, TerraNovaBlocks.DENSE_SAND_GRASS.get(color));
                accept(out, TerraNovaBlocks.SAND_GRASS.get(color));

                if (color.hasSandNew())
                {
                    for (SandstoneBlockType type : SandstoneBlockType.values())
                    {
                        accept(out, TerraNovaBlocks.SANDSTONE_TFC.get(color).get(type));
                        accept(out, TerraNovaBlocks.SANDSTONE_TFC_DECORATIONS.get(color).get(type));
                    }
                }
                for (TNSandstoneBlockType type : TNSandstoneBlockType.values())
                {
                    accept(out, TerraNovaBlocks.SANDSTONE.get(color).get(type));
                    accept(out, TerraNovaBlocks.SANDSTONE_DECORATIONS.get(color).get(type));
                }
                for (RockSand type : RockSand.values())
                {
                    for (Rock rock : Rock.values())
                    {
                        accept(out, TerraNovaBlocks.ROCKY_SAND_TFC.get(type).get(color).get(rock));
                    }
                    for (TerraNovaRock rock : TerraNovaRock.values())
                    {
                        if (!rock.isEnabled()) continue;

                        accept(out, TerraNovaBlocks.ROCKY_SAND.get(type).get(color).get(rock));
                    }
                }
            }
        }
        if (out.getTab() == TFCCreativeTabs.ORES.tab().get())
        {
            for (TerraNovaRock rock : TerraNovaRock.values())
            {
                if (!rock.isEnabled()) continue;

                for (Ore ore : Ore.values())
                {
                    if (!ore.isGraded())
                    {
                        accept(out, TerraNovaBlocks.ORES.get(rock).get(ore));
                    }
                    else
                    {
                        for (Ore.Grade grade : Ore.Grade.values())
                        {
                            accept(out, TerraNovaBlocks.GRADED_ORES.get(rock).get(ore).get(grade));
                        }
                    }
                }
                for (OreDeposit ore : OreDeposit.values())
                {
                    accept(out, TerraNovaBlocks.ORE_DEPOSITS.get(rock).get(ore));
                }
            }
        }
        if (out.getTab() == TFCCreativeTabs.ROCKS.tab().get())
        {
            for (TerraNovaRock rock : TerraNovaRock.values())
            {
                if (!rock.isEnabled()) continue;

                for (Rock.BlockType type : Rock.BlockType.values())
                {
                    accept(out, TerraNovaBlocks.ROCK_BLOCKS.get(rock).get(type));
                    if (type.hasVariants())
                    {
                        accept(out, TerraNovaBlocks.ROCK_DECORATIONS.get(rock).get(type));
                    }
                    if ((rock.category() == RockCategory.IGNEOUS_EXTRUSIVE || rock.category() == RockCategory.IGNEOUS_INTRUSIVE) && rock.isEnabled())
                    {
                        accept(out, TerraNovaBlocks.ROCK_ANVILS.get(rock));
                        accept(out, TerraNovaBlocks.MAGMA_BLOCKS.get(rock));
                    }
                }
            }
            for (TerraNovaRock.TNBlockType type : TerraNovaRock.TNBlockType.values())
            {
                for (Rock rock : Rock.values())
                {
                    accept(out, TerraNovaBlocks.ROCK_BLOCKS_SPECIAL_TFC.get(rock).get(type));
                    if (type.hasVariants())
                    {
                        accept(out, TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL_TFC.get(rock).get(type));
                    }
                }
                for (TerraNovaRock rock : TerraNovaRock.values())
                {
                    if (!rock.isEnabled()) continue;

                    accept(out, TerraNovaBlocks.ROCK_BLOCKS_SPECIAL.get(rock).get(type));
                    if (type.hasVariants())
                    {
                        accept(out, TerraNovaBlocks.ROCK_DECORATIONS_SPECIAL.get(rock).get(type));
                    }
                }
            }
        }
    }

    private static void accept(CreativeModeTab.Output out, DecorationBlockRegistryObject decoration)
    {
        out.accept(decoration.stair().get());
        out.accept(decoration.slab().get());
        out.accept(decoration.wall().get());
    }

    public static <T extends ItemLike, R extends Supplier<T>> void accept(CreativeModeTab.Output out, R reg)
    {
        if (reg.get().asItem() == Items.AIR)
        {
            SelfTests.reportExternalError();
            return;
        }
        out.accept(reg.get());
    }
}
