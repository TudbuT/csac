package tudbut.csac;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;
import tudbut.csac.events.EventHandler;

@Mod(modid = CSAC.MOD_ID)
public class CSAC {
    public static final String MOD_ID = "csac";

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
    }
    
    @Mod.EventHandler
    public void onStart(FMLPostInitializationEvent event) {
        Display.setTitle(Display.getTitle() + " + CSAC v1.0.0");
    }
    
    public static boolean ingameCheck() {
        return Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null;
    }
}
