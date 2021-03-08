package tudbut.csac.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.lwjgl.input.Keyboard;
import tudbut.csac.ChatUtils;

public class GUIReport {
    static Minecraft mc = Minecraft.getMinecraft();
    
    static int selected = 0;
    static boolean downDown = false;
    static boolean upDown = false;
    static boolean rightDown = false;
    static boolean leftDown = false;
    static boolean enterDown = false;
    static int selectedType = -1;
    static NetworkPlayerInfo[] playersLastTick = mc.player.connection.getPlayerInfoMap().toArray(new NetworkPlayerInfo[0]);
    
    private enum ReportType {
        Cheating("CTG"),
        CrossTeaming("CTT"),
        BadChatPublic("CHR"),
        BadChatParty("PC_C"),
        BadChatGuild("GU_C"),
        BadChatDM("DM_C"),
        BadName("BDN"),
        
        ;
        
        public final String msg;
        
        ReportType(String msg) {
            this.msg = msg;
        }
    }
    
    public static void render() {
        ScaledResolution resolution = new ScaledResolution(mc);
    
        NetworkPlayerInfo[] players = mc.player.connection.getPlayerInfoMap().toArray(new NetworkPlayerInfo[0]);
    
        int x = resolution.getScaledWidth() / 6;
        int y = (int) (resolution.getScaledHeight() / 2.5 - Math.min(players.length * 10, 10) / 2);
    
        if (players.length != playersLastTick.length) {
            for (int i = 0; i < players.length; i++) {
                NetworkPlayerInfo player = players[i];
                boolean b = false;
                for (int j = 0; j < playersLastTick.length; j++) {
                    if (playersLastTick[j].getGameProfile().getId().equals(player.getGameProfile().getId())) {
                        b = true;
                        break;
                    }
                }
                if (!b) {
                    if (selected >= i) {
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
                if (!b) {
                    if (selected == i)
                        selectedType = -1;
                    if (selected > i) {
                        selected--;
                    }
                }
            }
        }
    
        if (selected >= players.length)
            selected = players.length - 1;
        if (selected < 0)
            selected = 0;
        if (selectedType >= ReportType.values().length)
            selectedType = ReportType.values().length - 1;
    
        if (selectedType == -1) {
            for (int i = Math.max(0, selected - 5); i < Math.min(selected + 5, players.length); i++) {
                mc.fontRenderer.drawString(
                        (selected == i ? "§m| §f" : "| §f") +
                        (
                                players[i].getDisplayName() != null ?
                                players[i].getDisplayName().getUnformattedText() :
                                ScorePlayerTeam.formatPlayerName(
                                        players[i].getPlayerTeam(),
                                        players[i].getGameProfile().getName()
                                )
                        ) +
                        (selected == i ? "§r§c > " : ""),
                        x, y, selected == i ? 0xff0000 : 0x00ff00
                );
                y += 10;
            }
        }
        else {
            for (int i = 0; i < ReportType.values().length; i++) {
                mc.fontRenderer.drawString((selectedType == i ? "< §m| §f" : "< | §f") + ReportType.values()[i].name(), x, y, selectedType == i ? 0xff0000 : 0x00ff00);
                y += 10;
            }
        
            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) && mc.currentScreen == null) {
                if (!enterDown) {
                    ChatUtils.simulateSend("/report " + players[selected].getGameProfile().getName() + " -b " + ReportType.values()[selectedType].msg + " -C", false);
                    selectedType = -1;
                }
                enterDown = true;
            }
            else {
                enterDown = false;
            }
        }
    
        if (Keyboard.isKeyDown(Keyboard.KEY_UP) && mc.currentScreen == null) {
            if (!upDown)
                if (selectedType == -1)
                    selected--;
                else if (selectedType != 0)
                    selectedType--;
            upDown = true;
        }
        else
            upDown = false;
    
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && mc.currentScreen == null) {
            if (!downDown)
                if (selectedType == -1)
                    selected++;
                else
                    selectedType++;
            downDown = true;
        }
        else
            downDown = false;
    
    
        if (selected >= players.length)
            selected = players.length - 1;
        if (selected < 0)
            selected = 0;
        if (selectedType >= ReportType.values().length)
            selectedType = ReportType.values().length - 1;
    
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && mc.currentScreen == null) {
            if (!rightDown)
                selectedType = 0;
            rightDown = true;
        }
        else {
            rightDown = false;
        }
    
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && mc.currentScreen == null) {
            if (!leftDown)
                selectedType = -1;
            leftDown = true;
        }
        else {
            leftDown = false;
        }
    
        playersLastTick = players;
    }
}
