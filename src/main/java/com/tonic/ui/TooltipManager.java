package com.tonic.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class TooltipManager {
    private static Label tooltipLabel;

    public static void showTooltip(String text, float x, float y, Stage stage, Skin skin) {
        if (tooltipLabel != null) {
            tooltipLabel.remove();
        }
        tooltipLabel = new Label(text, skin);
        tooltipLabel.setPosition(x, y);
        stage.addActor(tooltipLabel);
    }

    public static void hideTooltip() {
        if (tooltipLabel != null) {
            tooltipLabel.remove();
            tooltipLabel = null;
        }
    }
}
