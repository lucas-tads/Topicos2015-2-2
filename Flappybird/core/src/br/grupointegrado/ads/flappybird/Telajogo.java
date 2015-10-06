package br.grupointegrado.ads.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by lucas on 28/09/2015.
 */
public class Telajogo extends TelaBase {

    private OrthographicCamera camera; // representa a camera
    private World mundo; // representa a camera
    private Body chao; //corpo do chao
    private Passaro passaro;

    private Box2DDebugRenderer debug;  //desenha o mundo na tela para ajudar o desenvolvimento

    public Telajogo(Maingame game) {
        super(game);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth() / Util.ESCALA, Gdx.graphics.getHeight() / Util.ESCALA);
        debug = new Box2DDebugRenderer();
        mundo = new World(new Vector2(0, -9.8f), false);

        initchao();
        initpassaro();
    }

    private void initchao() {
        chao = Util.criarcorpo(mundo, BodyDef.BodyType.StaticBody, 0 , 0);



    }

    private void initpassaro() {
        passaro = new Passaro(mundo, camera, null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1); //limpa a tela e pinta a cor de fundo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //mantem o buffer de cores

        atualizar(delta);
        renderizar(delta);

        debug.render(mundo, camera.combined.cpy().scl(Util.PIXEL_METRO));
    }

    /**
     * atulizacao e calculos dos corpos
     * @param delta
     */
    private void atualizar(float delta) {
        mundo.step(1f / 60f, 6, 2);
        atualizarchao();
    }

    /**
     * atualiza a posicao do chao para acompanhar a posicao do passaro
     */
    private void atualizarchao() {
        float largura = camera.viewportWidth / Util.PIXEL_METRO;
        Vector2 posicao = new Vector2();
        posicao.x = largura /2;
        chao.setTransform(posicao, 0);
    }

    /**
     * desenhar as imagens
     * @param delta
     */
    private void renderizar(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / Util.ESCALA, height / Util.ESCALA);
        camera.update();

        redimensionachao();
    }

    /**
     * configura o tamanho do chao de acordo com o tamanho da tela.
     */
    private void redimensionachao() {
        chao.getFixtureList().clear();
        float largura = camera.viewportWidth / Util.PIXEL_METRO;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura/ 2, Util.ALTURA_CHAO / 2);
        Fixture forma = Util.criarforma(chao, shape, "CHAO");
        shape.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        mundo.dispose();
        debug.dispose();
    }
}
