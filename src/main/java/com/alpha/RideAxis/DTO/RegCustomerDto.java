package com.alpha.RideAxis.DTO;

public class RegCustomerDto {
	private String name;
	private int age;
	private String gender;
	private long mobileno;
	private String email;
	private String latitude;
	private String longitude;
	private String password;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public long getMobileno() {
		return mobileno;
	}
	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	
	public RegCustomerDto(String name, int age, String gender, long mobileno, String email, String latitude,
			String longitude, String password) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.mobileno = mobileno;
		this.email = email;
		this.latitude = latitude;
		this.longitude = longitude;
		this.password = password;
	}
	@Override
	public String toString() {
		return "RegCustomerDto [name=" + name + ", age=" + age + ", gender=" + gender + ", mobileno=" + mobileno
				+ ", email=" + email + ", latitude=" + latitude + ", longitude=" + longitude + ", password=" + password
				+ "]";
	}
	public RegCustomerDto() {
		super();
		// TODO Auto-generated constructor stub
	}

}
