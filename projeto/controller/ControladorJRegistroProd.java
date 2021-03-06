package projeto.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import projeto.dao.ProdutoDAO;
import projeto.dao.TipoDAO;
import projeto.modelo.Produto;
import projeto.modelo.TipoProduto;
import projeto.modelo.Usuario;
import projeto.servicos.DbConnection;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class ControladorJRegistroProd implements Initializable {

    @FXML
    private TextField nomeProduto;

    @FXML
    private Button cadastrarBnt;

    @FXML
    private TextField precoProduto;

    @FXML
    private TextArea descProduto;


    @FXML
    private ComboBox<TipoProduto> tiposBox;

    @FXML
    private Label erroLabel;

    @FXML
    private Spinner<Integer> qntSpinner;

    private Connection connection;

    private static Usuario usuario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = DbConnection.getConexao();
        usuario = ControladorJPrincipal.usuarioAtual;
        carregarTipos();

        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100);
        qntSpinner.setValueFactory(spinnerValueFactory);
        qntSpinner.setEditable(true);
    }

    public void carregarTipos() {
        TipoDAO tipoDAO = new TipoDAO(connection);
        List<TipoProduto> tipoProdutoList = tipoDAO.getAll();
        ObservableList<TipoProduto> observableList = FXCollections.observableList(tipoProdutoList);
        tiposBox.setItems(observableList);
    }

    public void gerarNovoPodruto() {
        TipoProduto tipoSelecionado = tiposBox.getSelectionModel().getSelectedItem();
        if ((nomeProduto != null && !nomeProduto.getText().isEmpty()) && (precoProduto != null && !precoProduto.getText().isEmpty()) &&
                (descProduto != null && !descProduto.getText().isEmpty()) && (tipoSelecionado != null)) {

            Produto novoProduto = new Produto(nomeProduto.getText().toUpperCase(), descProduto.getText().toUpperCase(),
                    precoProduto.getText().replaceAll(",", "."), usuario,
                    tiposBox.getSelectionModel().getSelectedItem() , qntSpinner.getValue());

            ProdutoDAO produtoDAO = new ProdutoDAO(connection);
            produtoDAO.add(novoProduto);
            Stage stage = (Stage) cadastrarBnt.getScene().getWindow();
            stage.close();
        } else {
            erroLabel.setVisible(true);
        }

        nomeProduto.setText("");
        descProduto.setText("");
        precoProduto.setText("");

    }
}
