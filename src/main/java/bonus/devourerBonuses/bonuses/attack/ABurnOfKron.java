package bonus.devourerBonuses.bonuses.attack;

import bonus.bonuses.Bonus;
import javafx.scene.image.ImageView;
import management.actionManagement.actions.ActionEvent;
import management.actionManagement.actions.ActionEventFactory;
import management.actionManagement.actions.ActionType;
import management.actionManagement.service.components.handleComponet.HandleComponent;
import management.actionManagement.service.engine.services.DynamicHandleService;
import management.playerManagement.Player;

import java.util.logging.Logger;

public final class ABurnOfKron extends Bonus implements DynamicHandleService{

    private static final Logger log = Logger.getLogger(ABurnOfKron.class.getName());

    private static final double DAMAGE_COEFFICIENT = 0.05;

    private static final int TURNS = 5;

    public ABurnOfKron(final String name, final int id, final ImageView sprite) {
        super(name, id, sprite);
    }

    @Override
    public final void use() {
        final HandleComponent handler = getHandlerInstance();
        actionManager.getEventEngine().addHandler(handler);
    }

    @Override
    public final HandleComponent getHandlerInstance() {
        return new HandleComponent() {

            private int count = TURNS;

            private Player player;

            private Player opponentPlayer;

            private double damage;

            @Override
            public final void setup() {
                this.player = playerManager.getCurrentTeam().getCurrentPlayer();
                this.damage = player.getCurrentHero().getAttack() * DAMAGE_COEFFICIENT;
                this.opponentPlayer = playerManager.getOpponentTeam().getCurrentPlayer();
            }

            @Override
            public final void handle(final ActionEvent actionEvent) {
                if (actionEvent.getActionType() == ActionType.START_TURN && actionEvent.getPlayer()
                        .equals(opponentPlayer)){
                    if (opponentPlayer.getCurrentHero().getDamage(damage)) {
                        actionManager.getEventEngine().handle(ActionEventFactory.getAfterDealDamage(player
                                , opponentPlayer.getCurrentHero(), damage));
                    }
                }
                if (actionEvent.getActionType() == ActionType.END_TURN && actionEvent.getPlayer() == player) {
                    if (isWorking()) {
                        count--;
                        log.info("COUNTDOWN: " + count);
                    }
                }
            }

            @Override
            public final String getName() {
                return "BurnOfKron";
            }

            @Override
            public final Player getCurrentPlayer() {
                return player;
            }

            @Override
            public final boolean isWorking() {
                return count > 0;
            }

            @Override
            public final void setWorking(final boolean able) {
                if (able){
                    count++;
                } else {
                    count = 0;
                }
            }
        };
    }
}