package model;

public class Sach {
	private int stt;
	private String masach;
	private String tensach;
	private String tacgia;
	private String manhaxuatban;
	private int namxuatban;
	private int sl;
	
	public Sach(int stt, String masach, String tensach, String tacgia, String manhaxuatban, int namxuatban, int sl) {
		super();
		this.stt = stt;
		this.masach = masach;
		this.tensach = tensach;
		this.tacgia = tacgia;
		this.manhaxuatban = manhaxuatban;
		this.namxuatban = namxuatban;
		this.sl = sl;
	}
	public int getStt() {
		return stt;
	}
	public void setStt(int stt) {
		this.stt = stt;
	}
	public String getMasach() {
		return masach;
	}
	public void setMasach(String masach) {
		this.masach = masach;
	}
	public String getTensach() {
		return tensach;
	}
	public void setTensach(String tensach) {
		this.tensach = tensach;
	}
	public String getTacgia() {
		return tacgia;
	}
	public void setTacgia(String tacgia) {
		this.tacgia = tacgia;
	}
	public String getManhaxuatban() {
		return manhaxuatban;
	}
	public void setManhaxuatban(String manhaxuatban) {
		this.manhaxuatban = manhaxuatban;
	}
	public int getNamxuatban() {
		return namxuatban;
	}
	public void setNamxuatban(int namxuatban) {
		this.namxuatban = namxuatban;
	}
	public int getSl() {
		return sl;
	}
	public void setSl(int sl) {
		this.sl = sl;
	}


}