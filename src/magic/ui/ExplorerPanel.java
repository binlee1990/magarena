package magic.ui;

import magic.data.CardImagesProvider;
import magic.model.MagicCardDefinition;
import magic.model.MagicCubeDefinition;
import magic.model.MagicDeck;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicRandom;
import magic.ui.viewer.CardViewer;
import magic.ui.viewer.DeckStatisticsViewer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


public class ExplorerPanel extends TexturedPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    public static final int ALL = 0;
    public static final int LAND = 1;
    public static final int SPELL = 2;

    private static final String ADD_BUTTON_TEXT = "Add";
    private static final String REMOVE_BUTTON_TEXT = "Remove";
    private static final String CARD_POOL_TITLE = "Card Pool";
    private static final int SPACING=10;

    protected final MagicFrame frame;
    private final MagicPlayerDefinition player;

    private final CardTable cardPoolTable;
    private final CardTable deckTable;
    private final CardViewer cardViewer;
    private final DeckStatisticsViewer statsViewer;
    private final ExplorerFilterPanel filterPanel;
    private final JButton addButton;
    private final JButton removeButton;

    private List<MagicCardDefinition> cardPoolDefs;
    private MagicDeck deckDefs;

    public ExplorerPanel(final MagicFrame frame) {
        this(frame, ExplorerPanel.ALL, null, null);
    }

    public ExplorerPanel(
            final MagicFrame frame,
            final int mode,
            final MagicPlayerDefinition player,
            final MagicCubeDefinition cube) {

        this.frame=frame;
        this.player=player;

        setBackground(FontsAndBorders.MAGSCREEN_FADE_COLOR);

        final SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        // buttons
        final JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setOpaque(false);

        // create buttons for deck editing
        if (isEditingDeck()) {
            addButton = new JButton(ADD_BUTTON_TEXT);
            addButton.setFont(FontsAndBorders.FONT1);
            addButton.setFocusable(false);
            addButton.addActionListener(this);
            buttonsPanel.add(addButton);

            buttonsPanel.add(Box.createHorizontalStrut(SPACING));

            removeButton = new JButton(REMOVE_BUTTON_TEXT);
            removeButton.setFont(FontsAndBorders.FONT1);
            removeButton.setFocusable(false);
            removeButton.addActionListener(this);
            buttonsPanel.add(removeButton);

            buttonsPanel.add(Box.createHorizontalStrut(SPACING));
        } else {
            addButton = null;
            removeButton = null;
        }

        add(buttonsPanel);

        // left side (everything but buttons)
        final JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        // card image
        cardViewer = new CardViewer(false,true);
        cardViewer.setPreferredSize(CardImagesProvider.CARD_DIMENSION);
        cardViewer.setMaximumSize(CardImagesProvider.CARD_DIMENSION);
        leftPanel.add(cardViewer);

        // deck statistics
        if (isEditingDeck()) {
            leftPanel.add(Box.createVerticalStrut(SPACING));
            statsViewer = new DeckStatisticsViewer();
            statsViewer.setMaximumSize(DeckStatisticsViewer.PREFERRED_SIZE);
            leftPanel.add(statsViewer);
        } else {
            statsViewer = null;
        }

        // add scrolling to left side
        final JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftScrollPane.setBorder(FontsAndBorders.NO_BORDER);
        leftScrollPane.setBackground(java.awt.Color.green);
        leftPanel.setOpaque(false);
        leftPanel.setBorder(FontsAndBorders.NO_BORDER);
        leftPanel.setBackground(java.awt.Color.yellow);
        leftScrollPane.setOpaque(false);
        leftScrollPane.getViewport().setOpaque(false);
        leftScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(leftScrollPane);

        // filters
        MagicPlayerProfile profile=null;
        if (isEditingDeck()) {
            profile=getPlayer().getProfile();
        }
        filterPanel = new ExplorerFilterPanel(frame, this, mode, profile, cube);

        final JScrollPane filterScrollPane = new JScrollPane(filterPanel);
        filterScrollPane.setBorder(FontsAndBorders.NO_BORDER);
        filterScrollPane.setOpaque(false);
        filterScrollPane.getViewport().setOpaque(false);
        filterScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        add(filterScrollPane);

        // card pool
        cardPoolDefs = filterPanel.getCardDefinitions();

        // deck
        final Container cardsPanel; // reference panel holding both card pool and deck

        if (isEditingDeck()) {
            cardPoolTable = new CardTable(cardPoolDefs, cardViewer, generatePoolTitle(), false);
            cardPoolTable.addMouseListener(new CardPoolMouseListener());

            deckDefs = getPlayer().getDeck();
            deckTable = new CardTable(deckDefs, cardViewer, generateDeckTitle(deckDefs), true);
            deckTable.addMouseListener(new DeckMouseListener());

            final JSplitPane cardsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            cardsSplitPane.setOneTouchExpandable(true);
            cardsSplitPane.setLeftComponent(cardPoolTable);
            cardsSplitPane.setRightComponent(deckTable);
            cardsSplitPane.setResizeWeight(0.5);

            add(cardsSplitPane);
            cardsPanel = cardsSplitPane;

            // update deck stats
            statsViewer.setPlayer(getPlayer());
        } else {
            // no deck
            cardPoolTable = new CardTable(cardPoolDefs, cardViewer);
            deckDefs = null;
            deckTable = null;

            add(cardPoolTable);
            cardsPanel = cardPoolTable;
        }

        // set sizes by defining gaps between components
        final Container contentPane = this;

        // card image's gap (top left)
        springLayout.putConstraint(SpringLayout.WEST, leftScrollPane,
                             SPACING, SpringLayout.WEST, contentPane);
        springLayout.putConstraint(SpringLayout.NORTH, leftScrollPane,
                             SPACING, SpringLayout.NORTH, contentPane);

        // gap between card image and filter
        springLayout.putConstraint(SpringLayout.WEST, filterScrollPane,
                             SPACING, SpringLayout.EAST, leftScrollPane);

        // filter panel's gaps (top right)
        springLayout.putConstraint(SpringLayout.NORTH, filterScrollPane,
                             0, SpringLayout.NORTH, leftScrollPane);
        springLayout.putConstraint(SpringLayout.EAST, filterScrollPane,
                             -SPACING, SpringLayout.EAST, contentPane);

        // filter panel's gap with card tables
        springLayout.putConstraint(SpringLayout.WEST, cardsPanel,
                             0, SpringLayout.WEST, filterScrollPane);
        springLayout.putConstraint(SpringLayout.NORTH, cardsPanel,
                             SPACING, SpringLayout.SOUTH, filterScrollPane);

        // card tables' gap (right)
        springLayout.putConstraint(SpringLayout.EAST, cardsPanel,
                             -SPACING, SpringLayout.EAST, contentPane);
        springLayout.putConstraint(SpringLayout.SOUTH, cardsPanel,
                                 -SPACING, SpringLayout.SOUTH, contentPane);

        // buttons' gap (top right bottom)
        springLayout.putConstraint(SpringLayout.EAST, buttonsPanel,
                             0, SpringLayout.EAST, leftScrollPane);
        springLayout.putConstraint(SpringLayout.SOUTH, leftScrollPane,
                             -SPACING, SpringLayout.NORTH, buttonsPanel);
        springLayout.putConstraint(SpringLayout.SOUTH, buttonsPanel,
                             -SPACING, SpringLayout.SOUTH, contentPane);

        // set initial card image
        if (cardPoolDefs.isEmpty()) {
            cardViewer.setCard(MagicCardDefinition.UNKNOWN,0);
         } else {
             final int index = MagicRandom.nextRNGInt(cardPoolDefs.size());
             cardViewer.setCard(cardPoolDefs.get(index),0);
         }
    }

     private String generatePoolTitle() {
         return CARD_POOL_TITLE + " - " + cardPoolDefs.size() + " cards";
     }

    private boolean isEditingDeck() {
        return player != null;
    }

    public MagicPlayerDefinition getPlayer() {
        return player;
    }

    String generateDeckTitle(final MagicDeck deck) {
        return "Deck (" + deck.getName() + ") - " + deck.size() + " cards";
    }

    public void updateCardPool() {
        cardPoolDefs = filterPanel.getCardDefinitions();
        cardPoolTable.setCards(cardPoolDefs);
        if(isEditingDeck()) {
             cardPoolTable.setTitle(generatePoolTitle());
         }
    }

    public void updateDeck() {
        if(isEditingDeck()) {
            deckDefs = getPlayer().getDeck();
            deckTable.setTitle(generateDeckTitle(deckDefs));
            deckTable.setCards(deckDefs);
            statsViewer.setPlayer(getPlayer());
        }
    }

    private void removeSelectedFromDeck() {
        final List<MagicCardDefinition> deckCards = deckTable.getSelectedCards();

        if (deckCards.size() > 0) {
            for(final MagicCardDefinition card : deckCards) {
                getPlayer().getDeck().remove(card);
            }

            updateDeck();

        } else {
            // display error
            JOptionPane.showMessageDialog(frame, "Select a valid card in the deck to remove it.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSelectedToDeck() {
        final List<MagicCardDefinition> cardPoolCards = cardPoolTable.getSelectedCards();

        if (cardPoolCards.size() > 0) {
            for(final MagicCardDefinition card : cardPoolCards) {
                getPlayer().getDeck().add(card);
            }

            updateDeck();

        } else {
            // display error
            JOptionPane.showMessageDialog(frame, "Select a valid card in the card pool to add it to the deck.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Object source=event.getSource();

        if (isEditingDeck()) {
            if(source == addButton) {
                addSelectedToDeck();
            } else if(source == removeButton) {
                removeSelectedFromDeck();
            }
        }
    }

    protected void close() {
        filterPanel.closePopups();
    }

    //protected abstract void closeScreen();

    private class CardPoolMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent e) {
            if(isEditingDeck() && e.getClickCount() > 1) {
                addSelectedToDeck();
            }
        }
    }

    private class DeckMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent e) {
            if(isEditingDeck()) {
                if (e.getClickCount() > 1) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        removeSelectedFromDeck();
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        final List<MagicCardDefinition> deckCards = deckTable.getSelectedCards();
                        if (deckCards.size() > 0) {
                            for(final MagicCardDefinition card : deckCards) {
                                getPlayer().getDeck().add(card);
                            }

                            updateDeck();

                        }
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    final JTable table = (JTable)(e.getSource());
                    final int row = table.rowAtPoint(e.getPoint());
                    table.clearSelection();
                    table.addRowSelectionInterval(row,row);
                }
            }
        }
    }

}
