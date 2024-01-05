package tfcterranova.config;

import java.util.EnumMap;

import org.apache.commons.lang3.StringUtils;

import net.minecraftforge.common.ForgeConfigSpec;

import net.dries007.tfc.config.ConfigBuilder;

import tfcterranova.common.blocks.rock.TerraNovaRock;

public class TNCommonConfig
{
    // General
    public final ForgeConfigSpec.BooleanValue toggleExtraBiomes;
    public final EnumMap<TerraNovaRock, ForgeConfigSpec.BooleanValue> toggleRock;

    TNCommonConfig(ConfigBuilder builder)
    {
        builder.push("General");
        toggleExtraBiomes = builder.comment("Enable new biomes?", "Requires a game restart!").define("toggleExtraBiomes", false);

        builder.push("Rock configurations");
        builder.comment("ATTENTION: toggling rocks for an existing world WILL remove them!", "", "");

        toggleRock = new EnumMap<>(TerraNovaRock.class);
        for (TerraNovaRock rock : TerraNovaRock.VALUES)
        {
            final String rockName = StringUtils.capitalize(rock.getSerializedName().replace("_", " "));
            final String valueName = String.format("toggle" + rockName, rock.getSerializedName());
            toggleRock.put(rock, builder.comment(String.format(("Enable " + rockName + " rock?"), rock.getSerializedName())).define(valueName, true));
        }

        builder.pop();
    }
}
