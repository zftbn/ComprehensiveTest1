package nl.tudelft.jpacman.npc.ghost;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.google.common.collect.Lists;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;


public class ClydeTest {

    private GhostMapParser ghostMapParser;

    /**
     *实例化地图解析对象.
     */
    @BeforeEach
    void setup() {
        PacManSprites sprites = new PacManSprites();
        GhostFactory ghostFactory = new GhostFactory(sprites);
        BoardFactory boardFactory = new BoardFactory(sprites);
        LevelFactory levelFactory = new LevelFactory(sprites, ghostFactory, mock(PointCalculator.class));
        ghostMapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }

    /**
     *clyde距player少于8格.
     */
    @Test
    @DisplayName("clyde距player少于8格")
    void departLessThanEight() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "###########",
                "#.C.......P",
                "###########"));
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);
        Optional<Direction> opt = clyde.nextAiMove();
        assertThat(opt.get()).isEqualTo(Direction.valueOf("WEST"));
    }

    /**
     *clyde距player多于8格.
     */
    @Test
    @DisplayName("clyde距player多于8格")
    void departMoreThanEight() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "##############",
                "#.C..........P",
                "##############"));

        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);
        Optional<Direction> opt = clyde.nextAiMove();
        assertThat(opt.get()).isEqualTo(Direction.valueOf("EAST"));
    }

    /**
     *没有player对象.
     */
    @Test
    @DisplayName("没有player对象")
    void departWithoutPlayer() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "##############",
                "#.C...........",
                "##############"));

        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertThat(level.isAnyPlayerAlive()).isFalse();
        Optional<Direction> opt = clyde.nextAiMove();
        assertThat(opt.isPresent()).isFalse();
    }

    /**
     *clyde和player无路径可达.
     */
    @Test
    @DisplayName("clyde和player无路径可达")
    void withoutPathBetweenClydeAndPlayer() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "#############P",
                "#......C.....#",
                "##############"));

        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);
        Optional<Direction> opt = clyde.nextAiMove();
        assertThat(opt.isPresent()).isFalse();
    }
}
