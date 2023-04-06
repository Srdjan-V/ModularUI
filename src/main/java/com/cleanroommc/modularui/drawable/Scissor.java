package com.cleanroommc.modularui.drawable;

import com.cleanroommc.modularui.ModularUI;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.utils.MathUtils;
import com.cleanroommc.modularui.widget.sizer.Area;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.Stack;

public class Scissor {

    private final static Stack<Area> scissors = new Stack<Area>();

    public static void scissor(Area area, GuiContext context) {
        scissor(area.x, area.y, area.width, area.height, context);
    }

    public static void scissor(int x, int y, int w, int h, GuiContext context) {
        scissor(context.globalX(x), context.globalY(y), w, h, context.screen.getViewport().width, context.screen.getViewport().height);
    }

    public static void scissorTransformed(Area area, GuiContext context) {
        scissorTransformed(area.x, area.y, area.width, area.height, context);
    }

    public static void scissorTransformed(int x, int y, int w, int h, GuiContext context) {
        scissor(x, y, w, h, context.screen.getViewport().width, context.screen.getViewport().height);
    }

    /**
     * Scissor (clip) the screen
     */
    public static void scissor(int x, int y, int w, int h, int sw, int sh) {
        Area scissor = scissors.isEmpty() ? null : scissors.peek();

        /* If it was scissored before, then clamp to the bounds of the last one */
        if (scissor != null) {
            w += Math.min(x - scissor.x, 0);
            h += Math.min(y - scissor.y, 0);
            x = MathUtils.clamp(x, scissor.x, scissor.ex());
            y = MathUtils.clamp(y, scissor.y, scissor.ey());
            w = MathUtils.clamp(w, 0, scissor.ex() - x);
            h = MathUtils.clamp(h, 0, scissor.ey() - y);
        }

        scissor = new Area(x, y, w, h);
        scissorArea(x, y, w, h, sw, sh);
        scissors.add(scissor);
    }

    private static void scissorArea(int x, int y, int w, int h, int sw, int sh) {
        /* Clipping area around scroll area */
        Minecraft mc = Minecraft.getMinecraft();

        float rx = (float) Math.ceil(mc.displayWidth / (double) sw);
        float ry = (float) Math.ceil(mc.displayHeight / (double) sh);

        int xx = (int) (x * rx);
        int yy = (int) (mc.displayHeight - (y + h) * ry);
        int ww = (int) (w * rx);
        int hh = (int) (h * ry);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        if (ww == 0 || hh == 0) {
            GL11.glScissor(0, 0, 1, 1);
        } else {
            GL11.glScissor(xx, yy, ww, hh);
        }
    }

    public static void unscissor(GuiContext context) {
        unscissor(context.screen.getViewport().width, context.screen.getViewport().height);
    }

    public static void unscissor(int sw, int sh) {
        scissors.pop();

        if (scissors.isEmpty()) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        } else {
            Area area = scissors.peek();

            scissorArea(area.x, area.y, area.width, area.height, sw, sh);
        }
    }

    public static boolean isInsideScissorArea(Area area, GuiContext context) {
        Area.SHARED.setGlobal(area, context);
        return scissors.isEmpty() || scissors.peek().intersects(Area.SHARED);
    }
}