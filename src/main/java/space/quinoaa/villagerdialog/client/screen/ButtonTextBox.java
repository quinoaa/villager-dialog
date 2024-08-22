package space.quinoaa.villagerdialog.client.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class ButtonTextBox extends TextBox {
    private final Runnable onClick;

    public ButtonTextBox(TextBoxTextureSpec spec, Component title, Component content, Font font, Runnable onClick) {
        super(spec, title, content, font);
        this.onClick = onClick;
    }

    public void setHovered(int mouseX, int mouseY){
        vOffset = isMouseIn(mouseX, mouseY) ? 1 : 0;
    }

    public void onClick(){
        onClick.run();
    }
}
