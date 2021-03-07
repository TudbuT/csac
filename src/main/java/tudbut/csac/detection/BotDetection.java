package tudbut.csac.detection;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import tudbut.csac.Utils;
import tudbut.obj.TLMap;

import java.util.ArrayList;
import java.util.List;

public class BotDetection {
    static Minecraft mc = Minecraft.getMinecraft();
    static TLMap<Integer, Integer> ticksExisted = new TLMap<>();
    
    public static EntityLivingBase[] digest(List<EntityLivingBase> list) {
        ArrayList<EntityLivingBase> r = new ArrayList<>();
    
        for (int i = 0; i < list.size(); i++) {
            if(ticksExisted.get(list.get(i).getEntityId()) >= 60) {
                r.add(list.get(i));
            }
        }
        
        return r.toArray(new EntityLivingBase[0]);
    }
    
    public static void onTick() {
        EntityLivingBase[] entities = mc.world.getEntities(EntityLivingBase.class, e -> true).toArray(new EntityLivingBase[0]);
        for (int i = 0; i < entities.length; i++) {
            if(!entities[i].isInvisible() && entities[i].getHeldItemMainhand().getItem() != Items.AIR) {
                ticksExisted.setIfNull(entities[i].getEntityId(), 0);
                ticksExisted.set(entities[i].getEntityId(), ticksExisted.get(entities[i].getEntityId()) + 1);
            }
        }
    }
}
