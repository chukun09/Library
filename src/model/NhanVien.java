package model;

public class NhanVien {
	private int stt;
	private String manhanvien;
	private String hoten;
	private String ngaysinh;
	private String sdt;
	
	public NhanVien(int stt, String manhanvien, String hoten, String ngaysinh, String sdt) {
		super();
		this.stt = stt;
		this.manhanvien = manhanvien;
		this.hoten = hoten;
		this.ngaysinh = ngaysinh;
		this.sdt = sdt;
	}
	public int getStt() {
		return stt;
	}
	public void setStt(int stt) {
		this.stt = stt;
	}
	public String getManhanvien() {
		return manhanvien;
	}
	public void setManhanvien(String manhanvien) {
		this.manhanvien = manhanvien;
	}
	public String getHoten() {
		return hoten;
	}
	public void setHoten(String hoten) {
		this.hoten = hoten;
	}
	public String getNgaysinh() {
		return ngaysinh;
	}
	public void setNgaysinh(String ngaysinh) {
		this.ngaysinh = ngaysinh;
	}
	public String getSdt() {
		return sdt;
	}
	public void setSdt(String sdt) {
		this.sdt = sdt;
	}
	
}
