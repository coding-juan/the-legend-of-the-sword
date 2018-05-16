package com.codingjuan.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class Dialogue {
    Array<String> paragraphs = new Array<String>();

    public Dialogue(String dialogueName) {
        FileHandle dialog = Gdx.files.internal("dialogues/" + dialogueName + ".txt");
        for (String paragraph : dialog.readString().split("-n" + System.getProperty("line.separator")))
            paragraphs.add(paragraph);
    }

    public int getParagraphs() {
        return paragraphs.size;
    }

    public String getParagraph(int i) {
        return paragraphs.get(i);
    }
}
