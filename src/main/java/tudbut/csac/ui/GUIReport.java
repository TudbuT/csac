package tudbut.csac.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.lwjgl.input.Keyboard;
import tudbut.csac.ChatUtils;

import java.security.Key;
import java.util.ArrayList;

public class GUIReport {
    static Minecraft mc = Minecraft.getMinecraft();
    
    public static int selected = 0;
    static boolean downDown = false;
    static boolean upDown = false;
    static boolean enterDown = false;
    static NetworkPlayerInfo[] playersLastTick = mc.player.connection.getPlayerInfoMap().toArray(new NetworkPlayerInfo[0]);
    
    
    public static void render() {
        ScaledResolution resolution = new ScaledResolution(mc);
    
        NetworkPlayerInfo[] players = mc.player.connection.getPlayerInfoMap().toArray(new NetworkPlayerInfo[0]);
        
        int x = resolution.getScaledWidth() / 6;
        int y = (int) (resolution.getScaledHeight() / 2.5 - Math.min(players.length * 10, 10) / 2);
        
        if(players.length != playersLastTick.length) {
            for (int i = 0; i < players.length; i++) {
                NetworkPlayerInfo player = players[i];
                boolean b = false;
                for (int j = 0; j < playersLastTick.length; j++) {
                    if (playersLastTick[j].getGameProfile().getId().equals(player.getGameProfile().getId())) {
                        b = true;
                        break;
                    }
                }
                if(!b) {
                    if(selected > i) {
                        selected++;
                    }
                }
            }
            for (int i = 0; i < playersLastTick.length; i++) {
                NetworkPlayerInfo player = playersLastTick[i];
                boolean b = false;
                for (int j = 0; j < players.length; j++) {
                    if (players[j].getGameProfile().getId().equals(player.getGameProfile().getId())) {
                        b = true;
                        break;
                    }
                }
                if(!b) {
                    if(selected > i) {
                        selected--;
                    }
                }
            }
        }
        
        if(selected >= players.length)
            selected = players.length - 1;
        if(selected < 0)
            selected = 0;
        
        for (int i = Math.max(0, selected - 5); i < Math.min(selected + 5, players.length); i++) {
            mc.fontRenderer.drawString(
                    (selected == i ? "|>" : "| ") + (
                            players[i].getDisplayName() != null ?
                            players[i].getDisplayName().getUnformattedText() :
                            ScorePlayerTeam.formatPlayerName(
                                    players[i].getPlayerTeam(),
                                    players[i].getGameProfile().getName()
                            )
                    ),
                    x, y, 0xffffff
            );
            y+=10;
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            if(!upDown)
                selected--;
            upDown = true;
        }
        else
            upDown = false;
    
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            if(!downDown)
                selected++;
            downDown = true;
        }
        else
            downDown = false;
        
        if(selected >= players.length)
            selected = players.length - 1;
        if(selected < 0)
            selected = 0;
        
        if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
            if(!enterDown)
                ChatUtils.simulateSend("/report " + players[selected].getGameProfile().getName() + " cheating", false);
            enterDown = true;
        }
        else
            enterDown = false;
        
        playersLastTick = players;
    }
}
