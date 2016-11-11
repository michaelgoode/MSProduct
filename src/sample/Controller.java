package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Controller {

    @FXML
    private Button btnAddProduct;
    @FXML
    private TextField txtCallout;
    @FXML
    private TextField txtProductCode;
    @FXML
    private TextField txtStyle;
    @FXML
    private TextField txtInternational;
    @FXML
    private Label lblResult;

    @FXML
    public void addProduct() {

        System.out.println("add a product");

        // add the product

        System.out.println(txtCallout.getText());

        System.out.println(txtProductCode.getText());

        System.out.println(txtInternational.getText());

        System.out.println(txtStyle.getText());

        int count = 0;

        try {

            DBManager db = DBManager.getInstance();
            Connection msedi_conn = db.getMSEDIConnection();
            Connection edi_conn = db.getEDIConnection();

            count = saveProduct(edi_conn, txtCallout.getText().trim().toUpperCase(),
                    txtProductCode.getText().trim().toUpperCase(),
                    txtStyle.getText().trim().toUpperCase(),
                    txtInternational.getText().trim().toUpperCase());
            count = saveProduct(msedi_conn, txtCallout.getText().trim().toUpperCase(),
                    txtProductCode.getText().trim().toUpperCase(),
                    txtStyle.getText().trim().toUpperCase(),
                    txtInternational.getText().trim().toUpperCase());

            msedi_conn.close();
            edi_conn.close();

        } catch (SQLException ex) {
            lblResult.setText(ex.getMessage());
            ex.printStackTrace();
        }

        if (count > 0) {

            lblResult.setText("Added product " + txtCallout.getText().trim());

        }

        txtCallout.setText("");

        txtProductCode.setText("");

        txtInternational.setText("");


    }

    private int saveProduct(Connection conn, String callout, String product, String style, String international) {

        String sql = "insert into edi_lookup_value (FK_lookup_ConditionID,CValue1,UValue1,UValue2,CValue3) values (267,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, callout);
            stmt.setString(2, product);
            stmt.setString(3, style);
            stmt.setString(4, international);
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }

    }

}
