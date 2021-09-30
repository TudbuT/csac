package tudbut.csac.events;

import com.sun.jna.platform.win32.Wdm;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import tudbut.csac.CSAC;
import tudbut.csac.Utils;
import tudbut.csac.detection.BotDetection;
import tudbut.csac.detection.HitDetection;
import tudbut.csac.ui.GUIReport;
import tudbut.csac.ui.GuiUtilsIngame;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EventHandler {
    public static float tps = 20.0f;
    private static long lastTick = -1;
    private static long joinTime = 0;
    
    public static void onPacket(Packet<?> packet) {
    
        if (packet instanceof SPacketTimeUpdate) {
            long time = System.currentTimeMillis();
            if (lastTick != -1 && new Date().getTime() - joinTime > 5000) {
                long diff = time - lastTick;
                if (diff > 50) {
                    tps = (tps + ((1000f / diff) * 20f)) / 2;
                }
            }
            else {
                tps = 20.0f;
            }
            lastTick = time;
        }
    }
    
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if(CSAC.ingameCheck()) {
            BotDetection.onTick();
            HitDetection.onTick();
        }
    }
    
    @SubscribeEvent
    public static void onHUDRender(RenderGameOverlayEvent.Post event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            GUIReport.render();
            GuiUtilsIngame.draw();
        }
    }
    
    // When the client joins a server
    @SubscribeEvent
    public void onJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        tps = 20.0f;
        lastTick = -1;
        joinTime = new Date().getTime();
    }
}
