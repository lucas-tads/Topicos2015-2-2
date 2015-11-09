package br.grupointegrado.ads.flappybird;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by lucas on 26/10/2015.
 */
public class Obstaculo {

    private World mundo;
    private OrthographicCamera camera;
    private Body corpoCima, corpoBaixo;
    private float posx;
    private float posyCima, posyBaixo;
    private float largura, altura;
    private boolean passou;
    private Obstaculo ultimoObstaculo; //ultimo obstaculo antes do atual
    private float x;

    public Obstaculo(World mundo, OrthographicCamera camera, Obstaculo ultimoObstaculo) {
        this.mundo = mundo;
        this.camera = camera;
        this.ultimoObstaculo = ultimoObstaculo;

        initposicao();
        initCorpocima();
        initCorpoBaixo();

    }

    private void initCorpoBaixo() {
        corpoBaixo = Util.criarcorpo(mundo, BodyDef.BodyType.StaticBody, posx, posyBaixo);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, altura / 2);

        Util.criarforma(corpoBaixo, shape, "OBSTACULO_BAIXO");
        shape.dispose();
    }

    private void initCorpocima() {
        corpoCima = Util.criarcorpo(mundo, BodyDef.BodyType.StaticBody, posx, posyCima);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura/ 2, altura/2);

        Util.criarforma(corpoCima, shape, "OBSTACULO_CIMA");
        shape.dispose();
    }

    private void initposicao() {
        largura = 40 /Util.PIXEL_METRO;
        altura = camera.viewportHeight / Util.PIXEL_METRO;

        float xinicial = largura + (camera.viewportWidth / 2/ Util.PIXEL_METRO);
        if (ultimoObstaculo != null)
            xinicial = ultimoObstaculo.getposx();

        posx= xinicial + 4;
        //parcela é o tamanho da  tela dividido por6
        // para encontrar a posicao de y do obstáculo
        float parcela = (altura - Util.ALTURA_CHAO) / 6;

        int multiplicador = MathUtils.random(1, 3);

        posyBaixo = Util.ALTURA_CHAO + (parcela * multiplicador) - (altura / 2);
        posyCima = posyBaixo + altura + 2f; // 2f altura entre os canos
    }

    public void remover(){
        mundo.destroyBody(corpoCima);
        mundo.destroyBody(corpoBaixo);
    }

    public float getposx() {
        return this.posx;
    }

    public float getLargura() {
        return largura;
    }

    public void setLargura(float largura) {
        this.largura = largura;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public boolean isPassou() {
        return passou;
    }

    public void setPassou(boolean passou) {
        this.passou = passou;
    }

    public float getPosx() {
        return posx;
    }

    public void setPosx(float posx) {
        this.posx = posx;
    }
}
