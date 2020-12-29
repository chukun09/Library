package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import model.ConnectionDatabase;
import model.Muontra;
import model.Sach;

public class ControllChart extends Application implements Initializable {
	@FXML
	private BarChart<?, ?> SachChart;
	@FXML
	private CategoryAxis axisc;
	@FXML
	private NumberAxis axisn;
	@FXML
	private ChoiceBox<String> choiceboxtable, choicebox1, choicebox2;
	ObservableList<Sach> list;
	ObservableList<Muontra> list1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadData();
		choiceboxtable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				check1();
			}
		});
	}

	public void Submit() throws SQLException {
		SachChart.getData().clear();
		if (choicebox1.getValue() != null && choicebox2.getValue() != null) {
			axisc.setLabel(choicebox1.getValue());
			axisn.setLabel(choicebox2.getValue());
			if ((choicebox1.getValue().equals("Tên Sách"))) {
				Sachsl();
			}
			if ((choicebox1.getValue().equals("Mã Sách"))) {
				MaSachsl();
			}
			if ((choicebox1.getValue().equals("Tác Giả"))) {
				TacGiasl();
			}
			if ((choicebox1.getValue().equals("Nhà Xuất Bản"))) {
				NXBsl();
			}
			if ((choicebox1.getValue().equals("Năm Xuất Bản"))) {
				NamXBsl();
			}
			if (choicebox1.getValue().equals("Mã Mượn Trả") && choicebox2.getValue().equals("Tiền Phạt")) {
				Mamttienphat();
			}
			if (choicebox1.getValue().equals("Mã Độc Giả") && choicebox2.getValue().equals("Tiền Phạt")) {
				Madocgiatienphat();
			}
			if (choicebox1.getValue().equals("Mã Nhân Viên") && choicebox2.getValue().equals("Tiền Phạt")) {
				Manhanvientienphat();
			}
			if (choicebox1.getValue().equals("Mã Độc Giả") && choicebox2.getValue().equals("Số Lượt Mượn")) {
				MaDocGiaSoLuong();
			}
			if (choicebox1.getValue().equals("Mã Nhân Viên") && choicebox2.getValue().equals("Số Lượt Mượn")) {
				MaNhanVienSoLuong();
			}
		}
	}

	private void loadData() {
		String[] s = { "Bảng Sách", "Bảng Mượn Trả" };
		choiceboxtable.getItems().addAll(s);
	}

	private void check1() {
		String[] s1 = { "Tên Sách", "Mã Sách", "Nhà Xuất Bản", "Tác Giả", "Năm Xuất Bản" };
		String[] s2 = { "Mã Mượn Trả", "Mã Độc Giả", "Mã Nhân Viên" };
		String[] s3 = { "Tiền Phạt", "Số Lượt Mượn" };
		if (choiceboxtable.getValue().equals("Bảng Sách")) {
			choicebox2.getItems().clear();
			choicebox2.getItems().add("Số Lượng");
			choicebox2.setValue("Số Lượng");
			choicebox1.getItems().clear();
			choicebox1.getItems().addAll(s1);
		} else if (choiceboxtable.getValue().equals("Bảng Mượn Trả")) {
			choicebox1.getItems().clear();
			choicebox2.getItems().clear();
			choicebox1.getItems().addAll(s2);
			choicebox2.getItems().addAll(s3);
		}
	}

	public void Mamttienphat() throws SQLException {
		MuonTraController a = new MuonTraController();
		a.Update();
		list1 = a.Update();
		XYChart.Series set1 = new XYChart.Series<>();
		for (Muontra i : list1) {
			set1.getData().add(new XYChart.Data(i.getMamuontra(), i.getTienphat()));
		}
		SachChart.getData().addAll(set1);
	}

	public void Manhanvientienphat() throws SQLException {
		MuonTraController a = new MuonTraController();
		a.Update();
		list1 = a.Update();
		XYChart.Series set1 = new XYChart.Series<>();
		for (Muontra i : list1) {
			set1.getData().add(new XYChart.Data(i.getManhanvien(), TienPhatnv(i.getManhanvien())));
		}
		SachChart.getData().addAll(set1);
	}

	public int TienPhatnv(String s) throws SQLException {
		int sum = 0;
		MuonTraController a = new MuonTraController();
		ObservableList<Muontra> list = a.Update();
		for (Muontra i : list) {
			if (i.getManhanvien().equals(s)) {
				sum += i.getTienphat();
			}
		}
		return sum;
	}

	public void MaDocGiaSoLuong() throws SQLException {
		MuonTraController a = new MuonTraController();
		a.Update();
		list1 = a.Update();
		XYChart.Series set1 = new XYChart.Series<>();
		for (Muontra i : list1) {
			set1.getData().add(new XYChart.Data(i.getMadocgia(), CountDocGia(i.getMadocgia())));
		}
		SachChart.getData().addAll(set1);
	}

	public void MaNhanVienSoLuong() throws SQLException {
		MuonTraController a = new MuonTraController();
		a.Update();
		list1 = a.Update();
		XYChart.Series set1 = new XYChart.Series<>();
		for (Muontra i : list1) {
			set1.getData().add(new XYChart.Data(i.getManhanvien(), CountNhanVien(i.getManhanvien())));
		}
		SachChart.getData().addAll(set1);
	}

	public void Madocgiatienphat() throws SQLException {
		MuonTraController a = new MuonTraController();
		a.Update();
		list1 = a.Update();
		XYChart.Series set1 = new XYChart.Series<>();
		for (Muontra i : list1) {
			set1.getData().add(new XYChart.Data(i.getMadocgia(), TienPhat(i.getMadocgia())));
		}
		SachChart.getData().addAll(set1);
	}

	public void Sachsl() throws SQLException {
		TableViewSach a = new TableViewSach();
		a.Update();
		list = a.Update();
		XYChart.Series set1 = new XYChart.Series<>();
		for (Sach i : list) {
			set1.getData().add(new XYChart.Data(i.getTensach(), i.getSl()));
		}
		SachChart.getData().addAll(set1);
	}

	public void MaSachsl() throws SQLException {
		TableViewSach a = new TableViewSach();
		a.Update();
		list = a.Update();
		XYChart.Series set1 = new XYChart.Series<>();
		for (Sach i : list) {
			set1.getData().add(new XYChart.Data(i.getMasach(), i.getSl()));
		}
		SachChart.getData().addAll(set1);
	}

	public void TacGiasl() throws SQLException {
		TableViewSach a = new TableViewSach();
		a.Update();
		list = a.Update();
		XYChart.Series set1 = new XYChart.Series<>();
		for (Sach i : list) {
			set1.getData().add(new XYChart.Data(i.getTacgia(), CountTacGia(i.getTacgia())));
		}
		SachChart.getData().addAll(set1);
	}

	public void NXBsl() throws SQLException {
		TableViewSach a = new TableViewSach();
		a.Update();
		list = a.Update();
		XYChart.Series set1 = new XYChart.Series<>();
		for (Sach i : list) {
			set1.getData().add(new XYChart.Data(i.getManhaxuatban(), CoutNXB(i.getManhaxuatban())));
		}
		SachChart.getData().addAll(set1);
	}

	public void NamXBsl() throws SQLException {
		TableViewSach a = new TableViewSach();
		a.Update();
		list = a.Update();
		XYChart.Series set1 = new XYChart.Series<>();
		for (Sach i : list) {
			set1.getData().add(new XYChart.Data(String.valueOf(i.getNamxuatban()), CountNamXB(i.getNamxuatban())));
		}
		SachChart.getData().addAll(set1);
	}

	public int CountNamXB(int s) throws SQLException {
		Connection connection = ConnectionDatabase.ConnectionData("Library");
		Statement stat = connection.createStatement();
		ResultSet rs = stat.executeQuery("Select count(*) AS total from Sách where [Năm Xuất Bản] = " + s);
		rs.next();
		return rs.getInt("total");
	}

	public int TienPhat(String s) throws SQLException {
		int sum = 0;
		MuonTraController a = new MuonTraController();
		ObservableList<Muontra> list = a.Update();
		for (Muontra i : list) {
			if (i.getMadocgia().equals(s)) {
				sum += i.getTienphat();
			}
		}
		return sum;
	}

	public int CountDocGia(String s) throws SQLException {
		Connection connection = ConnectionDatabase.ConnectionData("Library");
		Statement stat = connection.createStatement();
		ResultSet rs = stat.executeQuery("Select count(*) AS total from [Mượn trả] where [Mã Độc Giả] = N'" + s + "'");
		rs.next();
		return rs.getInt("total");
	}

	public int CountNhanVien(String s) throws SQLException {
		Connection connection = ConnectionDatabase.ConnectionData("Library");
		Statement stat = connection.createStatement();
		ResultSet rs = stat
				.executeQuery("Select count(*) AS total from [Mượn trả] where [Mã Nhân Viên] = N'" + s + "'");
		rs.next();
		return rs.getInt("total");
	}

	public int CountTacGia(String s) throws SQLException {
		Connection connection = ConnectionDatabase.ConnectionData("Library");
		Statement stat = connection.createStatement();
		ResultSet rs = stat.executeQuery("Select count(*) AS total from Sách where [Tác Giả] = N'" + s + "'");
		rs.next();
		return rs.getInt("total");
	}

	public int CoutNXB(String s) throws SQLException {
		Connection connection = ConnectionDatabase.ConnectionData("Library");
		Statement stat = connection.createStatement();
		ResultSet rs = stat.executeQuery("Select count(*) AS total from Sách where [Mã NXB] = '" + s + "'");
		rs.next();
		return rs.getInt("total");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/View/Chart.fxml"));

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
