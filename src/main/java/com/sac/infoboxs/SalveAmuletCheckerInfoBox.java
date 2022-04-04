package com.sac.infoboxs;

import lombok.Setter;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SalveAmuletCheckerInfoBox extends InfoBox {

    @Setter
    private String infoBoxText;

    @Setter
    private Color infoBoxTextColor;

    public SalveAmuletCheckerInfoBox(BufferedImage image, Plugin plugin){
        super(image, plugin);
    }



    @Override
    public String getText() {
        return infoBoxText;
    }

    @Override
    public Color getTextColor() {
        return infoBoxTextColor;
    }
}
