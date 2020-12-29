package Controller;

import java.net.*;
import java.sql.*;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import model.ConnectionDatabase;
import model.DocGia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class TableViewDocGia implements Initializable {
	@FXML
	private Button btn_changereader, btn_deletereader, btn_addreader, btn_reset;
	@FXML
	private TextField txtSTT, txtMaDocGia, txtTenDocGia, txtDiaChi, txt_find;
	@FXML
	private TableView<DocGia> tabledocgia;
	@FXML
	private TableColumn<DocGia, Integer> sttdocgia;
	@FXML
	private TableColumn<DocGia, String> madocgia;
	@FXML
	private TableColumn<DocGia, String> tendocgia;
	@FXML
	private TableColumn<DocGia, String> diachi;
	Connection connection = null;
	ResultSet rs = null;
	PreparedStatement pst = null;

	public void UpdateTable() throws SQLException {
		sttdocgia.setCellValueFactory(new PropertyValueFactory<DocGia, Integer>("stt"));
		madocgia.setCellValueFactory(new PropertyValueFactory<DocGia, String>("madocgia"));
		tendocgia.setCellValueFactory(new PropertyValueFactory<DocGia, String>("tendocgia"));
		diachi.setCellValueFactory(new PropertyValueFactory<DocGia, String>("diachi"));
		ObservableList<DocGia> list = Update();
		tabledocgia.setItems(list);
	}

	public void Add() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		Statement stat = connection.createStatement();
		if (txtSTT.getText().trim().equals("") || txtMaDocGia.getText().trim().equals("")
				|| txtTenDocGia.getText().trim().equals("") || txtDiaChi.getText().trim().equals("")) {
			Notice("Nhập thiếu thông tin vui lòng kiểm tra lại !");
		} else {
			try {
				String sql = "Insert into [Độc Giả](STT, [Mã Độc Giả], [Tên Độc Giả], [Địa Chỉ]) values ("
						+ Integer.parseInt(txtSTT.getText()) + ", N'" + txtMaDocGia.getText() + "', N'"
						+ txtTenDocGia.getText() + "', N'" + txtDiaChi.getText() + "')";
				stat.executeUpdate(sql);
			} catch (Exception e) {
				Notice("Nhập dữ liệu không hợp lệ !");
				return;
			}
			Notice("Thêm Thông tin thành công !");
			UpdateTable();
			search();
		}
	}

	public ObservableList<DocGia> Update() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		ObservableList<DocGia> list = FXCollections.observableArrayList();
		try {
			PreparedStatement ps = connection.prepareStatement("Select * from [Độc Giả]");
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new DocGia(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
		} catch (Exception e) {
		}
		return list;
	}

	int index = -1;

	@FXML
	void getSelected(MouseEvent event) {
		index = tabledocgia.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}
		txtSTT.setText(sttdocgia.getCellData(index).toString());
		txtMaDocGia.setText(madocgia.getCellData(index).toString());
		txtTenDocGia.setText(tendocgia.getCellData(index).toString());
		txtDiaChi.setText(diachi.getCellData(index).toString());
	}

	public void Edit() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		String value1 = txtSTT.getText();
		String value2 = txtMaDocGia.getText();
		String value3 = txtTenDocGia.getText();
		String value4 = txtDiaChi.getText();
		String sql = "update [Độc Giả] set STT= " + Integer.parseInt(value1) + ", [Mã Độc Giả]= N'" + value2
				+ "', [Tên Độc Giả]= N'" + value3 + "',[Địa Chỉ]= N'" + value4 + "' where STT = "
				+ Integer.parseInt(value1) + ";";
		pst = connection.prepareStatement(sql);
		pst.execute();
		Notice("Cập nhật thông tin thành công !");
		UpdateTable();
		search();
	}

	public void Delete() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		String sql = "delete from [Độc Giả] where STT = ?";
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, txtSTT.getText());
			pst.execute();
			Notice("Xóa Thành công !");
			UpdateTable();
			search();
		} catch (Exception e) {
			Notice("Hệ Thống đang gặp lỗi, không thể xóa !");
		}

	}

	public void Notice(String s) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Thông Báo");
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.showAndWait();
	}

	public void search() throws SQLException {
		sttdocgia.setCellValueFactory(new PropertyValueFactory<DocGia, Integer>("stt"));
		madocgia.setCellValueFactory(new PropertyValueFactory<DocGia, String>("madocgia"));
		tendocgia.setCellValueFactory(new PropertyValueFactory<DocGia, String>("tendocgia"));
		diachi.setCellValueFactory(new PropertyValueFactory<DocGia, String>("diachi"));
		ObservableList<DocGia> list = Update();
		tabledocgia.setItems(list);
		FilteredList<DocGia> filteredData = new FilteredList<>(list, b -> true);
		txt_find.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(DocGia -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				if (String.valueOf(DocGia.getStt()).indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (DocGia.getMadocgia().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (DocGia.getTendocgia().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (DocGia.getDiachi().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else
					return false;
			});
		});
		SortedList<DocGia> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tabledocgia.comparatorProperty());
		tabledocgia.setItems(sortedData);
	}

	public void Reset() throws SQLException {
		txtSTT.setText("");
		txtMaDocGia.setText("");
		txtTenDocGia.setText("");
		txtDiaChi.setText("");
		UpdateTable();
		search();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			UpdateTable();
			search();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
