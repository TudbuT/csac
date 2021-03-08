package tudbut.csac.events;

import com.sun.jna.platform.win32.Wdm;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tudbut.csac.CSAC;
import tudbut.csac.Utils;
import tudbut.csac.detection.BotDetection;
import tudbut.csac.detection.HitDetection;
import tudbut.csac.ui.GUIReport;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EventHandler {
    
    public static void onPacket(Packet<?> packet) {
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
        }
    }
}
