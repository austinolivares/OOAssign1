package misc;

public class Record {
	private String firstName;
	private String lastName;
	private int age;
	private String employeeType;
	private double payRate;

/*public Record(String firstName, String lastName, int age, String employeeType, double payRate){
	this.setFirstName(firstName);
	this.setLastName(lastName);
	this.setAge(age);
	this.setEmployeeType(employeeType);
	this.setPayRate(payRate);
}*/
	public Record(){
		
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public double getPayRate() {
		return payRate;
	}

	public void setPayRate(double payRate) {
		this.payRate = payRate;
	}

}