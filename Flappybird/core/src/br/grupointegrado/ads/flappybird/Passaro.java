package br.grupointegrado.ads.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Willys on 05/10/2015.
 */
public class Passaro {

    private final World mundo;
    private final OrthographicCamera camera;
    private final Texture[] texturas;
    private Body corpo;

    public Passaro (World mundo, OrthographicCamera camera, Texture[] texturas){

        this.mundo = mundo;
        this.camera = camera;
        this.texturas = texturas;

        initCorpo();
    }

    private void initCorpo() {

        float x = (camera.viewportWidth / 2) / Util.PIXEL_METRO;
        float y = (camera.viewportHeight / 2) / Util.PIXEL_METRO;

        corpo = Util.criarcorpo(mundo, BodyDef.BodyType.DynamicBody, x, y);

        FixtureDef definicao = new FixtureDef();
        definicao.density = 1;
        definicao.friction = 0.4f;
        definicao.restitution = 0.3f;

        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("physics/bird.json"));
        loader.attachFixture(corpo, "bird", definicao, 1, "PASSARO");

    }

    /**
     * Atualiza o comportamento do passaro
     * @param delta
     * @param movimentar
     */
    public void atualizar(float delta, boolean movimentar){
        if(movimentar){
            atualizarVelocidade();
            atualizarRotacao();
        }

    }

    private void atualizarRotacao() {
        float velocidadey = corpo.getLinearVelocity().y;
        float rotacao = 0;

        if(velocidadey < 0){
            rotacao = -15;
        }else if (velocidadey > 0){
            rotacao = +10;
        }else rotacao = 0;

        rotacao = (float) Math.toRadians(rotacao);
        corpo.setTransform(corpo.getPosition(), rotacao);
    }

    private void atualizarVelocidade() {
        corpo.setLinearVelocity(2f,corpo.getLinearVelocity().y);
    }

    /**
     * Aplica uma forÃ§a positiva no Y para simular o pulo
     */
    public void pular(){
        corpo.setLinearVelocity(corpo.getLinearVelocity().x,0);
        corpo.applyForceToCenter(0,115,false);
    }

    public Body getCorpo(){
        return corpo;
    }
}
