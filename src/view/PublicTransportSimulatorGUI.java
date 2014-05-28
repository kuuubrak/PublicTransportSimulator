package view;

import mockup.Mockup;

/**
 * Created by mateusz on 27.05.14.
 */
public class PublicTransportSimulatorGUI implements GuiFunctionality{

    private ClientWrapper clientWrapper;
    private ViewModel viewModel;
    private static MainWindow mainWindow;

    private static PublicTransportSimulatorGUI simulatorGUI;

    public PublicTransportSimulatorGUI() {
    }

    public static void main(String[] args) {
        simulatorGUI = new PublicTransportSimulatorGUI();
        simulatorGUI.clientWrapper = new ClientWrapper(simulatorGUI);
        simulatorGUI.clientWrapper.start();
        simulatorGUI.viewModel = new ViewModel();
        mainWindow = new MainWindow(simulatorGUI.clientWrapper, simulatorGUI.viewModel);
        mainWindow.setVisible(true);
    }

    @Override
    public void connectionEstablished() {

    }

    @Override
    public void connectionLost() {

    }

    @Override
    public void newMockup(Mockup fresh) {
        viewModel.udateBusStopList(fresh.getBusStops());
        viewModel.updateBusList(fresh.getBuses());
    }
}
