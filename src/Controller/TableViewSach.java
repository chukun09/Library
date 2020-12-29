package Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import model.ConnectionDatabase;
import model.Sach;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class TableViewSach implements Initializable {
	ObservableList<String> MS = FXCollections.observableArrayList();
	ObservableList<String> TS = FXCollections.observableArrayList();
	ObservableList<String> SL = FXCollections.observableArrayList();
	ObservableList<String> TG = FXCollections.observableArrayList();
	ObservableList<String> NXB = FXCollections.observableArrayList();
	@FXML
	private Button btn_changebook, btn_deletebook, btn_addbook, btn_reset, btnchoosefile, btnoutput;
	@FXML
	private TextField txtSTT, txtMaSach, txtTenSach, txtTacGia, txtNamXB, txtNXB, txt_find, txtSoLuong;
	@FXML
	private TableView<Sach> tableSach;
	@FXML
	private TableColumn<Sach, Integer> sttSach;
	@FXML
	private TableColumn<Sach, String> masach;
	@FXML
	private TableColumn<Sach, String> tensach;
	@FXML
	private TableColumn<Sach, String> tacgia;
	@FXML
	private TableColumn<Sach, String> manxb;
	@FXML
	private TableColumn<Sach, Integer> namxb;
	@FXML
	private TableColumn<Sach, Integer> soluong;
	MuonTraController var = new MuonTraController();
	Connection connection = null;
	ResultSet rs = null;
	PreparedStatement pst = null;

	public void filechoose() throws IOException, SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		Statement stat = connection.createStatement();
		FileChooser fileChooser = new FileChooser();
		File selectFile = fileChooser.showOpenDialog(null);
		if (selectFile != null) {
			File file = new File(selectFile.getAbsolutePath());
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				String[] line1 = line.split(",");
				String sql = "Insert into Sách(STT, [Mã Sách], [Tên Sách], [Tác Giả], [Mã NXB], [Năm Xuất Bản], [Số Lượng]) values ("
						+ line1[0] + ", N'" + line1[1] + "', N'" + line1[2] + "', N'" + line1[3] + "', N'" + line1[4]
						+ "', " + line1[5] + ", " + Integer.parseInt(line1[6]) + ")";
				stat.executeUpdate(sql);
				line = reader.readLine();
			}
			UpdateTable();
			var.Notice("Add file Successful !");
		} else {
			var.Notice("File is not vaild !");
		}
	}

	public void UpdateTable() throws SQLException {
		sttSach.setCellValueFactory(new PropertyValueFactory<Sach, Integer>("stt"));
		masach.setCellValueFactory(new PropertyValueFactory<Sach, String>("masach"));
		tensach.setCellValueFactory(new PropertyValueFactory<Sach, String>("tensach"));
		tacgia.setCellValueFactory(new PropertyValueFactory<Sach, String>("tacgia"));
		manxb.setCellValueFactory(new PropertyValueFactory<Sach, String>("manhaxuatban"));
		namxb.setCellValueFactory(new PropertyValueFactory<Sach, Integer>("namxuatban"));
		soluong.setCellValueFactory(new PropertyValueFactory<Sach, Integer>("sl"));
		ObservableList<Sach> list = Update();
		tableSach.setItems(list);
	}

	public void AddBook() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		Statement stat = connection.createStatement();
		if (txtSTT.getText().trim().equals("") || txtMaSach.getText().trim().equals("")
				|| txtTenSach.getText().trim().equals("") || txtTacGia.getText().trim().equals("")
				|| txtNXB.getText().trim().equals("") || txtNamXB.getText().trim().equals("")
				|| txtSoLuong.getText().trim().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Thông Báo");
			alert.setHeaderText(null);
			alert.setContentText("Nhập thiếu thông tin vui lòng kiểm tra lại !");
			alert.showAndWait();
		} else {
			try {
				String sql = "Insert into Sách(STT, [Mã Sách], [Tên Sách], [Tác Giả], [Mã NXB], [Năm Xuất Bản], [Số Lượng]) values ("
						+ Integer.parseInt(txtSTT.getText()) + ", N'" + txtMaSach.getText() + "', N'"
						+ txtTenSach.getText() + "', N'" + txtTacGia.getText() + "', N'" + txtNXB.getText() + "', "
						+ Integer.parseInt(txtNamXB.getText()) + ", " + Integer.parseInt(txtSoLuong.getText()) + ")";
				stat.executeUpdate(sql);
			} catch (Exception e) {
				var.Notice("Mã Sách không hợp lệ vui lòng kiểm tra lại !");
				return;
			}
			var.Notice("Thêm Sách Thành Công !");
			UpdateTable();
			search_book();
		}
	}

	public ObservableList<Sach> Update() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		ObservableList<Sach> list = FXCollections.observableArrayList();
		MS = FXCollections.observableArrayList();
		TS = FXCollections.observableArrayList();
		SL = FXCollections.observableArrayList();
		TG = FXCollections.observableArrayList();
		NXB = FXCollections.observableArrayList();
		try {
			PreparedStatement ps = connection.prepareStatement("Select * from Sách");
			rs = ps.executeQuery();
			while (rs.next()) {
				MS.add(rs.getString(2));
				TS.add(rs.getString(3));
				SL.add(rs.getString(7));
				TG.add(rs.getString(4));
				NXB.add(rs.getString(5));
				list.add(new Sach(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getInt(6), rs.getInt(7)));
			}
		} catch (Exception e) {
		}
		return list;
	}

	int index = -1;

	@FXML
	void getSelected(MouseEvent event) {
		index = tableSach.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}
		txtSTT.setText(sttSach.getCellData(index).toString());
		txtMaSach.setText(masach.getCellData(index).toString());
		txtTenSach.setText(tensach.getCellData(index).toString());
		txtTacGia.setText(tacgia.getCellData(index).toString());
		txtNXB.setText(manxb.getCellData(index).toString());
		txtNamXB.setText(namxb.getCellData(index).toString());
		txtSoLuong.setText(soluong.getCellData(index).toString());
	}

	public void Edit() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		String value1 = txtSTT.getText();
		String value2 = txtMaSach.getText();
		String value3 = txtTenSach.getText();
		String value4 = txtTacGia.getText();
		String value5 = txtNXB.getText();
		String value6 = txtNamXB.getText();
		String value7 = txtSoLuong.getText();
		try {
			String sql = "update Sách set STT= " + Integer.parseInt(value1) + ", [Mã Sách]= N'" + value2
					+ "', [Tên Sách]= N'" + value3 + "',[Tác Giả]= N'" + value4 + "', [Mã NXB]= N'" + value5
					+ "' , [Năm Xuất Bản]= " + Integer.parseInt(value6) + ", [Số Lượng]=" + Integer.parseInt(value7)
					+ " where STT = " + Integer.parseInt(value1) + ";";
			pst = connection.prepareStatement(sql);
		} catch (Exception e) {
			var.Notice(e.toString());
			return;
		}
		pst.execute();
		var.Notice("Update Completed !");
		UpdateTable();
		search_book();
	}

	public void Delete() throws SQLException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Information");
		alert.setHeaderText("Are you sure want to move this Infor to the Recycle Bin?");
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == null) {
		} else if (option.get() == ButtonType.OK) {
			connection = ConnectionDatabase.ConnectionData("Library");
			String sql = "delete from Sách where STT = ?";
			try {
				pst = connection.prepareStatement(sql);
				pst.setString(1, txtSTT.getText());
				pst.execute();
				var.Notice("Delete Completed !");
				UpdateTable();
				search_book();
			} catch (Exception e) {
				var.Notice("Xóa không thành công !");
				return;
			}
		} else if (option.get() == ButtonType.CANCEL) {
			return;
		}
	}

	public void search_book() throws SQLException {
		sttSach.setCellValueFactory(new PropertyValueFactory<Sach, Integer>("stt"));
		masach.setCellValueFactory(new PropertyValueFactory<Sach, String>("masach"));
		tensach.setCellValueFactory(new PropertyValueFactory<Sach, String>("tensach"));
		tacgia.setCellValueFactory(new PropertyValueFactory<Sach, String>("tacgia"));
		manxb.setCellValueFactory(new PropertyValueFactory<Sach, String>("manhaxuatban"));
		namxb.setCellValueFactory(new PropertyValueFactory<Sach, Integer>("namxuatban"));
		soluong.setCellValueFactory(new PropertyValueFactory<Sach, Integer>("sl"));
		ObservableList<Sach> list = Update();
		tableSach.setItems(list);
		FilteredList<Sach> filteredData = new FilteredList<>(list, b -> true);
		txt_find.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(Sach -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				if (String.valueOf(Sach.getStt()).indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (Sach.getMasach().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (Sach.getTensach().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (Sach.getTacgia().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if (Sach.getManhaxuatban().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (String.valueOf(Sach.getNamxuatban()).indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (String.valueOf(Sach.getSl()).indexOf(lowerCaseFilter) != -1) {
					return true;
				} else
					return false;
			});
		});
		SortedList<Sach> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tableSach.comparatorProperty());
		tableSach.setItems(sortedData);
	}

	public void Reset() throws SQLException {
		txtSTT.setText("");
		txtMaSach.setText("");
		txtTenSach.setText("");
		txtTacGia.setText("");
		txtNXB.setText("");
		txtSoLuong.setText("");
		txtNamXB.setText("");
		UpdateTable();
		search_book();
	}

	public void Output() throws SQLException, IOException {
		try {
			ObservableList<Sach> list = Update();
			FileWriter fw = new FileWriter("C:\\Users\\DELL\\eclipse-workspace\\Library\\lib\\OutputSach");
			for (Sach i : list) {
				fw.write("STT :" + i.getStt() + ", Mã Sách :" + i.getMasach().trim() + ", Tên Sách :"
						+ i.getTensach().trim() + ", Tác Giả :" + i.getTacgia() + ", Nhà Xuất Bản :"
						+ i.getManhaxuatban().trim() + ", Năm Xuất Bản :" + i.getNamxuatban() + ", Số Lượng :"
						+ i.getSl());
				fw.write("\n");
			}
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Thông Báo");
		alert.setHeaderText(null);
		alert.setContentText("In Thành Công Mời Kiểm Tra File!");
		alert.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			UpdateTable();
			search_book();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
