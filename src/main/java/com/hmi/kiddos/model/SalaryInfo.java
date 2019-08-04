package com.hmi.kiddos.model;

public class SalaryInfo {
	private String month;
	private int year;
	private double basic;
	private double netPay;
	private double grossPay;
	private double workingDays;
	private double holidays;
	private double deductionDays;
	private double payAfterHolidays;
	private double overtime;
	private double PT;
	private double otIncentive;
	private String sendMail;
	
	private double clRemaining;
	private double clConsidered;
	
	private double pfEmployeeDeduction;
	private double pfCompanyDeduction;
	private double pfGovernmentContribution;
	private double otherLeavesConsumed;
	private double otherLeavesRemaining;
	private double salaryWithheld;
	
	private String remarks;
	
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
	public double getPfCompanyDeduction() {
		return pfCompanyDeduction;
	}
	public void setPfCompanyDeduction(double pfCompanyDeduction) {
		this.pfCompanyDeduction = pfCompanyDeduction;
	}
	public double getPfGovernmentContribution() {
		return pfGovernmentContribution;
	}
	public void setPfGovernmentContribution(double pfGovernmentContribution) {
		this.pfGovernmentContribution = pfGovernmentContribution;
	}
	public double getWorkingDays() {
		return workingDays;
	}
	public void setWorkingDays(double workingDays) {
		this.workingDays = workingDays;
	}
	public double getHolidays() {
		return holidays;
	}
	public void setHolidays(double holidays) {
		this.holidays = holidays;
	}
	public double getDeductionDays() {
		return deductionDays;
	}
	public void setDeductionDays(double deductionDays) {
		this.deductionDays = deductionDays;
	}
	public double getPayAfterHolidays() {
		return payAfterHolidays;
	}
	public void setPayAfterHolidays(double payAfterHolidays) {
		this.payAfterHolidays = payAfterHolidays;
	}
	public double getOvertime() {
		return overtime;
	}
	public void setOvertime(double overtime) {
		this.overtime = overtime;
	}
	public double getPT() {
		return PT;
	}
	public void setPT(double pT) {
		PT = pT;
	}
	public double getClRemaining() {
		return clRemaining;
	}
	public void setClRemaining(double clRemaining) {
		this.clRemaining = clRemaining;
	}
	public double getClConsidered() {
		return clConsidered;
	}
	public void setClConsidered(double clConsidered) {
		this.clConsidered = clConsidered;
	}
	public double getPfEmployeeDeduction() {
		return pfEmployeeDeduction;
	}
	public void setPfEmployeeDeduction(double pfEmployeeDeduction) {
		this.pfEmployeeDeduction = pfEmployeeDeduction;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public double getOtIncentive() {
		return otIncentive;
	}
	public void setOtIncentive(double otIncentive) {
		this.otIncentive = otIncentive;
	}
	public double getOtherLeavesConsumed() {
		return otherLeavesConsumed;
	}
	public void setOtherLeavesConsumed(double otherLeavesConsumed) {
		this.otherLeavesConsumed = otherLeavesConsumed;
	}
	public double getOtherLeavesRemaining() {
		return otherLeavesRemaining;
	}
	public void setOtherLeavesRemaining(double otherLeavesRemaining) {
		this.otherLeavesRemaining = otherLeavesRemaining;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SalaryInfo [");
		if (month != null)
			builder.append("month=").append(month).append(", ");
		if (firstName != null)
			builder.append("firstName=").append(firstName).append(", ");
		if (lastName != null)
			builder.append("lastName=").append(lastName);
		builder.append("basic=").append(basic).append(", netPay=").append(netPay).append(", grossPay=").append(grossPay)
				.append(", workingDays=").append(workingDays).append(", holidays=").append(holidays)
				.append(", deductionDays=").append(deductionDays).append(", payAfterHolidays=").append(payAfterHolidays)
				.append(", overtime=").append(overtime).append(", PT=").append(PT).append(", otIncentive=")
				.append(otIncentive).append(", clRemaining=").append(clRemaining).append(", clConsidered=")
				.append(clConsidered).append(", pfEmployeeDeduction=").append(pfEmployeeDeduction)
				.append(", pfCompanyDeduction=").append(pfCompanyDeduction).append(", pfGovernmentContribution=")
				.append(pfGovernmentContribution).append(", otherLeavesConsumed=").append(otherLeavesConsumed)
				.append(", salaryWithheld=").append(salaryWithheld)
				.append(", otherLeavesRemaining=")
				.append(otherLeavesRemaining)
				.append(", ").append("Month=").append(month)
				.append(", ").append("Year=").append(year);
		if (remarks != null)
			builder.append(", ").append("remarks=").append(remarks);
		if (emailId != null)
			builder.append(", ").append("emailId=").append(emailId);
		builder.append("]");
		return builder.toString();
	}
	public double getSalaryWithheld() {
		return salaryWithheld;
	}
	public void setSalaryWithheld(double salaryWithheld) {
		this.salaryWithheld = salaryWithheld;
	}
	public String getSendMail() {
		return sendMail;
	}
	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	
}
