package tudbut.csac;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tudbut.csac.events.EventHandler;

@Mod(modid = CSAC.MOD_ID)
public class CSAC {
    public static final String MOD_ID = "csac";

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
    }
}
