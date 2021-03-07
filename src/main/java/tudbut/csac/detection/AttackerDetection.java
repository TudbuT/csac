package tudbut.csac.detection;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import tudbut.csac.anticheat.AntiCheat;

public class AttackerDetection {
    static Minecraft mc = Minecraft.getMinecraft();
    
    public static void onHit(EntityLivingBase entity) {
        if(entity.getEntityId() != mc.player.getEntityId())
            AntiCheat.onHit(
                    entity,
                    mc.world.getEntities(
                            EntityLivingBase.class,
                            e ->
                                    e.getDistance(entity) < 10 &&
                                    e.getEntityId() != mc.player.getEntityId() &&
                                    e.getEntityId() != entity.getEntityId()
                    ).toArray(new EntityLivingBase[0])
            );
    }
}
