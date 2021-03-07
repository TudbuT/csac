package tudbut.csac;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import de.tudbut.type.Vector2d;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Utils { // A bunch of utils that don't deserve their own class, self-explanatory
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    public static Object getPrivateField(Class<?> clazz, Object instance, String field) {
        try {
            Object t;
            Field f = clazz.getDeclaredField(field);
            boolean b = f.isAccessible();
            f.setAccessible(true);
            t = f.get(instance);
            f.setAccessible(b);
            return t;
        } catch (Exception e) {
            return null;
        }
    }
    public static boolean setPrivateField(Class<?> clazz, Object instance, String field, Object content) {
        try {
            Field f = clazz.getDeclaredField(field);
            boolean b = f.isAccessible();
            f.setAccessible(true);
            f.set(instance, content);
            f.setAccessible(b);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static String[] getFieldsForType(Class<?> clazz, Class<?> type) {
        try {
            Field[] all = clazz.getDeclaredFields();
            ArrayList<String> names = new ArrayList<>();
            for (int i = 0; i < all.length; i++) {
                if(all[i].getType() == type) {
                    names.add(all[i].getName());
                }
            }
            return names.toArray(new String[0]);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    
    public static String removeNewlines(String s) {
        if (s == null)
            return null;
        return s.replaceAll("\n", "").replaceAll("\r", "");
    }
    
    private static Vec3d eyesPos() {
        return new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
    }
    
    public static Vector2d getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = eyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        double yaw = Math.toDegrees(Math.atan2(diffZ, diffX)) - 90f;
        double pitch = (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new Vector2d (MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch));
    }
    
    public static BlockPos getRealPos(Vec3d vec3d) {
        double x;
        double y;
        double z;
        
        if(vec3d.x < 0)
            x = (Math.ceil(vec3d.x) - 0.5);
        else
            x = (Math.floor(vec3d.x) + 0.5);
        
        if(vec3d.y < 0)
            y = (Math.ceil(vec3d.y) - 0.5);
        else
            y = (Math.floor(vec3d.y) + 0.5);
        
        if(vec3d.z < 0)
            z = (Math.ceil(vec3d.z) - 0.5);
        else
            z = (Math.floor(vec3d.z) + 0.5);
        
        return new BlockPos(x,y,z);
    }
}
