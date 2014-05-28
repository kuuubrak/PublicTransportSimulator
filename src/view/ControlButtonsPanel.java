package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mateusz on 28.05.14.
 */
public class ControlButtonsPanel extends JPanel {

    private JButton stepButton;
    private JToggleButton switchSimulationButton;
    private JButton newPassengerButton;
    private JButton changeInterval;
    private ClientWrapper clientWrapper;


    public ControlButtonsPanel(int x, int y, int width, int height, ClientWrapper clientWrapper){
        super();
        setBounds(x, y, width, height);
        setLayout(null);
        this.clientWrapper = clientWrapper;
        initStepButton();
        initSwitchSimulationButton();
        initNewPassengerButton();
        initChangeIntervalButton();

    }

    private void initChangeIntervalButton() {
        changeInterval = new JButton("Change interval");
        changeInterval.setBounds(210, 120, 160, 25);
        add(changeInterval);
        changeInterval.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ChooseGenerationInterval dialog = new ChooseGenerationInterval();
                dialog.setClientWrapper(clientWrapper);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
    }

    private void initStepButton(){
        stepButton = new JButton("Next step");
        stepButton.setBounds(120, 30, 160, 80);
        add(stepButton);
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clientWrapper.nextStep();
            }
        });
    }

    private void initSwitchSimulationButton(){
        switchSimulationButton = new JToggleButton("Manual simulation");
        switchSimulationButton.setBounds(10, 120, 180, 25);
        add(switchSimulationButton);
        switchSimulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(switchSimulationButton.isSelected()){
                    switchSimulationButton.setText("Auto simulation");
                    clientWrapper.setContinuous(true);
                }else{
                    switchSimulationButton.setText("Manual simulation");
                    clientWrapper.setContinuous(false);
                }
            }
        });
    }

    private void initNewPassengerButton(){
        newPassengerButton = new JButton("New passenger");
        newPassengerButton.setBounds(10, 150, 180, 45);
        add(newPassengerButton);
        newPassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clientWrapper.createPassenger("Z tąd", "Do tąd"); //TODO:
            }
        });
    }



}
