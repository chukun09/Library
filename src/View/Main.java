package View;

import java.sql.Statement;

import com.jfoenix.controls.JFXPasswordField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.ConnectionDatabase;

public class Main extends Application {
	@FXML
	private JFXPasswordField txtpass, txtconfirm;
	@FXML
	private CheckBox checkbox;
	@FXML
	private PasswordField txtmk;
	@FXML
	private TextField txttdn, txtacc;
	@FXML
	private Button btnlogin, btnregister;
	public static Stage window, window2;
	public static Scene scene2;
	public static Parent root2, root, root3;
	@Override
	public void start(Stage primaryStage) throws IOException {
		window = primaryStage;
		window2 = new Stage();
		root3 = FXMLLoader.load(getClass().getResource("/View/Register.fxml"));
		root = FXMLLoader.load(getClass().getResource("/View/LibraryManagementSoftware.fxml"));
		root2 = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
		scene2 = new Scene(root3);
		window.setScene(new Scene(root2));
		window.show();
	}

	public void Login(ActionEvent event) throws IOException, SQLException {
		if (check() == false) {
			Notice("Nhập sai tài khoản hoặc tài khoản không tồn tại :(((");
		} else {
			window.close();
			window.setScene(new Scene(root));
			window.show();
		}
	}

	public boolean check() throws SQLException {
		Connection connection = ConnectionDatabase.ConnectionData("Library");
		Statement stat = connection.createStatement();
		ResultSet rs = stat.executeQuery("SELECT * FROM Table_Account");
		while (rs.next()) {
			if (txtmk.getText().equals(rs.getString(2).toString())
					&& txttdn.getText().equals(rs.getString(1).toString())) {
				return true;
			}
		}
		return false;
	}

	public void Register() {
		window2.setScene(scene2);
		window2.show();
	}

	public void Regis() throws SQLException {
		Connection connection = ConnectionDatabase.ConnectionData("Library");
		Statement stat = connection.createStatement();
		if (!txtpass.getText().equals(txtconfirm.getText())) {
			Notice("Mật Khẩu không trùng khớp !!!");
		} else if (!checkbox.isSelected()) {
			Notice("Vui lòng đồng ý với điều khoản sử dụng !!!");
		} else {
			try {
				String s = "Insert into Table_Account(Account, Password) values (N'" + txtacc.getText() + "' ,N'"
						+ txtpass.getText() + "')";
				stat.executeUpdate(s);
			} catch (Exception e) {
				Notice("Tài Khoản đã tồn tại !");
				return;
			}
			Notice("Register Completed !");
			txtacc.clear();
			txtconfirm.clear();
			txtpass.clear();
			window2.close();
		}
	}

	public void Notice(String s) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Thông Báo");
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.showAndWait();
	}

	public static void main(String[] args) throws SQLException {
		launch(args);
	}

}
