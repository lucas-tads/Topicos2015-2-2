package br.grupointegrado.ads.flappybird;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by lucas on 05/10/2015.
 */
public class Util {

    public static  final float ESCALA = 2;
    public static final float PIXEL_METRO = 32;
    public static final float ALTURA_BORDA = 80 / PIXEL_METRO;
    public static final float ALTURA_CHAO = 2.5f;

    /**
     * cria o corpo dentro do mundo
     * @param mundo
     * @param tipo
     * @param x
     * @param y
     * @return
     */
    public static Body criarcorpo(World mundo, BodyDef.BodyType tipo, float x, float y) {
        BodyDef definicao = new BodyDef();
        definicao.type = tipo;
        definicao.position.set(x , y);
        definicao.fixedRotation = true;
        Body corpo = mundo.createBody(definicao);

        return  corpo;
    }

    /**
     * cria uma forma para o corpo
     * @param corpo
     * @param shape forma geometrcica parA o corpo     *
     * @param nome nome para utilizar na colisao
     * @return
     */
    public static Fixture criarforma(Body corpo, Shape shape, String nome){
        FixtureDef definicao = new FixtureDef();
        definicao.density = 1; //densidade do corpo
        definicao.friction = 0.6f; //friccao ou atirto enttre um corpo e outro
        definicao.restitution = 0.3f; //elasticidade do corpo
        definicao.shape = shape;

        Fixture forma = corpo.createFixture(definicao);
        forma.setUserData(nome);  //identificacao da forma
        return  forma;
    }
}
