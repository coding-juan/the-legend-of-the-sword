package com.codingjuan.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.codingjuan.entities.Dialogue;

public class DialogueManager {
    Dialogue currentDialog = null;
    BitmapFont font;

    int currentParagraph, maxParagraph;

    public DialogueManager() {
        font = new BitmapFont(Gdx.files.internal("font/font.fnt"), Gdx.files.internal("font/font.png"), false);
        font.getData().setScale(0.25f);
    }

    public void loadDialogue(String name) {
        currentDialog = new Dialogue(name);
        currentParagraph = 0;
        maxParagraph = currentDialog.getParagraphs() - 1;
    }

    public void unloadDialogue() {
        currentDialog = null;
        currentParagraph = -1;
        maxParagraph = -1;
    }

    public void nextParagraph() {
        if (currentParagraph + 1<= maxParagraph)
            currentParagraph++;
        else
            unloadDialogue();
    }

    public void render(SpriteBatch spriteBatch, Camera camera) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, currentDialog.getParagraph(currentParagraph));
        font.draw(spriteBatch, layout,camera.position.x - (layout.width / 2), camera.position.y - (camera.viewportHeight / 4) + (layout.height));
    }

    public boolean isDialogReady() {
        return currentDialog != null;
    }
}
