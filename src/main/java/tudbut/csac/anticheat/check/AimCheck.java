package tudbut.csac.anticheat.check;

import de.tudbut.type.Vector2d;
import net.minecraft.entity.EntityLivingBase;
import tudbut.csac.anticheat.AntiCheat;
import tudbut.obj.TLMap;

public class AimCheck extends Check {
    static TLMap<Integer, Vector2d> rotationsLastTick = new TLMap<>();
    public static float rot = 270;
    
    @Override
    public float check(EntityLivingBase attacked, EntityLivingBase attacker) {
        float f = 0;
        
        float aimScore = (rot - AntiCheat.aimDist(attacked, attacker)) / rot;
        
        if(aimScore < 0)
            aimScore = 0;
        
        rotationsLastTick.setIfNull(attacker.getEntityId(), new Vector2d(attacker.rotationYaw, attacker.rotationPitch));
        Vector2d vec = rotationsLastTick.get(attacker.getEntityId());
        float aimDist = AntiCheat.aimDist((float) vec.getX(), (float) vec.getY(), attacker.rotationYaw, attacker.rotationPitch) / rot;
        rotationsLastTick.set(attacker.getEntityId(), new Vector2d(attacker.rotationYaw, attacker.rotationPitch));
    
        f += aimScore * aimDist;
        
        return f;
    }
}
