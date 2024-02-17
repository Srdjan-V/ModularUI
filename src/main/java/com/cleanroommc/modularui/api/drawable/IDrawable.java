package com.cleanroommc.modularui.api.drawable;

import com.cleanroommc.modularui.drawable.Icon;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widget.sizer.Area;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.gson.JsonObject;

/**
 * An object which can be drawn. This is mainly used for backgrounds and overlays in
 * {@link com.cleanroommc.modularui.api.widget.IWidget}.
 */
public interface IDrawable {

    /**
     * Draws this drawable at the given position with the given size.
     *
     * @param context current context to draw with
     * @param x       x position
     * @param y       y position
     * @param width   draw width
     * @param height  draw height
     */
    @SideOnly(Side.CLIENT)
    void draw(GuiContext context, int x, int y, int width, int height, WidgetTheme widgetTheme);

    /**
     * @deprecated use {@link #draw(GuiContext, int, int, int, int, WidgetTheme)}
     */
    @SideOnly(Side.CLIENT)
    @Deprecated
    default void draw(GuiContext context, int x, int y, int width, int height) {
        draw(context, x, y, width, height, WidgetTheme.getDefault());
    }

    /**
     * @deprecated use {@link #drawAtZero(GuiContext, int, int, WidgetTheme)}
     */
    @SideOnly(Side.CLIENT)
    @Deprecated
    default void drawAtZero(GuiContext context, int width, int height) {
        drawAtZero(context, width, height, WidgetTheme.getDefault());
    }

    /**
     * Draws this drawable at the current (0|0) with the given size.
     *
     * @param context     gui context
     * @param width       draw width
     * @param height      draw height
     * @param widgetTheme
     */
    @SideOnly(Side.CLIENT)
    default void drawAtZero(GuiContext context, int width, int height, WidgetTheme widgetTheme) {
        draw(context, 0, 0, width, height, widgetTheme);
    }

    /**
     * @deprecated use {@link #draw(GuiContext, Area, WidgetTheme)}
     */
    @SideOnly(Side.CLIENT)
    @Deprecated
    default void draw(GuiContext context, Area area) {
        draw(context, area.x, area.y, area.width, area.height, WidgetTheme.getDefault());
    }

    /**
     * Draws this drawable in a given area.
     *
     * @param context current context to draw with
     * @param area    draw area
     */
    @SideOnly(Side.CLIENT)
    default void draw(GuiContext context, Area area, WidgetTheme widgetTheme) {
        draw(context, area.x, area.y, area.width, area.height, widgetTheme);
    }

    /**
     * @deprecated use {@link #drawAtZero(GuiContext, Area, WidgetTheme)}
     */
    @Deprecated
    @SideOnly(Side.CLIENT)
    default void drawAtZero(GuiContext context, Area area) {
        draw(context, 0, 0, area.width, area.height, WidgetTheme.getDefault());
    }

    /**
     * Draws this drawable at the current (0|0) with the given area's size.
     *
     * @param context gui context
     * @param area    draw area
     */
    @SideOnly(Side.CLIENT)
    default void drawAtZero(GuiContext context, Area area, WidgetTheme widgetTheme) {
        draw(context, 0, 0, area.width, area.height, widgetTheme);
    }

    /**
     * @return if theme color can be applied on this drawable
     */
    default boolean canApplyTheme() {
        return false;
    }

    /**
     * @return a widget with this drawable as a background
     */
    default Widget<?> asWidget() {
        return new DrawableWidget(this);
    }

    /**
     * @return this drawable as an icon
     */
    default Icon asIcon() {
        return new Icon(this);
    }

    /**
     * Reads extra json data after this drawable is created.
     *
     * @param json json to read from
     */
    default void loadFromJson(JsonObject json) {}

    /**
     * An empty drawable. Does nothing.
     */
    IDrawable EMPTY = (context, x, y, width, height, widgetTheme) -> {};

    /**
     * An empty drawable used to mark hover textures as "should not be used"!
     */
    IDrawable NONE = (context, x, y, width, height, widgetTheme) -> {};

    /**
     * A widget wrapping a drawable. The drawable is drawn between the background and the overlay.
     */
    class DrawableWidget extends Widget<DrawableWidget> {

        private final IDrawable drawable;

        public DrawableWidget(IDrawable drawable) {
            this.drawable = drawable;
        }

        @SideOnly(Side.CLIENT)
        @Override
        public void draw(GuiContext context, WidgetTheme widgetTheme) {
            this.drawable.drawAtZero(context, getArea(), widgetTheme);
        }
    }
}
