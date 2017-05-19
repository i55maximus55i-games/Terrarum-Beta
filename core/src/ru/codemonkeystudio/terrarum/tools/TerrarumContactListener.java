package ru.codemonkeystudio.terrarum.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import ru.codemonkeystudio.terrarum.objects.Player;

/**
 * Created by maximus on 19.05.2017.
 */

public class TerrarumContactListener implements ContactListener {
    private Player player;

    public TerrarumContactListener(Player player) {
        this.player = player;
    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        if (contact.getFixtureA().getBody().equals(player.getBody()) || contact.getFixtureB().getBody().equals(player.getBody())) {
            player.hit();
        }
    }
}
