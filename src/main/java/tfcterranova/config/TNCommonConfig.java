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

    public final ForgeConfigSpec.IntValue slopeDistanceFelsic;
    public final ForgeConfigSpec.IntValue slopeDistanceSedimentary;
    public final ForgeConfigSpec.IntValue slopeDistanceMetamorphic;
    public final ForgeConfigSpec.IntValue slopeDistanceIntermediate;
    public final ForgeConfigSpec.IntValue slopeDistanceMafic;

    public final ForgeConfigSpec.IntValue dropOffFelsic;
    public final ForgeConfigSpec.IntValue dropOffSedimentary;
    public final ForgeConfigSpec.IntValue dropOffMetamorphic;
    public final ForgeConfigSpec.IntValue dropOffIntermediate;
    public final ForgeConfigSpec.IntValue dropOffMafic;

    public final ForgeConfigSpec.IntValue tickDelayFelsic;
    public final ForgeConfigSpec.IntValue tickDelaySedimentary;
    public final ForgeConfigSpec.IntValue tickDelayMetamorphic;
    public final ForgeConfigSpec.IntValue tickDelayIntermediate;
    public final ForgeConfigSpec.IntValue tickDelayMafic;

    public final EnumMap<TerraNovaRock, ForgeConfigSpec.BooleanValue> toggleRock;

    TNCommonConfig(ConfigBuilder builder)
    {
        builder.push("General");
        toggleExtraBiomes = builder.comment("Enable new biomes?", "Requires a game restart!").define("toggleExtraBiomes", false);

        builder.comment("Felsic rocks contain more silica compounds than mafic, thus \"felsic lava\" has a higher viscosity, flow speed and distance -- like honey.", "");
        builder.comment("Standard slope distance for lava is 2, whilst 4 in the Nether.");
        slopeDistanceFelsic = builder.comment("Slope distance for lava in felsic rocks.").define("slopeDistanceFelsic", 3, 0, Integer.MAX_VALUE);
        slopeDistanceSedimentary = builder.comment("Slope distance for lava in sedimentary rocks.").define("slopeDistanceSedimentary", 4, 0, Integer.MAX_VALUE);
        slopeDistanceMetamorphic = builder.comment("Slope distance for lava in metamorphic rocks.").define("slopeDistanceMetamorphic", 4, 0, Integer.MAX_VALUE);
        slopeDistanceIntermediate = builder.comment("Slope distance for lava in intermediate rocks.").define("slopeDistanceIntermediate", 5, 0, Integer.MAX_VALUE);
        slopeDistanceMafic = builder.comment("Slope distance for lava in mafic rocks.").define("slopeDistanceMafic", 6, 0, Integer.MAX_VALUE);

        builder.comment("Standard drop off for lava is 2, whilst 1 in the Nether.");
        dropOffFelsic = builder.comment("Drop off for lava in felsic rocks.").define("dropOffFelsic", 4, 0, Integer.MAX_VALUE);
        dropOffSedimentary = builder.comment("Drop off for lava in sedimentary rocks.").define("dropOffSedimentary", 3, 0, Integer.MAX_VALUE);
        dropOffMetamorphic = builder.comment("Drop off for lava in metamorphic rocks.").define("dropOffMetamorphic", 3, 0, Integer.MAX_VALUE);
        dropOffIntermediate = builder.comment("Drop off for lava in intermediate rocks.").define("dropOffIntermediate", 2, 0, Integer.MAX_VALUE);
        dropOffMafic = builder.comment("Drop off for lava in mafic rocks.").define("dropOffMafic", 1, 0, Integer.MAX_VALUE);

        builder.comment("Standard tick delay for lava is 30, whilst 10 in the Nether.");
        tickDelayFelsic = builder.comment("Tick delay for lava in felsic rocks.").define("tickDelayFelsic", 40, 0, Integer.MAX_VALUE);
        tickDelaySedimentary = builder.comment("Tick delay for lava in sedimentary rocks.").define("tickDelaySedimentary", 25, 0, Integer.MAX_VALUE);
        tickDelayMetamorphic = builder.comment("Tick delay for lava in metamorphic rocks.").define("tickDelayMetamorphic", 25, 0, Integer.MAX_VALUE);
        tickDelayIntermediate = builder.comment("Tick delay for lava in intermediate rocks.").define("tickDelayIntermediate", 10, 0, Integer.MAX_VALUE);
        tickDelayMafic = builder.comment("Tick delay for lava in mafic rocks.").define("tickDelayMafic", 5, 0, Integer.MAX_VALUE);

        builder.push("Rock configurations");
        builder.comment("ATTENTION: toggling rocks for an existing world WILL remove them!", "Tread wisely.", "");

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
