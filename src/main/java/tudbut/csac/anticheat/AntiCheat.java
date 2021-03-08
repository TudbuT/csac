package tudbut.csac.anticheat;

import de.tudbut.type.StringArray;
import de.tudbut.type.Vector2d;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import tudbut.csac.ChatUtils;
import tudbut.csac.Utils;
import tudbut.csac.anticheat.check.AimCheck;
import tudbut.csac.anticheat.check.ReachCheck;
import tudbut.obj.TLMap;

import java.util.ArrayList;

public class AntiCheat {
    
    public static TLMap<String, Float> results = new TLMap<>();
    
    public static void onHit(EntityLivingBase attacked, EntityLivingBase[] potentialAttackers) {
        EntityLivingBase mostLikelyAttacker = null;
        float lowestCost = Float.MAX_VALUE;
        
        for (int i = 0; i < potentialAttackers.length; i++) {
            EntityLivingBase e = potentialAttackers[i];
            
            float f = getAttackCost(attacked, e);
            if(f < lowestCost) {
                mostLikelyAttacker = e;
                lowestCost = f;
            }
        }
    
        if (mostLikelyAttacker != null)
            onHit(attacked, mostLikelyAttacker);
    }
    
    public static float aimDist(EntityLivingBase attacked, EntityLivingBase attacker) {
        Vector2d vector2d = Utils.getLegitRotations(attacked.getPositionVector().add(0, 1.5, 0));
        return aimDist((float) vector2d.getX(), (float) vector2d.getY(), attacker.rotationYaw, attacker.rotationPitch);
    }
    
    public static float aimDist(float yaw0, float pitch0, float yaw1, float pitch1) {
        float distX0 = (MathHelper.wrapDegrees(yaw0) + 180) % 360 - (MathHelper.wrapDegrees(yaw1) + 180) % 360;
        float distY0 = (MathHelper.wrapDegrees(pitch0) + 180) % 360 - (MathHelper.wrapDegrees(pitch1) + 180) % 360;
        float distX1 = MathHelper.wrapDegrees(yaw0) - MathHelper.wrapDegrees(yaw1);
        float distY1 = MathHelper.wrapDegrees(pitch0) - MathHelper.wrapDegrees(pitch1);
        float distX = Math.min(Math.abs(distX0), Math.abs(distX1));
        float distY = Math.min(Math.abs(distY0), Math.abs(distY1));
        return (float) Math.sqrt(distX * distX + distY * distY);
    }
    
    private static float getAttackCost(EntityLivingBase attacked, EntityLivingBase attacker) {
        float f = 0;
        
        if (attacker.swingProgress != -1) {
            f += attacker.swingProgress * 50;
        }
        
        f += aimDist(attacked, attacker);
        
        if(attacker.swingProgressInt == -1) {
            f *= 50;
        }
        
        return f;
    }
    
    private static void onHit(EntityLivingBase attacked, EntityLivingBase mostLikelyAttacker) {
        if(mostLikelyAttacker instanceof EntityPlayer) {
            System.out.println(mostLikelyAttacker.getName() + " attacked " + attacked.getName());
            float f = 0;
    
            ArrayList<String> failedChecks = new ArrayList<>();
    
            float f1;
            f1 = new AimCheck().check(attacked, mostLikelyAttacker);
            if(f1 > 0.5)
                failedChecks.add("AimCheck");
            f += f1;
            f1 = new ReachCheck().check(attacked, mostLikelyAttacker);
            if(f1 > 0.5)
                failedChecks.add("ReachCheck");
            f += f1;
    
            f *= 0.5 * 1.5f;
    
            if (f > 0.5) {
                ChatUtils.print("§6[WARN] " + mostLikelyAttacker.getName() + " may be hacking. Failed checks: " + new StringArray(failedChecks.toArray(new String[0])).join(", "));
                results.setIfNull(mostLikelyAttacker.getName(), 0f);
                if(results.get(mostLikelyAttacker.getName()) != -1) {
                    results.set(mostLikelyAttacker.getName(), results.get(mostLikelyAttacker.getName()) + f);
                    if (results.get(mostLikelyAttacker.getName()) > 3) {
                        ChatUtils.print("§c[REPORTING] " + mostLikelyAttacker.getName() + " is very likely to be hacking. Reporting...");
                        results.set(mostLikelyAttacker.getName(), -1f);
                        ChatUtils.simulateSend("/report " + mostLikelyAttacker.getName() + " cheating", false);
                    }
                }
            }
            else {
                if (results.get(mostLikelyAttacker.getName()) != null && results.get(mostLikelyAttacker.getName()) != -1 && results.get(mostLikelyAttacker.getName()) > 0.1) {
                    results.set(mostLikelyAttacker.getName(), results.get(mostLikelyAttacker.getName()) - 0.1f);
                }
            }
        }
    }
}
