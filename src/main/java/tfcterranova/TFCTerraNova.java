package tfcterranova;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import tfcterranova.client.ClientEventHandler;
import tfcterranova.common.blockentities.TerraNovaBlockEntities;
import tfcterranova.common.blocks.TerraNovaBlocks;
import tfcterranova.common.container.TNContainerTypes;
import tfcterranova.common.items.TerraNovaItems;
import tfcterranova.config.TNConfig;

@Mod(TFCTerraNova.MOD_ID)
public class TFCTerraNova
{
    public static final String MOD_ID = "tfcterranova";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFCTerraNova()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        TNConfig.init();
        TerraNovaBlocks.BLOCKS.register(bus);
        TerraNovaItems.ITEMS.register(bus);
        TNContainerTypes.CONTAINERS.register(bus);
        TerraNovaBlockEntities.BLOCK_ENTITIES.register(bus);

        TNForgeEventHandler.init();

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEventHandler.init();
        }
    }
}