package tudbut.csac.events;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityStatus;

public class EventHandler {
    public static void onPacket(Packet<?> packet) {
        System.out.println(packet.getClass());
    }
}
