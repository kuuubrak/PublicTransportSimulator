package view;

import mockup.Mockup;

/**
 * Created by mateusz on 27.05.14.
 */
public class PublicTransportSimulatorGUI implements GuiFunctionality{

    private ClientWrapper clientWrapper;
    private ViewModel viewModel;
    private MainWindow mainWindow;

    private static PublicTransportSimulatorGUI simulatorGUI;

    public PublicTransportSimulatorGUI() {
    }

    public static void main(String[] args) {
        simulatorGUI = new PublicTransportSimulatorGUI();
        simulatorGUI.clientWrapper = new ClientWrapper(simulatorGUI);
        simulatorGUI.clientWrapper.start();
        simulatorGUI.viewModel = new ViewModel();
        simulatorGUI.mainWindow = new MainWindow(simulatorGUI.clientWrapper, simulatorGUI.viewModel);
        simulatorGUI.mainWindow.setVisible(true);
    }

    @Override
    public void connectionEstablished() {

    }

    @Override
    public void connectionLost() {

    }

    @Override
    public synchronized void newMockup(Mockup fresh) {
        viewModel.udateBusStopList(fresh.getBusStops());
        viewModel.updateBusList(fresh.getBuses());
        simulatorGUI.mainWindow.updateViewModel(viewModel);
        simulatorGUI.mainWindow.repaint();
    }
}
