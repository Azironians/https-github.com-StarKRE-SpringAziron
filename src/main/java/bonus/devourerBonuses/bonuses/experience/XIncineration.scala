package bonus.devourerBonuses.bonuses.experience

import javafx.scene.image.ImageView
import management.actionManagement.service.components.providerComponent.ProviderComponent
import management.actionManagement.service.engine.services.DynamicHandleService

import scala.collection.mutable

final class XIncineration(name: String, id: Int, sprite: ImageView) extends Bonus(name, id, sprite)
  with DynamicHandleService {

  private val previousProviderComponentList = new mutable.MutableList[ProviderComponent[java.lang.Integer]]()

  override def use(): Unit = {
    //Install handler:
    this.actionManager.getEventEngine.addHandler(getHandlerInstance)
    //Change bonus provider components:
    val opponentHero = playerManager.getOpponentTeam.getCurrentPlayer.getCurrentHero
    val bonusManager = opponentHero.getBonusManager
    val providerComponentList = bonusManager.getProviderComponentList
    for (i <- 0 until providerComponentList.size()){
      val previousProviderComponent = providerComponentList.get(i)
      this.previousProviderComponentList.+=(previousProviderComponent)
      val emptyProviderComponent = this.getEmptyProviderComponent
      bonusManager.setCustomProviderComponent(i, emptyProviderComponent)
    }
  }

  private def getEmptyProviderComponent = new ProviderComponent[java.lang.Integer] {

    private val undefined: Int = -1

    override final def getValue: Integer = undefined

    override final def getPriority: Int = 0

    override final def setPriority(priority: Int): Unit = {
      //nothing...
    }
  }

  override def getHandlerInstance: HandleComponent = new HandleComponent {

    private var player: Player = _

    private var opponentPlayer: Player = _

    private var work: Boolean = _

    override final def setup(): Unit = {
      this.player = playerManager.getCurrentTeam.getCurrentPlayer
      this.opponentPlayer = playerManager.getOpponentTeam.getCurrentPlayer
      this.work = true
    }

    override final def handle(actionEvent: ActionEvent): Unit = {
      val actionType = actionEvent.getActionType
      val player = actionEvent.getPlayer
      if (actionType == ActionType.END_TURN && this.opponentPlayer == player){
        val opponentHero = player.getCurrentHero
        val bonusManager = opponentHero.getBonusManager
        val providerComponentList = bonusManager.getProviderComponentList
        for (i <- 0 until providerComponentList.size()){
          val previousProviderComponent: ProviderComponent[java.lang.Integer] = previousProviderComponentList(i)
          bonusManager.returnPreviousProviderComponent(i, previousProviderComponent)
        }
      }
    }

    override final def getName: String = "TempoSpeed"

    override final def getCurrentPlayer: Player = player

    override final def isWorking: Boolean = work

    override final def setWorking(able: Boolean): Unit = work = able
  }
}