package space.quinoaa.villagerdialog.client.screen;

import net.minecraft.resources.ResourceLocation;

public class TextBoxTextureSpec {
    public final ResourceLocation location;

    public final int uOff, vOff, uSize, vSize, insets, maxWidth;

    public TextBoxTextureSpec(ResourceLocation location, int uOff, int vOff, int uSize, int vSize, int insets, int maxWidth) {
        this.location = location;
        this.uOff = uOff;
        this.vOff = vOff;
        this.uSize = uSize;
        this.vSize = vSize;
        this.insets = insets;
        this.maxWidth = maxWidth;
    }


}
