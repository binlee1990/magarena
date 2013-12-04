package magic.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.MenuPanel;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class DeckEditorScreenOptions extends TexturedPanel implements IMenuOverlay {

    private final MagicFrame frame;
    private final DeckEditorScreen screen;

    public DeckEditorScreenOptions(final MagicFrame frame0, final DeckEditorScreen screen0) {

        this.frame = frame0;
        this.screen = screen0;

        setBackground(FontsAndBorders.IMENUOVERLAY_BACKGROUND_COLOR);
        setLayout(new MigLayout("insets 0, gap 10, flowx, center, center"));
        add(getScreenMenu());
        add(getGeneralMenu());

        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseMotionAdapter() {});

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeMenu");
        getActionMap().put("closeMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideOverlay();
            }
        });

        frame0.setGlassPane(this);
        setVisible(true);

    }

    private MenuPanel getGeneralMenu() {
        final MenuPanel menuPanel = new GeneralMenuPanel(frame, this);
        menuPanel.setBackground(FontsAndBorders.IMENUOVERLAY_MENUPANEL_COLOR);
        return menuPanel;
    }

    private MenuPanel getScreenMenu() {

        final MenuPanel menu = new MenuPanel("Deck Options");

        menu.addMenuItem("New empty deck", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                screen.createNewEmptyDeck();
                hideOverlay();
            }
        });
//        menu.addMenuItem("New random deck", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                setVisible(false);
//            }
//        });
        menu.addMenuItem("Load deck", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideAllMenuPanels();
                screen.loadDeck();
                hideOverlay();
            }
        });
        menu.addMenuItem("Save deck", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideAllMenuPanels();
                screen.saveDeck();
                hideOverlay();
            }
        });
        menu.addBlankItem();
        menu.addMenuItem("Close menu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideOverlay();
            }
        });

        menu.refreshLayout();
        menu.setBackground(FontsAndBorders.IMENUOVERLAY_MENUPANEL_COLOR);
        return menu;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMenuOverlay#hideOverlay()
     */
    @Override
    public void hideOverlay() {
        setVisible(false);
    }

    /* (non-Javadoc)
     * @see magic.ui.IMenuOverlay#hideAllMenuPanels()
     */
    @Override
    public void hideAllMenuPanels() {
        for (Component component : getComponents()) {
            if (component instanceof MenuPanel) {
                ((MenuPanel) component).setVisible(false);
            }
        }
    }

}
