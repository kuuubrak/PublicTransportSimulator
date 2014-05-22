package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class MainWindow extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = -3543647902381142115L;
    private static final int minWindowWidth = 1200;
    private JPanel contentPane;
    private Canvas worldCanvas;
    private Canvas detailCanvas;
    private JPanel buttonsPanel;

    /**
     * Create the frame.
     */
    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, minWindowWidth, minWindowWidth / 2);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        worldCanvas = new Canvas() {
            @Override
            public void paint(Graphics g) {
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
        };
        contentPane.add(worldCanvas);

        detailCanvas = new DetailViewender(820, 10, 360, 300);
        contentPane.add(detailCanvas);

        buttonsPanel = new JPanel();
        buttonsPanel.setBounds(300, 10, 350, 250);
        contentPane.add(buttonsPanel);
        buttonsPanel.setLayout(null);

        JToggleButton switchSimulationButton = new JToggleButton("Auto simulation");
        switchSimulationButton.setBounds(12, 12, 192, 25);
        buttonsPanel.add(switchSimulationButton);


        JButton stepButton = new JButton("Next step");
        stepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        stepButton.setBounds(12, 49, 117, 79);
        buttonsPanel.add(stepButton);

        JButton btnAddNewPassanger = new JButton("Add new passanger");
        btnAddNewPassanger.setBounds(12, 153, 175, 46);
        buttonsPanel.add(btnAddNewPassanger);

        this.addComponentListener(new ComponentListener() {

            @Override
            public void componentShown(ComponentEvent arg0) {

            }

            @Override
            public void componentResized(ComponentEvent arg0) {
                resize();

            }

            @Override
            public void componentMoved(ComponentEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void componentHidden(ComponentEvent arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainWindow frame = new MainWindow();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void resize() {

        if (getWidth() < minWindowWidth) {
            setSize(minWindowWidth, minWindowWidth / 2);
        } else if (getHeight() < getWidth() / 2) {
            setSize(getWidth(), getWidth() / 2);
        } else {
            worldCanvas.setBounds(10, 10, (int) (0.6 * getWidth()), (int) (0.45 * getWidth()));
            remove(detailCanvas);
            detailCanvas = new DetailViewender(worldCanvas.getWidth() + 20, 10, (int) (0.64 * getHeight()), (int) (0.48 * getHeight()));
            add(detailCanvas);
            buttonsPanel.setBounds(worldCanvas.getWidth() + 20, detailCanvas.getHeight() + 40, buttonsPanel.getWidth(), buttonsPanel.getHeight());
        }
    }

}
