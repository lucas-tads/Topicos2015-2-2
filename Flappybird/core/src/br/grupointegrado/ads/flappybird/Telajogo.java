package br.grupointegrado.ads.flappybird;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.graphics.Color;
        import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.BitmapFont;
        import com.badlogic.gdx.graphics.g2d.Sprite;
        import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
        import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
        import com.badlogic.gdx.physics.box2d.Contact;
        import com.badlogic.gdx.physics.box2d.ContactImpulse;
        import com.badlogic.gdx.physics.box2d.ContactListener;
        import com.badlogic.gdx.physics.box2d.Fixture;
        import com.badlogic.gdx.physics.box2d.Manifold;
        import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
        import com.badlogic.gdx.scenes.scene2d.Stage;
        import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
        import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
        import com.badlogic.gdx.scenes.scene2d.ui.Label;
        import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
        import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
        import com.badlogic.gdx.utils.Array;
        import com.badlogic.gdx.utils.viewport.FillViewport;

        import java.awt.event.InputEvent;

/**
 * Created by Lucas on 28/09/2015.
 */
public class Telajogo extends TelaBase {

    private OrthographicCamera camera; //camera do jogo
    private World mundo; // representa o mundo do Box2D
    private Body chao; // corpo do chÃ£o
    private Passaro passaro;
    private Array<Obstaculo> obstaculos = new Array<Obstaculo>();

    private int pontuacao = 0;
    private BitmapFont fontePontuacao;
    private Stage palcoInformacoes;
    private Label lbPontuacao;
    private ImageButton btnPlay;
    private ImageButton btnGameOver;
    private OrthographicCamera cameraInfo;

    private Texture[] texturaspasaros;
    private Texture texturaObstaculoCima;
    private Texture texturaObstaculoBaixo;
    private Texture texturaChao;
    private Texture texturaFundo;
    private Texture texturaPlay;
    private Texture texturaGameOver;

    private  boolean jogoiniciado = false;

    private Box2DDebugRenderer debug; //desenha o mundo na tela para ajudar no desenvolvimento.

    public Telajogo(Maingame game) {
        super(game);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth()/Util.ESCALA, Gdx.graphics.getHeight()/Util.ESCALA);
        cameraInfo = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        debug = new Box2DDebugRenderer();
        mundo = new World(new Vector2(0,-9.8f), false);

