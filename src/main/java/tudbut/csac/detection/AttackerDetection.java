package tudbut.csac.detection;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import tudbut.csac.anticheat.AntiCheat;

public class AttackerDetection {
    static Minecraft mc = Minecraft.getMinecraft();
    
    public static void onHit(EntityLivingBase entity) {
        AntiCheat.onHit(entity, mc.world.getEntities(EntityLivingBase.class, e -> e.getDistance(entity) < 10 && !e.isEntityEqual(entity)).toArray(new EntityLivingBase[0]));
    }
}
