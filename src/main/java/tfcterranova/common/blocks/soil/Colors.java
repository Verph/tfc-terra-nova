package tfcterranova.common.blocks.soil;

import java.awt.*;
import java.util.Locale;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import tfcterranova.common.blocks.TerraNovaBlocks;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.common.blocks.soil.TFCSandBlock;

import static net.minecraft.world.level.material.MapColor.*;

public enum Colors implements StringRepresentable
{
    BLACK(new Color(56, 56, 56).getRGB(), MapColor.TERRACOTTA_BLACK, true, false, true),
    BLUE(new Color(50, 68, 255).getRGB(), MapColor.TERRACOTTA_BLUE, false, true, true),
    BROWN(new Color(112, 113, 89).getRGB(), MapColor.TERRACOTTA_BROWN, true, false, true),
    CYAN(new Color(73, 198, 198).getRGB(), MapColor.TERRACOTTA_CYAN, false, true, false),
    GRAY(new Color(112, 112, 112).getRGB(), MapColor.TERRACOTTA_GRAY, false, true, true),
    GREEN(new Color(106, 116, 81).getRGB(), MapColor.TERRACOTTA_GREEN, true, false, true),
    LIGHT_BLUE(new Color(85, 162, 214).getRGB(), MapColor.TERRACOTTA_LIGHT_BLUE, false, true, false),
    LIGHT_GRAY(new Color(175, 175, 175).getRGB(), MapColor.TERRACOTTA_LIGHT_GRAY, false, true, false),
    LIGHT_GREEN(new Color(136, 132, 61).getRGB(), MapColor.TERRACOTTA_LIGHT_GREEN, false, true, true),
    MAGENTA(new Color(165, 0, 165).getRGB(), MapColor.TERRACOTTA_MAGENTA, false, true, false),
    ORANGE(new Color(190, 109, 56).getRGB(), MapColor.TERRACOTTA_ORANGE, false, true, true),
    PINK(new Color(150, 101, 97).getRGB(), MapColor.TERRACOTTA_PINK, true, false, true),
    PURPLE(new Color(116, 73, 94).getRGB(), MapColor.TERRACOTTA_PURPLE, false, true, true),
    RED(new Color(125, 99, 84).getRGB(), MapColor.TERRACOTTA_RED, true, false, true),
    WHITE(new Color(202, 202, 201).getRGB(), MapColor.TERRACOTTA_WHITE, true, false, true),
    YELLOW(new Color(215, 196, 140).getRGB(), MapColor.TERRACOTTA_YELLOW, true, false, true);

    public static final Colors[] VALUES = values();

    public static Colors valueOf(int i)
    {
        return i >= 0 && i < VALUES.length ? VALUES[i] : BLACK;
    }

    public final int dustColor;
    public final MapColor materialColor;
    public final boolean hasSand;
    public final boolean hasSandNew;
    public final boolean hasLayered;
    private final String serializedName;

