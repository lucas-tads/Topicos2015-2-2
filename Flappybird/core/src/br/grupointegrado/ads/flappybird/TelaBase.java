package br.grupointegrado.ads.flappybird;

import com.badlogic.gdx.Screen;

/**
 * Created by lucas on 28/09/2015.
 */
public abstract class TelaBase implements Screen {

    protected Maingame game;

    public TelaBase (Maingame game){
        this.game = game;
    }

    @Override
    public void hide() {
        dispose();
    }
}
