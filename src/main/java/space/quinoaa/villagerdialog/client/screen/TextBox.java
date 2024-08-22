package space.quinoaa.villagerdialog.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class TextBox {
    private static final int PADDING = 4;
    private static final int TITLE_SPACING = 2;

    private final TextBoxTextureSpec spec;
    @Nullable
    private final Component title;
    private final List<FormattedCharSequence> lines;

    private final Font font;

    public final int width, height;
    public int x, y;

    public int vOffset = 0;

    public TextBox(TextBoxTextureSpec spec, @Nullable Component title, Component content, Font font) {
        this.spec = spec;
        this.title = title;
        this.font = font;

        int textMax = spec.maxWidth - spec.insets * 2;
        lines = font.split(content, textMax);
        height = lines.size() * font.lineHeight + spec.insets * 2 + (title != null ? font.lineHeight + TITLE_SPACING : 0) + PADDING * 2;

        width = Math.min(Math.max(title != null ? font.width(title) : 0, font.width(content)) + spec.insets * 2, spec.maxWidth) + PADDING * 2;
    }

    public void render(GuiGraphics g){
        int vOff = this.vOffset * spec.vSize;

        RenderSystem.disableBlend();
        g.blitNineSliced(spec.location, x, y, width, height, spec.insets, spec.insets, spec.uSize, spec.vSize, spec.uOff, spec.vOff + vOff);
        //g.blit(spec.location, 0, 0, 0, 0, 32, 32);

        int offset = spec.insets + PADDING;
        if(title != null){
            g.drawCenteredString(font, title, width / 2 + x, y + offset, 0xFFFFFFFF);
            offset += font.lineHeight + TITLE_SPACING;
        }
        for (FormattedCharSequence line : lines) {
            g.drawString(font, line, x + spec.insets + PADDING, y + offset, 0xffffffff, false);
            offset += font.lineHeight;
        }
    }

    public boolean isMouseIn(int mouseX, int mouseY){
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }


}
