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
 * Created by Lucas on 28/09/2015.
 */
public class Telajogo extends TelaBase {

    private OrthographicCamera camera; //camera do jogo
    private World mundo; // representa o mundo do Box2D
    private Body chao; // corpo do chÃ£o
    Passaro passaro;

    private Box2DDebugRenderer debug; //desenha o mundo na tela para ajudar no desenvolvimento.

    public Telajogo(Maingame game) {
        super(game);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth()/Util.ESCALA, Gdx.graphics.getHeight()/Util.ESCALA);
        debug = new Box2DDebugRenderer();
        mundo = new World(new Vector2(0,-9.8f), false);
        initChao();
        initPassaro();

        new Obstaculo(mundo, camera, null);
    }

    private void initChao() {
        chao = Util.criarcorpo(mundo, BodyDef.BodyType.StaticBody,0,0);
    }

    private void initPassaro() {

        passaro = new Passaro(mundo,camera,null);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1); //Limpa tela e pinta cor de fundo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // mantem o buffer de cores

        capturaTeclas();

        atualizar(delta);
        renderizar(delta);

        debug.render(mundo, camera.combined.cpy().scl(Util.PIXEL_METRO));
    }

    private boolean pulando = false;
    private void capturaTeclas() {
        pulando = false;
        if(Gdx.input.justTouched()){
            pulando = true;
        }
    }

    /**
     * AtualizaÃ§Ã£o e cÃ¡lculo dos corpos
     * @param delta
     */
    private void atualizar(float delta) {
        passaro.atualizar(delta);
        mundo.step(1f / 60f, 6, 2);
        atualizarCamera();
        atualizarChao();
        if(pulando){
            passaro.pular();
        }
    }

    private void atualizarCamera() {
        camera.position.x = (passaro.getCorpo().getPosition().x + 34 / Util.PIXEL_METRO) * Util.PIXEL_METRO;
        camera.update();
    }

    /**
     * Atualiza a posiÃ§Ã£o do chÃ£o para acompanhar o pÃ¡ssaro
     */
    private void atualizarChao() {
        Vector2 posicao = passaro.getCorpo().getPosition();
        chao.setTransform(posicao.x, 0, 0);
    }

    /**
     * Renderizar/desenhar as imagens
     * @param delta
     */
    private void renderizar(float delta) {
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / Util.ESCALA, height / Util.ESCALA);
        camera.update();
        redimensionaChao();
    }

    /**
     * Configura o tamanho do chÃ£o de acordo com o tamanho da tela
     */
    private void redimensionaChao() {
        chao.getFixtureList().clear();
        float largura = camera.viewportWidth / Util.PIXEL_METRO;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, Util.ALTURA_CHAO / 2);
        Fixture forma = Util.criarforma(chao,shape,"CHAO");
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
        debug.dispose();
        mundo.dispose();
    }
}