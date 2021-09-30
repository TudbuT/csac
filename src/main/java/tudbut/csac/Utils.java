package tudbut.csac;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import de.tudbut.type.Vector2d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.Entity;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

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
    
    public static float Q_sqrt(float x){
        float xHalfed = 0.5f * x;
        int i = Float.floatToRawIntBits(x);
        i = 0x5f3759df - (i >> 1);
        x = Float.intBitsToFloat(i);
        x = x*(1.5f - xHalfed*x*x);
        x = x*(1.5f - xHalfed*x*x);
        return x;
    }
    
    public static long[] getPingToServer(ServerData server) {
        
        server = new ServerData(server.serverName, server.serverIP, server.isOnLAN());
        
        try {
            long sa = new Date().getTime();
            
            AtomicLong pingSentAt = new AtomicLong();
            AtomicBoolean done = new AtomicBoolean();
            
            ServerAddress serveraddress = ServerAddress.fromString(server.serverIP);
            final NetworkManager networkmanager;
            networkmanager = NetworkManager.createNetworkManagerAndConnect(InetAddress.getByName(serveraddress.getIP()), serveraddress.getPort(), false);
            
            server.pingToServer = -1L;
            final long[] players = { 1, 1 };
            ServerData finalServer = server;
            networkmanager.setNetHandler(new INetHandlerStatusClient() {
                @Override
                public void onDisconnect(@Nullable ITextComponent reason) {
                    done.set(true);
                }
        
                @Override
                public void handleServerInfo(@Nullable SPacketServerInfo packetIn) {
                    pingSentAt.set(new Date().getTime());
                    networkmanager.sendPacket(new CPacketPing(pingSentAt.get()));
                    try {
                        assert packetIn != null;
                        players[0] = packetIn.getResponse().getPlayers().getOnlinePlayerCount();
                        players[1] = packetIn.getResponse().getPlayers().getMaxPlayers();
                    } catch (Exception ignored) { }
                }
        
                public void handlePong(@Nullable SPacketPong packetIn) {
                    long j = Minecraft.getSystemTime();
                    finalServer.pingToServer = j - pingSentAt.get();
                    networkmanager.closeChannel(new TextComponentString("Finished"));
                    done.set(true);
                }
            });
    
            networkmanager.sendPacket(new C00Handshake(serveraddress.getIP(), serveraddress.getPort(), EnumConnectionState.STATUS, false));
            networkmanager.sendPacket(new CPacketServerQuery());
            
            while (!done.get() && (new Date().getTime() - sa) < 5000) ;
            
            return new long[] { server.pingToServer, players[0], players[1] };
        }
        catch (Throwable ignored) {
            return new long[] { -1, 1, 1 };
        }
    }
    
    public static float roundTo(float f, int p) {
        p = (int) Math.pow(10, p);
        return (float) Math.round(f * p) / p;
    }
}
