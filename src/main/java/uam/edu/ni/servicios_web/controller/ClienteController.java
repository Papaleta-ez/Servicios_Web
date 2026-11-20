package uam.edu.ni.servicios_web.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import uam.edu.ni.servicios_web.model.Cliente;
import uam.edu.ni.servicios_web.service.ClienteService;

import java.util.List;

public class ClienteController {
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colCorreo;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colCiudad;
    @FXML private TableColumn<Cliente, Double> colIngreso;
    @FXML private TableColumn<Cliente, Integer> colPuntaje;
    @FXML private TableColumn<Cliente, String> colDeudor;
    @FXML private Button btnConsultar;
    @FXML private Label lblEstado;

    private final ClienteService service = new ClienteService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colIngreso.setCellValueFactory(new PropertyValueFactory<>("ingresoAnual"));
        colPuntaje.setCellValueFactory(new PropertyValueFactory<>("puntajeCredito"));
        colDeudor.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().isDeudor() ? "Sí" : "No"));
    }

    @FXML
    private void consultarClientes() {
        Task<List<Cliente>> consulta = new Task<>() {
            @Override
            protected List<Cliente> call() throws Exception {
                return service.obtenerClientes();
            }
        };

        lblEstado.setText("Consultando clientes...");
        btnConsultar.disableProperty().bind(consulta.runningProperty());
        consulta.setOnSucceeded(e -> {
            List<Cliente> clientes = consulta.getValue();
            tablaClientes.setItems(FXCollections.observableArrayList(clientes));
            lblEstado.setText("Clientes cargados: " + clientes.size());
        });
        consulta.setOnFailed(e -> lblEstado.setText("Error al consultar: " + consulta.getException()));

        Thread hilo = new Thread(consulta);
        hilo.setDaemon(true);
        hilo.start();
    }
}
