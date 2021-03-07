package tudbut.csac.anticheat.check;

import net.minecraft.entity.EntityLivingBase;

public abstract class Check {
    
    public abstract float check(EntityLivingBase attacked, EntityLivingBase attacker);
}
