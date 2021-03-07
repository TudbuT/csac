package tudbut.csac.detection;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.HashMap;
import java.util.Map;

public class HitDetection {
    static Map<Entity, Integer> entityHurtTimeLastTick = new HashMap<>();
    static Minecraft mc = Minecraft.getMinecraft();
    
    private static void checkHurtTimes(Map<Entity, Integer> thisTick, Map<Entity, Integer> lastTick) {
        Entity[] entities = thisTick.keySet().toArray(new Entity[0]);
        
        for (int i = 0; i < entities.length; i++) {
            if(lastTick.containsKey(entities[i])) {
                int htT = thisTick.get(entities[i]);
                int htL = lastTick.get(entities[i]);
                
                if(htT > htL) {
                    onNewHit((EntityLivingBase) entities[i]);
                }
            }
        }
    }
    
    private static void onNewHit(EntityLivingBase entity) {
        AttackerDetection.onHit(entity);
    }
    
    private static Map<Entity, Integer> getHurtTimes() {
        Map<Entity, Integer> map = new HashMap<>();
        EntityLivingBase[] entities = mc.world.getEntities(EntityLivingBase.class, e -> true).toArray(new EntityLivingBase[0]);
        
        for (int i = 0; i < entities.length; i++) {
            map.put(entities[i], entities[i].hurtTime);
        }
        
        return map;
    }
    
    public static void onTick() {
        Map<Entity, Integer> hurtTimes = getHurtTimes();
        
        checkHurtTimes(hurtTimes, entityHurtTimeLastTick);
        
        entityHurtTimeLastTick = hurtTimes;
    }
}