    Colors(int dustColor, MapColor materialColor, boolean hasSand, boolean hasSandNew, boolean hasLayered)
    {
        this.dustColor = dustColor;
        this.materialColor = materialColor;
        this.hasSand = hasSand;
        this.hasSandNew = hasSandNew;
        this.hasLayered = hasLayered;
        this.serializedName = name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String toString()
    {
        return serializedName;
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }

    public int getDustColor()
    {
        return dustColor;
    }

    public MapColor getMaterialColor()
    {
        return materialColor;
    }

    public Block create()
    {
        return new TFCSandBlock(getDustColor(), BlockBehaviour.Properties.copy(Blocks.SAND).mapColor(getMaterialColor()).strength(1F).sound(SoundType.ANCIENT_DEBRIS));
    }

    public Block createSandLayer()
    {
        if (hasSandNew())
        {
            return new SandLayerBlock(getDustColor(), ExtendedProperties.of(Blocks.SAND).mapColor(getMaterialColor()).strength(0.1F).requiresCorrectToolForDrops().sound(SoundType.SAND), TerraNovaBlocks.SAND.get(this));
        }
        else
        {
            return new SandLayerBlock(getDustColor(), ExtendedProperties.of(Blocks.SAND).mapColor(getMaterialColor()).strength(0.1F).requiresCorrectToolForDrops().sound(SoundType.SAND), TFCBlocks.SAND.get(this.toSandTFC(true)));
        }
    }

    public boolean hasSandTFC()
    {
        return hasSand;
    }

    public boolean hasSandNew()
    {
        return hasSandNew;
    }

    public boolean hasLayered()
    {
        return hasLayered;
    }

    public static Colors fromMaterialColour(MapColor materialColor)
    {
        if (materialColor == GRASS || materialColor == PLANT || materialColor == COLOR_GREEN || materialColor == EMERALD || materialColor == TERRACOTTA_GREEN)
        {
            return Colors.GREEN;
        }
        else if (materialColor == SAND || materialColor == COLOR_YELLOW || materialColor == GOLD || materialColor == TERRACOTTA_YELLOW || materialColor == RAW_IRON)
        {
            return Colors.YELLOW;
        }
        else if (materialColor == NONE || materialColor == WOOL || materialColor == SNOW || materialColor == QUARTZ || materialColor == TERRACOTTA_WHITE)
        {
            return Colors.WHITE;
        }
        else if (materialColor == FIRE || materialColor == COLOR_ORANGE || materialColor == TERRACOTTA_ORANGE)
        {
            return Colors.ORANGE;
        }
        else if (materialColor == ICE || materialColor == CLAY || materialColor == COLOR_LIGHT_BLUE || materialColor == TERRACOTTA_LIGHT_BLUE)
        {
            return Colors.LIGHT_BLUE;
        }
        else if (materialColor == METAL || materialColor == COLOR_LIGHT_GRAY || materialColor == TERRACOTTA_LIGHT_GRAY)
        {
            return Colors.LIGHT_GRAY;
        }
        else if (materialColor == DIRT || materialColor == WOOD || materialColor == COLOR_BROWN || materialColor == TERRACOTTA_BROWN || materialColor == PODZOL)
        {
            return Colors.BROWN;
        }
        else if (materialColor == STONE || materialColor == COLOR_GRAY || materialColor == TERRACOTTA_GRAY || materialColor == GLOW_LICHEN)
        {
            return Colors.GRAY;
        }
        else if (materialColor == WATER || materialColor == COLOR_BLUE || materialColor == TERRACOTTA_BLUE || materialColor == LAPIS)
        {
            return Colors.BLUE;
        }
        else if (materialColor == COLOR_MAGENTA || materialColor == TERRACOTTA_MAGENTA || materialColor == WARPED_STEM || materialColor == WARPED_HYPHAE)
        {
            return Colors.MAGENTA;
        }
        else if (materialColor == COLOR_LIGHT_GREEN || materialColor == TERRACOTTA_LIGHT_GREEN)
        {
            return Colors.LIGHT_GREEN;
        }
        else if (materialColor == COLOR_PINK || materialColor == TERRACOTTA_PINK)
        {
            return Colors.PINK;
        }
        else if (materialColor == COLOR_CYAN || materialColor == TERRACOTTA_CYAN || materialColor == DIAMOND || materialColor == WARPED_NYLIUM || materialColor == CRIMSON_STEM || materialColor == CRIMSON_HYPHAE)
        {
            return Colors.CYAN;
        }
        else if (materialColor == COLOR_PURPLE || materialColor == TERRACOTTA_PURPLE)
        {
            return Colors.PURPLE;
        }
        else if (materialColor == COLOR_RED || materialColor == TERRACOTTA_RED || materialColor == NETHER || materialColor == CRIMSON_NYLIUM || materialColor == WARPED_WART_BLOCK)
        {
            return Colors.RED;
        }
        else if (materialColor == COLOR_BLACK || materialColor == TERRACOTTA_BLACK || materialColor == DEEPSLATE)
        {
            return Colors.BLACK;
        }
        else
        {
            return Colors.ORANGE;
        }
    }

    public static Colors fromTFC(SandBlockType sandColor)
    {
        switch (sandColor)
        {
            case BROWN:
                return Colors.BROWN;
            case WHITE:
                return Colors.WHITE;
            case BLACK:
                return Colors.BLACK;
            case RED:
                return Colors.RED;
            case YELLOW:
                return Colors.YELLOW;
            case GREEN:
                return Colors.GREEN;
            case PINK:
                return Colors.PINK;
            default:
                return Colors.ORANGE;
        }
    }

    public static Colors nonTFC(Colors sandColor)
    {
        switch (sandColor)
        {
            case BROWN:
            case WHITE:
            case BLACK:
            case RED:
            case YELLOW:
            case GREEN:
            case PINK:
                return Colors.ORANGE;
            default:
                return sandColor;
        }
    }

    public Colors nonTFC()
    {
        switch (this)
        {
            case BROWN:
            case WHITE:
            case BLACK:
            case RED:
            case YELLOW:
            case GREEN:
            case PINK:
                return Colors.ORANGE;
            default:
                return this;
        }
    }

    public static SandBlockType toSandTFC(Colors sandColor, boolean withDefault)
    {
        switch (sandColor)
        {
            case BROWN:
                return SandBlockType.BROWN;
            case WHITE:
                return SandBlockType.WHITE;
            case BLACK:
                return SandBlockType.BLACK;
            case RED:
                return SandBlockType.RED;
            case YELLOW:
                return SandBlockType.YELLOW;
            case GREEN:
                return SandBlockType.GREEN;
            case PINK:
                return SandBlockType.PINK;
            default:
                return withDefault ? SandBlockType.YELLOW : null;
        }
    }

    public SandBlockType toSandTFC(boolean defaults)
    {
        switch (this)
        {
            case BROWN:
                return SandBlockType.BROWN;
            case WHITE:
                return SandBlockType.WHITE;
            case BLACK:
                return SandBlockType.BLACK;
            case RED:
                return SandBlockType.RED;
            case YELLOW:
                return SandBlockType.YELLOW;
            case GREEN:
                return SandBlockType.GREEN;
            case PINK:
                return SandBlockType.PINK;
            default:
                return defaults ? SandBlockType.YELLOW : null;
        }
    }

    public static Colors fromDyeColor(DyeColor dyeColor, boolean withDefault)
    {
        switch (dyeColor)
        {
            case WHITE:
                return Colors.WHITE;
            case ORANGE:
                return Colors.ORANGE;
            case MAGENTA:
                return Colors.MAGENTA;
            case LIGHT_BLUE:
                return Colors.LIGHT_BLUE;
            case YELLOW:
                return Colors.YELLOW;
            case LIME:
                return Colors.LIGHT_GREEN;
            case PINK:
                return Colors.PINK;
            case GRAY:
                return Colors.GRAY;
            case LIGHT_GRAY:
                return Colors.LIGHT_GRAY;
            case CYAN:
                return Colors.CYAN;
            case PURPLE:
                return Colors.PURPLE;
            case BLUE:
                return Colors.BLUE;
            case BROWN:
                return Colors.BROWN;
            case GREEN:
                return Colors.GREEN;
            case RED:
                return Colors.RED;
            case BLACK:
                return Colors.BLACK;
            default:
                return withDefault ? Colors.YELLOW : null;
        }
    }

    public static Colors fromString(String string)
    {
        switch (string)
        {
            case "WHITE":
                return Colors.WHITE;
            case "ORANGE":
                return Colors.ORANGE;
            case "MAGENTA":
                return Colors.MAGENTA;
            case "LIGHT_BLUE":
                return Colors.LIGHT_BLUE;
            case "YELLOW":
                return Colors.YELLOW;
            case "LIME":
                return Colors.LIGHT_GREEN;
            case "PINK":
                return Colors.PINK;
            case "GRAY":
                return Colors.GRAY;
            case "LIGHT_GRAY":
                return Colors.LIGHT_GRAY;
            case "CYAN":
                return Colors.CYAN;
            case "PURPLE":
                return Colors.PURPLE;
            case "BLUE":
                return Colors.BLUE;
            case "BROWN":
                return Colors.BROWN;
            case "GREEN":
                return Colors.GREEN;
            case "RED":
                return Colors.RED;
            case "BLACK":
                return Colors.BLACK;
            default:
                return Colors.YELLOW;
        }
    }

    public boolean isSandTFC(String sandType)
    {
        switch (sandType)
        {
            case "BLUE":
            case "CYAN":
            case "GRAY":
            case "LIGHT_BLUE":
            case "LIGHT_GRAY":
            case "LIGHT_GREEN":
            case "MAGENTA":
            case "ORANGE":
            case "PURPLE":
                return false;
            case "BLACK":
            case "BROWN":
            case "GREEN":
            case "PINK":
            case "RED":
            case "WHITE":
            case "YELLOW":
                return true;
            default:
                return true;
        }
    }
}