package pmalope.view.gui;

import pmalope.controller.GUIController;
import pmalope.model.character.heros.Hero;
import pmalope.utils.Globals;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUIView extends JFrame implements GameInterface{
    private static final long serialVersionUID = 1L;
    private JButton startButton;

    private JLabel error;
    private JLabel loadLabel;

    private JPanel mainPanel;

    private StartView startView;
    private SelectCombo selectCombo;
    private CreateView createView;
    private GameView gameView;

    public GUIView() {
        mainPanel = new JPanel(new BorderLayout());
        JPanel subPanelCenter = new JPanel();
        subPanelCenter.setLayout(new BoxLayout(subPanelCenter, BoxLayout.Y_AXIS));

        loadLabel = new JLabel("Loading...");
        loadLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        error = new JLabel();

        startButton = new JButton();
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

//        TODO logo
        JLabel logo = new JLabel();
//        ImageIcon logoImage = new ImageIcon("assets/Swingy_logo.png");
//        logo.setIcon(logoImage);
        logo.setHorizontalAlignment(JLabel.CENTER);
        logo.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        this.setTitle("Swingy");

        startButton.setText("Get started");
        startButton.setEnabled(false);

        startButton.addActionListener(e -> {
            GUIController.eventHandler(Globals.showStartScreen);});

        subPanelCenter.add(loadLabel);
        subPanelCenter.add(startButton);
        mainPanel.add(logo, BorderLayout.PAGE_START);
        mainPanel.add(subPanelCenter, BorderLayout.CENTER);
        mainPanel.add(error, BorderLayout.PAGE_END);
        this.add(mainPanel);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void run() {
        new GUIController(new GUIView());
    }


    @Override
    public void setEnableStartButton(boolean b) {
        startButton.setEnabled(b);
    }

    @Override
    public void showStartScreen() {
        startView = new StartView();
        setContentPane(startView);
        pack();
    }

    @Override
    public void showSelectScreen(List<Hero> heroList) {
        selectCombo = new SelectCombo(heroList);
        setContentPane(selectCombo);
        pack();
    }

    @Override
    public void showCreateScreen() {
        createView = new CreateView();
        setContentPane(createView);
        pack();
//        System.out.println("create hero");
    }

    @Override
    public void showGameView(Hero hero) {
        gameView = new GameView(hero);
        setContentPane(gameView);
        pack();
    }

    @Override
    public void showErrorDialog(String error) {
        JOptionPane.showMessageDialog(
                this,
                labelFormatter(error),
                "Swingy error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    public void clearLoadingLabel() {
        loadLabel.setText("");
    }

    private static String labelFormatter(String input) {
        return "<html>" + input.replaceAll("<","&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\n", "<br/>")
                + "</html>";
    }
}