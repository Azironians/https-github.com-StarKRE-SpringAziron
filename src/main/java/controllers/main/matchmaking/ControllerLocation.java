package controllers.main.matchmaking;

import com.google.inject.Inject;
import management.actionManagement.ActionManager;
import management.playerManagement.ATeam;
import management.playerManagement.PlayerManager;

abstract class ControllerLocation {

    @Inject
    private ActionManager actionManager;

    @Inject
    protected PlayerManager playerManager;

    final void makeHeroRequest(final ATeam aTeam){
        actionManager.setHeroRequest(aTeam);
    }

    final void makeSwapHeroRequest(final ATeam aTeam){
        actionManager.setPlayerSwapRequest(aTeam);
    }
}
