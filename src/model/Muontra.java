package model;

public class Muontra {
	private String mamuontra;
	private String manhanvien;
	private String ngaymuon;
	private String ngayhentra;
	private String madocgia;
	private String masach;
	private String ngaytra;
	private String ghichu;
	private int tienphat;

	public Muontra(String mamuontra, String manhanvien, String ngaymuon, String ngayhentra, String madocgia,
			String masach, String ngaytra, String ghichu, int tienphat) {
		super();
		this.tienphat = tienphat;
		this.mamuontra = mamuontra;
		this.manhanvien = manhanvien;
		this.ngaymuon = ngaymuon;
		this.ngayhentra = ngayhentra;
		this.madocgia = madocgia;
		this.masach = masach;
		this.ngaytra = ngaytra;
		this.ghichu = ghichu;
	}

	public String getMamuontra() {
		return mamuontra;
	}

	public void setMamuontra(String mamuontra) {
		this.mamuontra = mamuontra;
	}

	public String getManhanvien() {
		return manhanvien;
	}

	public void setManhanvien(String manhanvien) {
		this.manhanvien = manhanvien;
	}

	public String getNgaymuon() {
		return ngaymuon;
	}

	public void setNgaymuon(String ngaymuon) {
		this.ngaymuon = ngaymuon;
	}

	public String getNgayhentra() {
		return ngayhentra;
	}

	public void setNgayhentra(String ngayhentra) {
		this.ngayhentra = ngayhentra;
	}

	public String getMadocgia() {
		return madocgia;
	}

	public void setMadocgia(String madocgia) {
		this.madocgia = madocgia;
	}

	public String getMasach() {
		return masach;
	}

	public void setMasach(String masach) {
		this.masach = masach;
	}

	public String getNgaytra() {
		return ngaytra;
	}

	public void setNgaytra(String ngaytra) {
		this.ngaytra = ngaytra;
	}

	public String getGhichu() {
		return ghichu;
	}

	public void setGhichu(String ghichu) {
		this.ghichu = ghichu;
	}

	public int getTienphat() {
		return tienphat;
	}

	public void setTienphat(int tienphat) {
		this.tienphat = tienphat;
	}

}