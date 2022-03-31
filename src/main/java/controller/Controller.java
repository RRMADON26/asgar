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

import static model.enumiration.StatusKind.BOOKED;
import static model.enumiration.StatusKind.CANCELLED;

public class Controller {

	private static Connection connection;

	private static Statement statement;

	private static ResultSet resultSet;

	/**
	 * Add booking after passed validation
	 *
	 * @param book
	 * @throws SQLException
	 */
	public static void addBooking(Book book) throws SQLException {

		try {
			connection = Connect.toDb();
			statement = connection.createStatement();

			String insert = "INSERT INTO Book values(null ,'" + book.getCode() + "','" + book.getDateTime() + "' , '" + book.getMobileNumber() + "' , '" + book.getName() + "' , '" + BOOKED.name() + "','" + book.getBarbedId() + "','" + book.getServiceId() + "')";

			statement.execute(insert);

			statement.close();
			connection.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	/**
	 * Getting all booking
	 *
	 * @return List of View Booking
	 */
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

	/**
	 * Getting all Barber
	 *
	 * @return List of Barber
	 */
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

	/**
	 * Getting all Category Service
	 *
	 * @return List of Category Service
	 */
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

	/**
	 * Update status Booking to CONFIRM or CANCELLED
	 *
	 * @param statusBooking
	 * @param code
	 */
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

	/**
	 * Check booking is existed or not in the same time
	 *
	 * @param dateTime
	 * @param barber
	 * @return boolean
	 */
	public static boolean getAvailableBookingByDateAndBarber(String dateTime, int barber) {

		try {
			connection = Connect.toDb();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT count(*) as recordCount FROM Book WHERE dateTime = '" + dateTime + "' AND  barberName_id = '" + barber + "' AND status != '" + CANCELLED.name() + "'");

			while (resultSet.next()) {
				if (resultSet.getInt("recordCount") >= 1) {
					statement.close();
					connection.close();
					return false;

				}
			}

			statement.close();
			connection.close();

		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		return true;
	}

	/**
	 * Search By Name
	 *
	 * @param name
	 * @return List of View Booking
	 */
	public static List<Book.VBook> search(String name) {
		List<Book.VBook> bookingList = new ArrayList<>();

		try {
			connection = Connect.toDb();
			statement = connection.createStatement();
			resultSet = statement.executeQuery
					("SELECT  b.* , c.name as barberName , d.name as categoryServiceName FROM Book b " +
							"join Barber c on c.id = b.barberName_id " +
							"join CategoryService d on b.categoryService_id = d.id WHERE  b.name LIKE '%" + name + "%'");

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

	/**
	 * List of time
	 *
	 * @return Arrays String
	 */
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
