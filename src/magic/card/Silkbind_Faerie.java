package magic.card;

import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapCreatureActivation;
import magic.model.event.MagicTiming;
import magic.model.event.MagicUntapEvent;

public class Silkbind_Faerie {
    public static final MagicPermanentActivation A1 = new MagicTapCreatureActivation(
        new MagicCondition[]{
            MagicCondition.CAN_UNTAP_CONDITION,
            MagicConditionFactory.ManaCost("{1}{W/U}")
        },
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{1}{W/U}")),
                new MagicUntapEvent(source)
            };
        }
    };
}
