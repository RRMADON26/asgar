package controller;

import connection.Connect;
import model.Barber;
import model.Book;
import model.CategoryService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Controller {

	private static Connection connection;

	private static Statement statement;

	private static ResultSet resultSet;

	public static void addBooking(Book to) throws SQLException {

		try {
			connection = Connect.toDb();
			statement = connection.createStatement();

			String insert = "INSERT INTO Book values(null ,'" + to.getCode() + "','" + to.getDateTime() + "' , '" + to.getMobileNumber() + "' , '" + to.getName() + "' , 'Book','" + to.getBarbedId() + "','" + to.getServiceId() + "')";

			statement.execute(insert);

			statement.close();
			connection.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static List<Book.VBook> getBooking() {
		List<Book.VBook> bookingList = new ArrayList<>();

		try {
			connection = Connect.toDb();
			statement = connection.createStatement();
			resultSet = statement.executeQuery
					("SELECT  b.* , c.name as barberName , d.name as categoryServiceName FROM Book b " +
							"join Barber c on c.id = b.barberName_id " +
							"join CategoryService d on b.categoryService_id = d.id");

			while (resultSet.next()) {
				Book.VBook data = new Book.VBook(
						resultSet.getInt("id"),
						resultSet.getString("code"),
						resultSet.getString("name"),
						resultSet.getString("dateTime"),
						resultSet.getString("mobileNumber"),
						resultSet.getString("status"),
						resultSet.getString("barberName"),
						resultSet.getString("categoryServiceName"));

				bookingList.add(data);

			}

			statement.close();
			connection.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return bookingList;
	}

	public static List<Barber> getBarber() {
		List<Barber> barberList = new ArrayList<>();

		try {
			connection = Connect.toDb();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT  * FROM Barber");

			while (resultSet.next()) {
				Barber data = new Barber(
						resultSet.getInt("id"),
						resultSet.getString("code"),
						resultSet.getString("name"));

				barberList.add(data);
			}

			statement.close();
			connection.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return barberList;
	}

	public static List<CategoryService> getCategoryServices() {
		List<CategoryService> categoryServiceList = new ArrayList<>();

		try {
			connection = Connect.toDb();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM CategoryService");

			while (resultSet.next()) {
				CategoryService data = new CategoryService(
						resultSet.getInt("id"),
						resultSet.getString("code"),
						resultSet.getString("name"),
						resultSet.getBigDecimal("price"));

				categoryServiceList.add(data);
			}

			statement.close();
			connection.close();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return categoryServiceList;
	}

	public static void updateStatus(String statusBooking, String code) {
		try {
			connection = Connect.toDb();
			statement = connection.createStatement();
			statement.execute("UPDATE Book SET status = '" + statusBooking + "' WHERE code = '" + code + "'");

			statement.close();
			connection.close();
			System.out.println("SUCCESS UPDATE STATUS");
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static boolean getAvailableBookingByDate(String dateTime) {

//		try {
//			connection = Connect.toDb();
//			statement = connection.createStatement();
//			resultSet = statement.executeQuery("SELECT * FROM Book WHERE dateTime = '"+dateTime+"' ");
//
//			resultSet.last();
//
//			System.out.println(resultSet.getRow());
//			statement.close();
//			connection.close();
//


		return true;
	}

	public static String[] getTime() {
		return new String[]{
				"1:00 PM",
				"2:00 PM",
				"3:00 PM",
				"4:00 PM",
				"5:00 PM",
				"6:00 PM",
				"7:00 PM",
				"8:00 PM",
				"9:00 PM"};
	}
}
