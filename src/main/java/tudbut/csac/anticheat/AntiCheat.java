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
        
        if(mostLikelyAttacker != null)
            onHit(attacked, mostLikelyAttacker);
    }
    
    public static float aimDist(EntityLivingBase attacked, EntityLivingBase attacker)  {
        Vector2d vector2d = Utils.getLegitRotations(attacked.getPositionVector().add(0, 1.5, 0));
        float distX0 = (float) ((vector2d.getX() + 180) % 360 - (MathHelper.wrapDegrees(attacker.rotationYaw) + 180) % 360);
        float distY0 = (float) ((vector2d.getY() + 180) % 360 - (MathHelper.wrapDegrees(attacker.rotationPitch) + 180) % 360);
        float distX1 = (float) (vector2d.getX() - MathHelper.wrapDegrees(attacker.rotationYaw));
        float distY1 = (float) (vector2d.getY() - MathHelper.wrapDegrees(attacker.rotationPitch));
        float distX = Math.min(Math.abs(distX0), Math.abs(distX1));
        float distY = Math.min(Math.abs(distY0), Math.abs(distY1));
        return (float) Math.sqrt(distX * distX + distY * distY);
    }
    
    private static float getAttackCost(EntityLivingBase attacked, EntityLivingBase attacker) {
        float f = 0;
        
        if(attacker.swingProgress != -1) {
            f += attacker.swingProgress * 50;
        }
        
        Vector2d vector2d = Utils.getLegitRotations(attacked.getPositionVector().add(0, 1.5, 0));
        float distX = (float) (vector2d.getX() - (MathHelper.wrapDegrees(attacker.rotationYaw)));
        float distY = (float) (vector2d.getY() - (MathHelper.wrapDegrees(attacker.rotationPitch)));
        float dist = (float) Math.sqrt(distX * distX + distY * distY);
        f += dist;
        
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
                results.set(mostLikelyAttacker.getName(), results.get(mostLikelyAttacker.getName()) + f);
                if(results.get(mostLikelyAttacker.getName()) > 7.5) {
                    ChatUtils.print("§c[REPORTING] " + mostLikelyAttacker.getName() + " is very likely to be hacking. Reporting...");
                    try {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ChatUtils.simulateSend("/report " + mostLikelyAttacker.getName() + " cheating", false);
                }
            }
        }
    }
}
