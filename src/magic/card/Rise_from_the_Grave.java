package magic.card;

import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicAddStaticAction;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicGraveyardTargetPicker;

import java.util.EnumSet;

public class Rise_from_the_Grave {
    private static final MagicStatic Zombie = new MagicStatic(MagicLayer.Type) {
         @Override
         public void modSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
             flags.add(MagicSubType.Zombie);
         }
    };
    
    private static final MagicStatic Black = new MagicStatic(MagicLayer.Color) {
         @Override
         public int getColorFlags(final MagicPermanent permanent,final int flags) {
             return flags|MagicColor.Black.getMask();
         }
    };

    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                    new MagicGraveyardTargetPicker(true),
                    this,
                    "Put target creature$ card from a graveyard onto the " +
                    "battlefield under your control. That creature is a black " +
                    "Zombie in addition to its other colors and types.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    final MagicPlayer player = event.getPlayer();
                    
                    final MagicPlayCardAction action = new MagicPlayCardAction(targetCard,player,MagicPlayCardAction.NONE);
                    game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
                    game.doAction(action);

                    final MagicPermanent permanent = action.getPermanent();
                    game.doAction(new MagicAddStaticAction(permanent, Zombie));
                    game.doAction(new MagicAddStaticAction(permanent, Black));
                }
            });
        }
    };
}
