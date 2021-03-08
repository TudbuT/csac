package tudbut.csac.anticheat.check;

import de.tudbut.type.Vector2d;
import net.minecraft.entity.EntityLivingBase;
import tudbut.csac.anticheat.AntiCheat;
import tudbut.obj.TLMap;

public class AimCheck extends Check {
    static TLMap<Integer, Vector2d> rotationsLastTick = new TLMap<>();
    @Override
    public float check(EntityLivingBase attacked, EntityLivingBase attacker) {
        float f = 0;
    
        float aimScore = (270 - AntiCheat.aimDist(attacked, attacker)) / 270;
        
        if(aimScore < 0)
            aimScore = 0;
        
        rotationsLastTick.setIfNull(attacker.getEntityId(), new Vector2d(attacker.rotationYaw, attacker.rotationPitch));
        Vector2d rot = rotationsLastTick.get(attacker.getEntityId());
        float aimDist = AntiCheat.aimDist((float) rot.getX(), (float) rot.getY(), attacker.rotationYaw, attacker.rotationPitch) / 270;
        rotationsLastTick.set(attacker.getEntityId(), new Vector2d(attacker.rotationYaw, attacker.rotationPitch));
    
        f += aimScore * aimDist;
        
        return f;
    }
}
