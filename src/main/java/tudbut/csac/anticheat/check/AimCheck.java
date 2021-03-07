package tudbut.csac.anticheat.check;

import net.minecraft.entity.EntityLivingBase;
import tudbut.csac.anticheat.AntiCheat;

public class AimCheck extends Check {
    @Override
    public float check(EntityLivingBase attacked, EntityLivingBase attacker) {
        float f = 0;
    
        float aimDist = AntiCheat.aimDist(attacked, attacker);
        System.out.println(aimDist);
        f += aimDist / (360);
        
        return f;
    }
}
