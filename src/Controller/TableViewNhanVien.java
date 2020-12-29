package Controller;

import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import model.ConnectionDatabase;
import model.NhanVien;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class TableViewNhanVien implements Initializable {
	@FXML
	private Button btn_change, btn_delete, btn_add, btn_reset;
	@FXML
	private TextField txtSTT, txtMaNhanVien, txtTenNhanVien, txtNgaySinh, txtSoDienThoai, txt_find;
	@FXML
	private TableView<NhanVien> tablenhanvien;
	@FXML
	private TableColumn<NhanVien, Integer> sttnhanvien;
	@FXML
	private TableColumn<NhanVien, String> manhanvien;
	@FXML
	private TableColumn<NhanVien, String> tennhanvien;
	@FXML
	private TableColumn<NhanVien, String> ngaysinh;
	@FXML
	private TableColumn<NhanVien, String> sodienthoai;
	Connection connection = null;
	ResultSet rs = null;
	PreparedStatement pst = null;

	public void UpdateTable() throws SQLException {
		sttnhanvien.setCellValueFactory(new PropertyValueFactory<NhanVien, Integer>("stt"));
		manhanvien.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("manhanvien"));
		tennhanvien.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("hoten"));
		ngaysinh.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("ngaysinh"));
		sodienthoai.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("sdt"));
		ObservableList<NhanVien> list = Update();
		tablenhanvien.setItems(list);
	}

	public void Add() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		Statement stat = connection.createStatement();
		if (txtSTT.getText().trim().equals("") || txtMaNhanVien.getText().trim().equals("")
				|| txtTenNhanVien.getText().trim().equals("") || txtNgaySinh.getText().trim().equals("")
				|| txtSoDienThoai.getText().trim().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Thông Báo");
			alert.setHeaderText(null);
			alert.setContentText("Nhập thiếu thông tin vui lòng kiểm tra lại !");
			alert.showAndWait();
		} else {
			try {
				String sql = "Insert into [Nhân Viên](STT, [Mã Nhân Viên], [Họ và Tên], [Ngày Sinh], [SĐT]) values ("
						+ Integer.parseInt(txtSTT.getText()) + ", N'" + txtMaNhanVien.getText() + "', N'"
						+ txtTenNhanVien.getText() + "', N'" + txtNgaySinh.getText() + "', N'"
						+ txtSoDienThoai.getText() + "')";
				stat.executeUpdate(sql);
			} catch (Exception e) {
				Notice("Thêm Không Thành Công ! Vui Lòng Kiểm Tra Lại :(");
				return;
			}
			Notice("Thêm Thành Công <3 !");
			UpdateTable();
			search();
		}
	}

	public void Notice(String s) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Thông Báo");
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.showAndWait();
	}

	public ObservableList<NhanVien> Update() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		ObservableList<NhanVien> list = FXCollections.observableArrayList();
		try {
			PreparedStatement ps = connection.prepareStatement("Select * from [Nhân Viên]");
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(
						new NhanVien(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
			}
		} catch (Exception e) {
		}
		return list;
	}

	int index = -1;

	@FXML
	void getSelected(MouseEvent event) {
		index = tablenhanvien.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}
		txtSTT.setText(sttnhanvien.getCellData(index).toString());
		txtMaNhanVien.setText(manhanvien.getCellData(index).toString());
		txtTenNhanVien.setText(tennhanvien.getCellData(index).toString());
		txtNgaySinh.setText(ngaysinh.getCellData(index).toString());
		txtSoDienThoai.setText(sodienthoai.getCellData(index).toString());
	}

	public void Edit() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		String value1 = txtSTT.getText();
		String value2 = txtMaNhanVien.getText();
		String value3 = txtTenNhanVien.getText();
		String value4 = txtNgaySinh.getText();
		String value5 = txtSoDienThoai.getText();
		try {
			String sql = "update [Nhân Viên] set STT= " + Integer.parseInt(value1) + ", [Mã Nhân Viên]= N'" + value2
					+ "', [Họ và Tên]= N'" + value3 + "',[Ngày Sinh]= N'" + value4 + "', [SĐT]= N'" + value5
					+ "' where STT = " + Integer.parseInt(value1) + ";";
			pst = connection.prepareStatement(sql);
			pst.execute();
		} catch (Exception e) {
			Notice("Sửa dữ liệu không hợp lệ !");
			return;
		}
		Notice("Update Completed !");
		UpdateTable();
		search();
	}

	public void Delete() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		String sql = "delete from [Nhân Viên] where STT = ?";
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, txtSTT.getText());
			pst.execute();
			Notice("Delete Completed!");
			UpdateTable();
			search();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}

	}

	public void search() throws SQLException {
		sttnhanvien.setCellValueFactory(new PropertyValueFactory<NhanVien, Integer>("stt"));
		manhanvien.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("manhanvien"));
		tennhanvien.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("hoten"));
		ngaysinh.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("ngaysinh"));
		sodienthoai.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("sdt"));
		ObservableList<NhanVien> list = Update();
		tablenhanvien.setItems(list);
		FilteredList<NhanVien> filteredData = new FilteredList<>(list, b -> true);
		txt_find.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(NhanVien -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				if (String.valueOf(NhanVien.getStt()).indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (NhanVien.getManhanvien().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (NhanVien.getHoten().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (NhanVien.getNgaysinh().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if (NhanVien.getSdt().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else
					return false;
			});
		});
		SortedList<NhanVien> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tablenhanvien.comparatorProperty());
		tablenhanvien.setItems(sortedData);
	}

	public void Output() throws SQLException, IOException {
		try {
			ObservableList<NhanVien> list = Update();
			FileWriter fw = new FileWriter("C:\\Users\\DELL\\eclipse-workspace\\Library\\lib\\Outputnhanvien");
			for (NhanVien i : list) {
				fw.write("STT :" + i.getStt() + ", Mã Nhân Viên :" + i.getManhanvien().trim() + ", Tên Nhân Viên : "
						+ i.getHoten() + ", Ngày Sinh :" + i.getNgaysinh() + ", Số ĐT :" + i.getSdt());
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

	public void Reset() {
		txtSTT.setText("");
		txtMaNhanVien.setText("");
		txtTenNhanVien.setText("");
		txtNgaySinh.setText("");
		txtSoDienThoai.setText("");
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
