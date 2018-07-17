package com.hmi.kiddos.model;

public class SalaryInfo {
	private double basic;
	private double netPay;
	private double grossPay;
	private double pfDeduction;
	private String emailId;
	private String firstName;
	private String lastName;
	public double getBasic() {
		return basic;
	}
	public void setBasic(double basic) {
		this.basic = basic;
	}
	public double getNetPay() {
		return netPay;
	}
	public void setNetPay(double netPay) {
		this.netPay = netPay;
	}
	public double getGrossPay() {
		return grossPay;
	}
	public void setGrossPay(double grossPay) {
		this.grossPay = grossPay;
	}
	public double getPfDeduction() {
		return pfDeduction;
	}
	public void setPfDeduction(double pfDeduction) {
		this.pfDeduction = pfDeduction;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	@Override
	public String toString() {
		return "SalaryInfo [basic=" + basic + ", netPay=" + netPay + ", grossPay=" + grossPay + ", pfDeduction="
				+ pfDeduction + ", emailId=" + emailId + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
	
	
}
