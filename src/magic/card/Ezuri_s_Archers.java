package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Ezuri_s_Archers {
    public static final MagicWhenBlocksTrigger T = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPermanent blocked=permanent.getBlockedCreature();
            return (permanent==data && 
                    blocked.isValid() &&
                    blocked.hasAbility(MagicAbility.Flying)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +3/+0 until end of turn."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),3,0));
        }
    };
}
