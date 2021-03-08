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
                                e.getDistance(entity) < 8 &&
                                e.getEntityId() != mc.player.getEntityId() &&
                                e.getEntityId() != entity.getEntityId() &&
                                e.swingProgressInt != -1 &&
                                e.getHeldItemMainhand().getItem() != Items.BOW &&
                                e.getHeldItemMainhand().getItem() != Items.FISHING_ROD &&
                                e.getHeldItemMainhand().getItem() != Items.FIRE_CHARGE &&
                                e.getHeldItemMainhand().getItem() != Items.FIREWORK_CHARGE
                ))
        );
    }
}
