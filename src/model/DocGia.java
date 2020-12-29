package model;

public class DocGia {
	private int stt;
	private String madocgia;
	private String tendocgia;
	private String diachi;
	
	public DocGia(int stt, String madocgia, String tendocgia, String diachi) {
		super();
		this.stt = stt;
		this.madocgia = madocgia;
		this.tendocgia = tendocgia;
		this.diachi = diachi;
	}
	public int getStt() {
		return stt;
	}
	public void setStt(int stt) {
		this.stt = stt;
	}
	public String getMadocgia() {
		return madocgia;
	}
	public void setMadocgia(String madocgia) {
		this.madocgia = madocgia;
	}
	public String getTendocgia() {
		return tendocgia;
	}
	public void setTendocgia(String tendocgia) {
		this.tendocgia = tendocgia;
	}
	public String getDiachi() {
		return diachi;
	}
	public void setDiachi(String diachi) {
		this.diachi = diachi;
	}
	

}
