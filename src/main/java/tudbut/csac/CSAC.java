package tudbut.csac;

import de.tudbut.io.StreamReader;
import de.tudbut.tools.FileRW;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;
import tudbut.csac.anticheat.AntiCheat;
import tudbut.csac.anticheat.check.AimCheck;
import tudbut.csac.anticheat.check.ReachCheck;
import tudbut.csac.detection.HitDetection;
import tudbut.csac.events.EventHandler;
import tudbut.parsing.TCN;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Mod(modid = CSAC.MOD_ID)
public class CSAC {
    public static final String MOD_ID = "csac";
    public static long ping;
    
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
        if(!new File("csac.txt").exists()) {
            try {
                new FileRW("csac.txt").setContent("" +
                                                  "range: -1\n" +
                                                  "    # -1 is infinite, anything else is the maximum\n" +
                                                  "    # distance between you and the battle for it to\n" +
                                                  "    # be recorded\n" +
                                                  "\n" +
                                                  "autoReport: true\n" +
                                                  "\n" +
                                                  "# DO NOT MODIFY UNLESS YOU KNOW WHAT YOU ARE DOING!!!\n" +
                                                  "checks {\n" +
                                                  "\n" +
                                                  "    aim {\n" +
                                                  "        rot: 270\n" +
                                                  "    }\n" +
                                                  "\n" +
                                                  "\n" +
                                                  "    reach {\n" +
                                                  "        max: 4.5\n" +
                                                  "    }\n" +
                                                  "\n" +
                                                  "}");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            TCN tcn = TCN.read(new StreamReader(new FileInputStream("csac.txt")).readAllAsString());
            HitDetection.rad = tcn.getFloat("range");
            AimCheck.rot = tcn.getInteger("checks#aim#rot");
            ReachCheck.mr = tcn.getFloat("checks#reach#max");
            AntiCheat.autoReport = tcn.getBoolean("autoReport");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        new Thread(() -> {
            while (true) {
                ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
                if(serverData != null)
                    ping = Utils.getPingToServer(serverData);
                try {
                    Thread.sleep(10000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
        }).start();
    }
    
    @Mod.EventHandler
    public void onStart(FMLPostInitializationEvent event) {
        Display.setTitle(Display.getTitle() + " + CSAC v1.0.0");
    }
    
    public static boolean ingameCheck() {
        return Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null;
    }
}
