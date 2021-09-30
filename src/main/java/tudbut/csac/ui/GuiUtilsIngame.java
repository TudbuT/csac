package tudbut.csac.ui;

import de.tudbut.type.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import tudbut.csac.CSAC;
import tudbut.csac.Utils;
import tudbut.csac.events.EventHandler;
import tudbut.obj.Vector2i;

public class GuiUtilsIngame extends Gui {
    static Minecraft mc = Minecraft.getMinecraft();
    
    public static void draw() {
        new GuiUtilsIngame().drawImpl();
    }
    
    public void drawImpl() {
        ScaledResolution sr = new ScaledResolution(mc);
        Vector2i screenSize = new Vector2i(sr.getScaledWidth(), sr.getScaledHeight());
    
        int y = sr.getScaledHeight() - (5 + mc.fontRenderer.FONT_HEIGHT);
        int x = screenSize.getX() - 5;
    
        if(!CSAC.ingameCheck())
            return;
        
        
        y = drawPos(mc.player, "Player", x, y);
        drawString("Ping: " + CSAC.ping[0] + " | TPS: " + (Utils.roundTo(EventHandler.tps, 2)) + " | Players: " + CSAC.ping[1] + "/" + CSAC.ping[2], x, y, 0xff00ff00);
        
    }
    
    private void drawString(String s, int x, int y, int color) {
        drawString(
                mc.fontRenderer,
                s,
                x - mc.fontRenderer.getStringWidth(s),
                y,
                color
        );
    }
    
    private void drawStringL(String s, int x, int y, int color) {
        drawString(
                mc.fontRenderer,
                s,
                x,
                y,
                color
        );
    }
    
    private int drawPos(Entity e, String s, int x, int y) {
        Vector3d p = new Vector3d(e.posX, e.posY, e.posZ);
        
        p.setX(Math.round(p.getX() * 10d) / 10d);
        p.setY(Math.round(p.getY() * 10d) / 10d);
        p.setZ(Math.round(p.getZ() * 10d) / 10d);
    
        if(mc.world.provider.getDimension() == -1)
            drawString(
                    s + " Overworld " + Math.round(p.getX() * 8 * 10d) / 10d + " " + Math.round(p.getY() * 8 * 10d) / 10d + " " + Math.round(p.getZ() * 8 * 10d) / 10d,
                    x, y, 0xff00ff00
            );
        if(mc.world.provider.getDimension() == 0)
            drawString(
                    s + " Nether " + Math.round(p.getX() / 8 * 10d) / 10d + " " + Math.round(p.getY() / 8 * 10d) / 10d + " " + Math.round(p.getZ() / 8 * 10d) / 10d,
                    x, y, 0xff00ff00
            );
        y -= 10;
        drawString(s + " " + p.getX() + " " + p.getY() + " " + p.getZ(), x, y, 0xff00ff00);
        return y - 10;
    }
}
