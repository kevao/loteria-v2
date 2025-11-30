package dev.loteria.gui.controllers;

import dev.loteria.services.SorteioService;
import dev.loteria.services.SorteioService.SorteioDTO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SorteioController {

    @FXML
    private VBox root;

    @FXML
    private TableView<SorteioDTO> tableView;

    @FXML
    private TableColumn<SorteioDTO, String> colModalidade;

    @FXML
    private TableColumn<SorteioDTO, String> colNumeros;

    @FXML
    private TableColumn<SorteioDTO, String> colHorario;

    @FXML
    private TableColumn<SorteioDTO, Void> colActions;

    @FXML
    private Button btnNovo;

    private final SorteioService service = new SorteioService();

    @FXML
    public void initialize() {
        colModalidade.setCellValueFactory(
                dto -> new javafx.beans.property.SimpleStringProperty(dto.getValue().getNomeModalidade()));
        colNumeros.setCellValueFactory(dto -> {
            List<?> nums = dto.getValue().getNumeros();
            String joined = "";
            if (nums != null && !nums.isEmpty()) {
                joined = nums.stream()
                        .map(Object::toString)
                        .collect(java.util.stream.Collectors.joining("-"));
            }
            return new javafx.beans.property.SimpleStringProperty(joined);
        });
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colHorario.setCellValueFactory(dto -> new javafx.beans.property.SimpleStringProperty(
                dto.getValue().getHorario() == null ? "" : dto.getValue().getHorario().format(fmt)));

        colActions.setCellFactory(col -> new TableCell<SorteioDTO, Void>() {
            private final HBox container = new HBox(8);
            private final Button btnDelete = new Button();

            {
                SVGPath deleteIcon = new SVGPath();
                deleteIcon.setContent("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
                btnDelete.setGraphic(deleteIcon);
                btnDelete.getStyleClass().addAll("action-btn", "action-btn--delete");
                btnDelete.setOnAction(e -> {
                    SorteioDTO dto = getTableView().getItems().get(getIndex());
                    try {
                        service.deletar(dto.getId());
                        refreshTable();
                    } catch (Exception ex) {
                        System.err.println("Erro ao deletar sorteio: " + ex.getMessage());
                    }
                });
                container.setAlignment(Pos.CENTER);
                container.getChildren().addAll(btnDelete);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });

        btnNovo.setOnAction(e -> openForm());
        refreshTable();
    }

    private void refreshTable() {
        List<SorteioDTO> lista = service.listarTodos();
        tableView.getItems().setAll(lista);
    }

    private void openForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sorteio_form.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
            SorteioFormController controller = loader.getController();
            controller.setOnSaved(s -> refreshTable());
            Stage stage = new Stage();
            stage.setTitle("Novo Sorteio");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            System.err.println("Erro ao abrir formulário de sorteio: " + ex.getMessage());
        }
    }
}
