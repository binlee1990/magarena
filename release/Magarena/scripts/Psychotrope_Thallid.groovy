[
    new MagicPermanentActivation(
        [
            MagicConditionFactory.ManaCost("{1}"),
            MagicCondition.ONE_SAPROLING_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Draw),
        "Draw") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(
                    source,
                    source.getController(),
                    MagicManaCost.create("{1}")
                ),
                new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.SACRIFICE_SAPROLING
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Draw a card."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicDrawAction(event.getPlayer(), 1));
        }
    }
]