        mundo.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                detectarcolisao(contact.getFixtureA(), contact.getFixtureB());
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        initTexturas();
        initChao();
        initPassaro();
        initFontes();
        initinformacoes();
    }

    private void initTexturas() {
        texturaspasaros = new  Texture[3];
        texturaspasaros[0] = new Texture("sprites/bird-1.png");
        texturaspasaros[1] = new Texture("sprites/bird-2.png");
        texturaspasaros[2] = new Texture("sprites/bird-3.png");

        texturaObstaculoCima = new Texture("sprites/toptube.png");
        texturaObstaculoBaixo= new Texture("sprites/bottomtube.png");

        texturaFundo = new Texture("sprites/bg.png");
        texturaChao = new Texture("sprites/ground.png");
        texturaPlay = new Texture("sprites/playbtn.png");
        texturaGameOver = new Texture("sprites/gameover.png");
    }

    private boolean gameOver = false;
    /**
     * verifca se o passaro esta envolvido na colisao
     * @param fixtureA //corpo  a
     * @param fixtureB//corpo b
     */
    private void detectarcolisao(Fixture fixtureA, Fixture fixtureB) {
        if ("PASSARO".equals(fixtureA.getUserData()) || "PASSARO".equals(fixtureB.getUserData())){
            gameOver = true;
        }
    }

    private void initFontes() {
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 56;
        fontParameter.color = Color.WHITE;
        fontParameter.shadowColor = Color.BLACK;
        fontParameter.shadowOffsetX = 4;
        fontParameter.shadowOffsetY = 4;

        FreeTypeFontGenerator gerador = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));

        fontePontuacao = gerador.generateFont(fontParameter);
        gerador.dispose();
    }

    private void initinformacoes() {
        palcoInformacoes = new Stage(new FillViewport(cameraInfo.viewportWidth, cameraInfo.viewportHeight, cameraInfo));
        Gdx.input.setInputProcessor(palcoInformacoes);

        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = fontePontuacao;

        lbPontuacao = new Label("0", estilo);
        palcoInformacoes.addActor(lbPontuacao);

        ImageButtonStyle estilobotao = new ImageButtonStyle();
        estilobotao.up = new SpriteDrawable(new Sprite(texturaPlay));
        btnPlay = new ImageButton(estilobotao);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                jogoiniciado = true;
            }
        });

        palcoInformacoes.addActor(btnPlay);

        estilobotao = new ImageButtonStyle();
        estilobotao.up = new SpriteDrawable(new Sprite(texturaGameOver));

        btnGameOver = new ImageButton(estilobotao);
        btnGameOver.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                reiniciarJogo();
            }
        });

        palcoInformacoes.addActor(btnGameOver);
    }

    private void reiniciarJogo() {
        game.setScreen(new Telajogo(game));// aqui vai o codigo de reiniciar  o jogo
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
        palcoInformacoes.act(delta);

        passaro.getCorpo().setFixedRotation(!gameOver);
        passaro.atualizar(delta, !gameOver);

        if(jogoiniciado) {
            mundo.step(1f / 60f, 6, 2);
            atualizarObstaculos();
        }

        atualizarInformacoes();

        if(!gameOver) {
            atualizarCamera();
            atualizarChao();
        }
        if(pulando && !gameOver && jogoiniciado){
            passaro.pular();
        }
    }

    private void atualizarInformacoes() {
        lbPontuacao.setText(pontuacao + "");
        lbPontuacao.setPosition(cameraInfo.viewportWidth / 2 - lbPontuacao.getPrefWidth() / 2,
                cameraInfo.viewportHeight - lbPontuacao.getPrefHeight());

        btnPlay.setPosition(cameraInfo.viewportWidth / 2 - btnPlay.getPrefWidth() / 2,
                cameraInfo.viewportHeight / 2 - btnPlay.getPrefHeight() * 2);
        btnPlay.setVisible(!jogoiniciado);

        btnGameOver.setPosition(cameraInfo.viewportWidth / 2 - btnGameOver.getPrefWidth() / 2,
                cameraInfo.viewportHeight / 2 - btnGameOver.getPrefHeight() /2);
        btnGameOver.setVisible(gameOver);
    }

    private void atualizarObstaculos() {
        //enquanto a lista tiver menos que 4, crie obstaculos
        while (obstaculos.size < 4){
            Obstaculo ultimo = null;

            if (obstaculos.size > 0)
                ultimo = obstaculos.peek();//recupera o ultimo obstaculo da lista

            Obstaculo o = new Obstaculo(mundo, camera, ultimo);
            obstaculos.add(o);
        }

        //verifica se os obstaculos sairam da tela para removelos
        for(Obstaculo o : obstaculos) {
            float iniciocamera = passaro.getCorpo().getPosition().x -
                    (camera.viewportHeight / 2 / Util.PIXEL_METRO) - o.getLargura();

            //verifica se o obstculos saiu da tela
            if (iniciocamera > o.getPosx()) {
                o.remover();
                obstaculos.removeValue(o, true);
            } else if (!o.isPassou() && o.getPosx() < passaro.getCorpo().getPosition().x) {
                o.setPassou(true);
                pontuacao ++;
            }
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
        palcoInformacoes.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / Util.ESCALA, height / Util.ESCALA);
        camera.update();
        redimensionaChao();

        cameraInfo.setToOrtho(false, width, height);
        cameraInfo.update();
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
        palcoInformacoes.dispose();
        fontePontuacao.dispose();

        texturaspasaros[0].dispose();
        texturaspasaros[1].dispose();
        texturaspasaros[2].dispose();

        texturaObstaculoCima.dispose();
        texturaObstaculoBaixo.dispose();

        texturaFundo.dispose();
        texturaChao.dispose();
        texturaChao.dispose();
        texturaGameOver.dispose();
    }
}