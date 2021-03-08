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
    
        float aimDist = AntiCheat.aimDist(attacked, attacker);
        System.out.println(aimDist);
        f += aimDist / (360) * 0.75;
        
        rotationsLastTick.setIfNull(attacker.getEntityId(), new Vector2d(attacker.rotationYaw, attacker.rotationPitch));
        Vector2d rot = rotationsLastTick.get(attacker.getEntityId());
        f += AntiCheat.aimDist((float) rot.getX(), (float) rot.getY(), attacker.rotationYaw, attacker.rotationPitch) / 360 * 0.5;
        rotationsLastTick.set(attacker.getEntityId(), new Vector2d(attacker.rotationYaw, attacker.rotationPitch));
    
        f *= 0.5;
        
        return f;
    }
}
