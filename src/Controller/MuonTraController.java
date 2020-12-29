package Controller;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import model.*;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class MuonTraController implements Initializable {
	int a;
	@FXML
	private Button btn_change, btn_delete, btn_add, btn_reset;
	@FXML
	private TextField txtmamuontra, txt_find, txtghichu;
	@FXML
	DatePicker txtngaymuon, txthentra, txtngaytra;
	@FXML
	ComboBox<String> txtdocgia, txtmanhanvien, txtmasach;
	@FXML
	private TableView<Muontra> tableMuonTra;
	@FXML
	private TableColumn<Muontra, String> MaMuonTra;
	@FXML
	private TableColumn<Muontra, String> MaNhanVien;
	@FXML
	private TableColumn<Muontra, String> NgayMuon;
	@FXML
	private TableColumn<Muontra, String> NgayHenTra;
	@FXML
	private TableColumn<Muontra, String> MaDocGia;
	@FXML
	private TableColumn<Muontra, String> MaSach;
	@FXML
	private TableColumn<Muontra, String> NgayTra;
	@FXML
	private TableColumn<Muontra, String> GhiChu;
	@FXML
	private TableColumn<Muontra, Integer> TienPhat;
	Connection connection = null;
	ResultSet rs = null;
	PreparedStatement pst = null;

	public void UpdateTable() throws SQLException {
		MaMuonTra.setCellValueFactory(new PropertyValueFactory<Muontra, String>("mamuontra"));
		MaNhanVien.setCellValueFactory(new PropertyValueFactory<Muontra, String>("manhanvien"));
		NgayMuon.setCellValueFactory(new PropertyValueFactory<Muontra, String>("ngaymuon"));
		NgayHenTra.setCellValueFactory(new PropertyValueFactory<Muontra, String>("ngayhentra"));
		MaDocGia.setCellValueFactory(new PropertyValueFactory<Muontra, String>("madocgia"));
		MaSach.setCellValueFactory(new PropertyValueFactory<Muontra, String>("masach"));
		NgayTra.setCellValueFactory(new PropertyValueFactory<Muontra, String>("ngaytra"));
		GhiChu.setCellValueFactory(new PropertyValueFactory<Muontra, String>("ghichu"));
		TienPhat.setCellValueFactory(new PropertyValueFactory<Muontra, Integer>("tienphat"));
		ObservableList<Muontra> list = Update();
		tableMuonTra.setItems(list);
	}

	public void loaddata() throws SQLException {
		TableViewSach a = new TableViewSach();
		TableViewDocGia b = new TableViewDocGia();
		TableViewNhanVien c = new TableViewNhanVien();
		ObservableList<Sach> list = a.Update();
		ObservableList<DocGia> list1 = b.Update();
		ObservableList<NhanVien> list2 = c.Update();
		for (Sach i : list) {
			txtmasach.getItems().add(i.getMasach());
		}
		for (DocGia i : list1) {
			txtdocgia.getItems().add(i.getMadocgia());
		}
		for (NhanVien i : list2) {
			txtmanhanvien.getItems().add(i.getManhanvien());
		}
	}

	public void search(ComboBox<String> s) {
		ObservableList<String> list = s.getItems();
		FilteredList<String> filteredList = new FilteredList<>(list);
		s.getEditor().textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(item -> {
			s.show();
			if (newValue == null || newValue.isEmpty()) {
				return true;
			}
			if (item.toLowerCase().contains(newValue.toLowerCase().trim())) {
				return true;
			}
			return false;
		}));
		s.setItems(filteredList);
	}

	public void searchprimary() {
		search(txtdocgia);
		search(txtmasach);
		search(txtmanhanvien);
	}

	public void Add() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		Statement stat = connection.createStatement();
		PreparedStatement ps1 = connection.prepareStatement("Select * from Sách");
		ResultSet rs1 = ps1.executeQuery();
		while (rs1.next()) {
			if (rs1.getString(2).equals(txtmasach.getValue()) && rs1.getInt(7) == 0) {
				Notice("Sách Hiện Không Còn! Vui Lòng Chọn Sách Khác !");
				return;
			}
		}
		if (txtmamuontra.getText().trim().equals("") || txtmanhanvien.getValue().trim().equals("")
				|| txtngaymuon.getValue().toString().trim().equals("")
				|| txthentra.getValue().toString().trim().equals("") || txtmasach.getValue().trim().equals("")
				|| txtdocgia.getValue().trim().equals("") || txtghichu.getText().trim().equals("")) {
			Notice("Nhập thiếu thông tin vui lòng kiểm tra lại !");
		} else {
			try {
				String sql = "Insert into [Mượn Trả]([Mã Mượn Trả], [Mã Nhân Viên], [Ngày Mượn], [Ngày Hẹn Trả], [Mã Độc Giả]) values ('"
						+ txtmamuontra.getText() + "', N'" + txtmanhanvien.getValue() + "', '"
						+ txtngaymuon.getValue().toString() + "', '" + txthentra.getValue().toString() + "', '"
						+ txtdocgia.getValue() + "')";
				String sql2 = "Insert into [CT Mượn_Trả] values('" + txtmamuontra.getText() + "', '"
						+ txtmasach.getValue() + "', '" + txtngaytra.getValue().toString() + "'," + 1 + ", N'"
						+ txtghichu.getText() + "')";
				stat.executeUpdate(sql);
				stat.executeUpdate(sql2);
			} catch (Exception e) {
				Notice("Thêm Thông Tin Thất Bại ! :(( Vui Lòng Kiểm Tra Lại !");
				return;
			}
			PreparedStatement ps = connection.prepareStatement("Select * from Sách");
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString(2).equals(txtmasach.getValue())) {
					a = rs.getInt(7) - 1;
					break;
				}
			}
			String sql3 = "Update Sách set [Số Lượng] = " + a + " where [Mã Sách] = '" + txtmasach.getValue() + "'";
			connection.prepareStatement(sql3).execute();
			UpdateTable();
			search_book();
			Notice("Add Information Completed !");
		}
	}

	public ObservableList<Muontra> Update() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		ObservableList<Muontra> list = FXCollections.observableArrayList();
		try {
			PreparedStatement ps = connection.prepareStatement(
					"Select  * From [Mượn trả], [CT Mượn_Trả] where [Mượn trả].[Mã mượn trả] = [CT Mượn_Trả].[Mã Mượn Trả]");
			rs = ps.executeQuery();
			while (rs.next()) {
				int b;
				if (rs.getString(4).equals("") || rs.getString(8).equals("")) {
					b = 0;
				} else {
					b = ((Integer.parseInt(rs.getString(4).substring(5, 7))
							- Integer.parseInt(rs.getString(8).substring(5, 7))) * (-150000))
							- ((Integer.parseInt(rs.getString(4).substring(8, 10))
									- Integer.parseInt(rs.getString(8).substring(8, 10))) * 5000);
				}
				if (b < 0) {
					b = 0;
				}
				list.add(new Muontra(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(7), rs.getString(8), rs.getString(10), b));
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return list;
	}

	int index = -1;

	@FXML
	void getSelected(MouseEvent event) {
		index = tableMuonTra.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}
		txtmamuontra.setText(MaMuonTra.getCellData(index).toString());
		txtmanhanvien.setValue(MaNhanVien.getCellData(index).toString());
		txtngaymuon.setValue(LocalDate.parse(NgayMuon.getCellData(index).toString()));
		txthentra.setValue(LocalDate.parse(NgayHenTra.getCellData(index).toString()));
		txtghichu.setText(GhiChu.getCellData(index).toString());
		txtdocgia.setValue(MaDocGia.getCellData(index).toString());
		txtngaytra.setValue(LocalDate.parse(NgayTra.getCellData(index).toString()));
		txtmasach.setValue(MaSach.getCellData(index).toString());
	}

	public void Edit() throws SQLException {
		connection = ConnectionDatabase.ConnectionData("Library");
		String value1 = txtmamuontra.getText();
		String value2 = txtmanhanvien.getValue();
		String value3 = txtngaymuon.getValue().toString();
		String value4 = txthentra.getValue().toString();
		String value5 = txtmasach.getValue();
		String value6 = txtdocgia.getValue();
		String value7 = txtngaytra.getValue().toString();
		String value8 = txtghichu.getText();
		String sql = "update [Mượn Trả]  set [Mã Mượn Trả]= N'" + value1 + "', [Mã Nhân Viên]= N'" + value2
				+ "', [Ngày Mượn]= '" + value3 + "',[Ngày Hẹn Trả]= '" + value4 + "', [Mã Độc Giả]= N'" + value6
				+ "' where [Mã Mượn Trả] = " + "'" + value1 + "';";
		pst = connection.prepareStatement(sql);
		pst.execute();
		String sql2 = "update [CT Mượn_Trả]  set [Mã Mượn Trả]= N'" + value1 + "', [Mã Sách]= N'" + value5
				+ "', [Ngày Trả]= N'" + value7 + "', [Ghi Chú]= N'" + value8 + "' where [Mã Mượn Trả] = " + "'" + value1
				+ "';";
		connection.prepareStatement(sql2).execute();
		Notice("Update Completed!");
		UpdateTable();
		search_book();
	}

	public void Notice(String s) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Thông Báo");
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.showAndWait();
	}

	public void Delete() throws SQLException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Information");
		alert.setHeaderText("Are you sure want to move this Infor to the Recycle Bin?");
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == null) {
		} else if (option.get() == ButtonType.OK) {
			connection = ConnectionDatabase.ConnectionData("Library");
			String sql = "delete from [Mượn Trả] where [Mã Mượn Trả] = ?";
			try {
				pst = connection.prepareStatement(sql);
				pst.setString(1, txtmamuontra.getText());
				pst.execute();
				PreparedStatement ps = connection.prepareStatement("Select * from Sách");
				rs = ps.executeQuery();
				while (rs.next()) {
					if (rs.getString(2).equals(txtmasach.getValue())) {
						a = rs.getInt(7) + 1;
						break;
					}
				}
				String sql3 = "Update Sách set [Số Lượng] = " + a + " where [Mã Sách] = '" + txtmasach.getValue() + "'";
				connection.prepareStatement(sql3).execute();
				Notice("Delete Completed!");
				UpdateTable();
				search_book();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}

		} else if (option.get() == ButtonType.CANCEL) {
			return;
		}
	}

	public void search_book() throws SQLException {
		MaMuonTra.setCellValueFactory(new PropertyValueFactory<Muontra, String>("mamuontra"));
		MaNhanVien.setCellValueFactory(new PropertyValueFactory<Muontra, String>("manhanvien"));
		NgayMuon.setCellValueFactory(new PropertyValueFactory<Muontra, String>("ngaymuon"));
		NgayHenTra.setCellValueFactory(new PropertyValueFactory<Muontra, String>("ngayhentra"));
		MaDocGia.setCellValueFactory(new PropertyValueFactory<Muontra, String>("madocgia"));
		MaSach.setCellValueFactory(new PropertyValueFactory<Muontra, String>("masach"));
		NgayTra.setCellValueFactory(new PropertyValueFactory<Muontra, String>("ngaytra"));
		GhiChu.setCellValueFactory(new PropertyValueFactory<Muontra, String>("ghichu"));
		ObservableList<Muontra> list = Update();
		tableMuonTra.setItems(list);
		FilteredList<Muontra> filteredData = new FilteredList<>(list, b -> true);
		txt_find.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(Muontra -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				if (Muontra.getMamuontra().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (Muontra.getManhanvien().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (Muontra.getNgayhentra().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (Muontra.getGhichu().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if (Muontra.getMasach().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (Muontra.getMadocgia().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (Muontra.getNgaymuon().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (Muontra.getNgaytra().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else
					return false;
			});
		});
		SortedList<Muontra> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tableMuonTra.comparatorProperty());
		tableMuonTra.setItems(sortedData);
	}

	public void Output() throws SQLException, IOException {
		try {
			ObservableList<Muontra> list = Update();
			FileWriter fw = new FileWriter("C:\\Users\\DELL\\eclipse-workspace\\Library\\lib\\Outputmuontra");
			for (Muontra i : list) {
				fw.write("Mã Mượn Trả :" + i.getMamuontra().trim() + ", Mã nhân viên :" + i.getManhanvien().trim()
						+ ", Ngày Mượn :" + i.getNgaymuon() + ", Ngày hẹn trả :" + i.getNgayhentra() + ", Ngày Trả :"
						+ i.getNgaytra() + ", Mã độc giả :" + i.getMadocgia().trim() + ", Mã Sách "
						+ i.getMasach().trim() + ", Tiền phạt :" + i.getTienphat());
				fw.write("\n");
			}
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		Notice("In file thành công !");
	}

	public void Reset() throws SQLException {
		txtmamuontra.setText("");
		txtmanhanvien.setValue("");
		txtngaymuon.setValue(null);
		txthentra.setValue(null);
		txtmasach.setValue("");
		txtngaytra.setValue(null);
		txtdocgia.setValue("");
		txtghichu.setText("");
		UpdateTable();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			loaddata();
			UpdateTable();
			search_book();
			searchprimary();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
