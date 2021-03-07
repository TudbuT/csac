package tudbut.csac.detection;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import tudbut.csac.anticheat.AntiCheat;

public class AttackerDetection {
    static Minecraft mc = Minecraft.getMinecraft();
    
    public static void onHit(EntityLivingBase entity) {
        AntiCheat.onHit(
                entity,
                BotDetection.digest(mc.world.getEntities(
                        EntityLivingBase.class,
                        e ->
                                e.getDistance(entity) < 10 &&
                                e.getEntityId() != mc.player.getEntityId() &&
                                e.getEntityId() != entity.getEntityId() &&
                                e.getHeldItemMainhand().getItem() != Items.BOW
                ))
        );
    }
}
