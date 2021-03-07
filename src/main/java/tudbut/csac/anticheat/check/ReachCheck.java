package tudbut.csac.anticheat.check;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import tudbut.obj.TLMap;


// From tudbut/minecraft_fullReachFix
public class ReachCheck extends Check {
    
    static TLMap<String, PlayerRecord> records = new TLMap<>();
    
    @Override
    public float check(EntityLivingBase attacked, EntityLivingBase attacker) {
        float f = 0;
    
        // Record
        PlayerRecord record;
    
        // Get the record
        if(!records.keys().contains(attacker.getUniqueID().toString())) {
            record = new PlayerRecord();
            records.set(attacker.getUniqueID().toString(), record);
        }
        else {
            record = records.get(attacker.getUniqueID().toString());
        }
    
        // Calculate reach and add it to the record
        record.recordReach(calculateReach(attacker, attacked));
        
        f = record.offenses / 5;
        
        return f;
    }
    
    public float calculateReach(EntityLivingBase source, EntityLivingBase victim) {
        float d = Float.MAX_VALUE;
        
        Vec3d eyes = source.getPositionEyes(0);
        AxisAlignedBB hitBox = victim.getEntityBoundingBox();
        
        // All corners of the hitbox
        Vec3d posLLL = new Vec3d(hitBox.minX, hitBox.minY, hitBox.minZ); // x0 y0 z0
        Vec3d posLLH = new Vec3d(hitBox.minX, hitBox.minY, hitBox.minZ); // x0 y0 z1
        Vec3d posLHL = new Vec3d(hitBox.minX, hitBox.maxY, hitBox.minZ); // x0 y1 z0
        Vec3d posLHH = new Vec3d(hitBox.minX, hitBox.maxY, hitBox.minZ); // x0 y1 z1
        Vec3d posHLL = new Vec3d(hitBox.maxX, hitBox.minY, hitBox.minZ); // x1 y0 z0
        Vec3d posHLH = new Vec3d(hitBox.maxX, hitBox.minY, hitBox.minZ); // x1 y0 z1
        Vec3d posHHL = new Vec3d(hitBox.maxX, hitBox.maxY, hitBox.minZ); // x1 y1 z0
        Vec3d posHHH = new Vec3d(hitBox.maxX, hitBox.maxY, hitBox.minZ); // x1 y1 z1
        
        // Get distance to each corner, the smallest distance is then stored
        d = getDistance(d, eyes, posLLL);
        d = getDistance(d, eyes, posLLH);
        d = getDistance(d, eyes, posLHL);
        d = getDistance(d, eyes, posLHH);
        d = getDistance(d, eyes, posHLL);
        d = getDistance(d, eyes, posHLH);
        d = getDistance(d, eyes, posHHL);
        d = getDistance(d, eyes, posHHH);
        
        // Return smallest distance
        return d;
    }
    
    public float getDistance(float d, Vec3d location, Vec3d vector) {
        // Get smallest value, this distance, or d (prev distance)
        return (float) Math.min(d, location.distanceTo(vector));
    }
    
    public static class PlayerRecord {
        
        // Default reach for a player record
        public float playerReach = 1.5f;
        
        // Reach below configured max?
        public boolean isReachNormal() {
            return playerReach < 4.5;
        }
        
        // Offense counter for plugin
        public float offenses = 0;
        // All offenses that ever happened
        public int allOffenses = 0;
        
        // Called by event handler, records the reach of a hit
        public void recordReach(float reach) {
            // Not a small-distance hit
            if(reach > 1) {
                playerReach += reach; // Add reach
                playerReach /= 2; // Average between this reach and the reach recorded before
                if(!isReachNormal()) {
                    offenses++; // Add offense if reach is too much
                    allOffenses++;
                    return;
                }
            }
            offenses -= 0.5;
            // If reach is normal and achievable,
            // remove one fifth of a offense, so after 5 normal hits,
            // a strange hit is normalized again.
            
            // No negative offenses
            if(offenses < 0)
                offenses = 0;
        }
    }
}
