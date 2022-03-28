package model;

public class Book extends Base {

	private String dateTime;

	private String mobileNumber;

	private String status;

	private int barbedId;

	private int serviceId;

	public Book() {
	}

	public Book(int id, String code, String name, String dateTime, String mobileNumber, String status, int barbedId, int serviceId) {
		this.setId(id);
		this.setCode(code);
		this.setName(name);
		this.dateTime = dateTime;
		this.mobileNumber = mobileNumber;
		this.status = status;
		this.barbedId = barbedId;
		this.serviceId = serviceId;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getBarbedId() {
		return barbedId;
	}

	public void setBarbedId(int barbedId) {
		this.barbedId = barbedId;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}


	public static class VBook extends Book {
		private String barberName;

		public String getBarberName() {
			return barberName;
		}

		public String getCategoryServiceName() {
			return categoryServiceName;
		}

		private String categoryServiceName;

		public VBook(int id, String code, String name, String dateTime, String mobileNumber, String status, String barberName, String serviceName) {
			this.setId(id);
			this.setCode(code);
			this.setName(name);
			this.setDateTime(dateTime);
			this.setMobileNumber(mobileNumber);
			this.setStatus(status);
			this.barberName = barberName;
			this.categoryServiceName = serviceName;
		}
	}
}
