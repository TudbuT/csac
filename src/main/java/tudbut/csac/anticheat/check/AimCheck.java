package tudbut.csac.anticheat.check;

import net.minecraft.entity.EntityLivingBase;
import tudbut.csac.anticheat.AntiCheat;

public class AimCheck extends Check {
    @Override
    public float check(EntityLivingBase attacked, EntityLivingBase attacker) {
        float f = 0;
    
        float aimDist = AntiCheat.aimDist(attacked, attacker);
        f += aimDist / 225;
        
        return f;
    }
}
