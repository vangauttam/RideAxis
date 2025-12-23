package com.alpha.RideAxis.DTO;

public class RegDriverVehicleDTO {
	private String licenceno;
	private String upiid;
	private String dname;
	private int age;
	private long mobileno;
	private String gender;
	private String mailid;
	private String vname;
	private String vehicleno;
	private String type;
	private String model;
	private int capacity;
	private double latitude;
	private double longitude;
	private double priceperkm;
	private double averagespeed;
	private String password;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLicenceno() {
		return licenceno;
	}
	public void setLicenceno(String licenceno) {
		this.licenceno = licenceno;
	}
	public String getUpiid() {
		return upiid;
	}
	public void setUpiid(String upiid) {
		this.upiid = upiid;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getMobileno() {
		return mobileno;
	}
	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMailid() {
		return mailid;
	}
	public void setMailid(String mailid) {
		this.mailid = mailid;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getPriceperkm() {
		return priceperkm;
	}
	public void setPriceperkm(double priceperkm) {
		this.priceperkm = priceperkm;
	}
	public double getAveragespeed() {
		return averagespeed;
	}
	public void setAveragespeed(double averagespeed) {
		this.averagespeed = averagespeed;
	}

	/**
	 * @param licenceno
	 * @param upiid
	 * @param dname
	 * @param age
	 * @param mobileno
	 * @param gender
	 * @param mailid
	 * @param vname
	 * @param vehicleno
	 * @param type
	 * @param model
	 * @param capacity
	 * @param latitude
	 * @param longitude
	 * @param priceperkm
	 * @param averagespeed
	 * @param password
	 */
	public RegDriverVehicleDTO(String licenceno, String upiid, String dname, int age, long mobileno, String gender,
			String mailid, String vname, String vehicleno, String type, String model, int capacity, double latitude,
			double longitude, double priceperkm, double averagespeed, String password) {
		super();
		this.licenceno = licenceno;
		this.upiid = upiid;
		this.dname = dname;
		this.age = age;
		this.mobileno = mobileno;
		this.gender = gender;
		this.mailid = mailid;
		this.vname = vname;
		this.vehicleno = vehicleno;
		this.type = type;
		this.model = model;
		this.capacity = capacity;
		this.latitude = latitude;
		this.longitude = longitude;
		this.priceperkm = priceperkm;
		this.averagespeed = averagespeed;
		this.password = password;
	}
	@Override
	public String toString() {
		return "RegDriverVehicleDTO [licenceno=" + licenceno + ", upiid=" + upiid + ", dname=" + dname + ", age=" + age
				+ ", mobileno=" + mobileno + ", gender=" + gender + ", mailid=" + mailid + ", vname=" + vname
				+ ", vehicleno=" + vehicleno + ", type=" + type + ", model=" + model + ", capacity=" + capacity
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", priceperkm=" + priceperkm
				+ ", averagespeed=" + averagespeed + ", password=" + password + "]";
	}
	public RegDriverVehicleDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
