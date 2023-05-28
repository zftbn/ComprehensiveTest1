package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class PlayerCollisionsTest {
    private PlayerCollisions playerCollisions;
    private PointCalculator pointCalculator;

    @BeforeEach
    void setup(){
        pointCalculator=mock(PointCalculator.class);
        playerCollisions=new PlayerCollisions(pointCalculator);
    }

    @Test
    @DisplayName("Player碰撞Ghost")
    @Order(1)
    void playerCollideGhost(){
        Player player=mock(Player.class);
        Ghost ghost=mock(Ghost.class);
        playerCollisions.collide(player,ghost);
        verify(pointCalculator).collidedWithAGhost(player,ghost);
        verify(player).setAlive(false);
        verify(player).setKiller(ghost);
    }

    @Test
    @DisplayName("Player碰撞Pellet")
    @Order(2)
    void playerCollidePellet(){
        Player player=mock(Player.class);
        Pellet pellet=mock(Pellet.class);
        playerCollisions.collide(player,pellet);
        verify(pointCalculator).consumedAPellet(player,pellet);
        verify(pellet).leaveSquare();
    }

    @Test
    @DisplayName("Ghost碰撞Player")
    @Order(3)
    void ghostCollidePlayer(){
        Player player=mock(Player.class);
        Ghost ghost=mock(Ghost.class);
        playerCollisions.collide(ghost,player);
        verify(pointCalculator).collidedWithAGhost(player,ghost);
        verify(player).setAlive(false);
        verify(player).setKiller(ghost);
    }

    @Test
    @DisplayName("Pellet碰撞Player")
    @Order(4)
    void pelletCollidePlayer(){
        Player player=mock(Player.class);
        Pellet pellet=mock(Pellet.class);
        playerCollisions.collide(pellet,player);
        verify(pointCalculator).consumedAPellet(player,pellet);
        verify(pellet).leaveSquare();
    }

    @Test
    @DisplayName("无效碰撞")
    @Order(5)
    void invalidCollision() {
        Player player = mock(Player.class);
        Ghost ghost = mock(Ghost.class);
        Pellet pellet = mock(Pellet.class);
        //规则5:Ghost碰撞Ghost
        playerCollisions.collide(ghost, ghost);
        //规则6:Ghost碰撞Pellet
        playerCollisions.collide(ghost, pellet);
        //规则7:Pellet碰撞Ghost
        playerCollisions.collide(pellet, ghost);
        //判断调用与否
        verify(player,times(0)).setAlive(false);
        verify(player,times(0)).setKiller(ghost);
        verify(pellet,times(0)).leaveSquare();
    }
}
