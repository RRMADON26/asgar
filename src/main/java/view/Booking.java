package view;

import com.toedter.calendar.JDateChooser;
import controller.Controller;
import model.Barber;
import model.Book;
import model.CategoryService;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static model.enumiration.StatusKind.CANCELLED;
import static model.enumiration.StatusKind.CONFIRM;

public class Booking {
	private JTextField nameTextField;
	private JTextField mobileNumberTextField;
	private JTextField dateTimeTextField;
	private JPanel panel;
	private JComboBox<Barber> barberComboBox;
	private JComboBox serviceComboBox;
	private JButton clearButton;
	private JButton submitButton;
	private JLabel nameLabel;
	private JLabel mobileNumberLabel;
	private JLabel barberLabel;
	private JScrollPane jScrollPane;
	private JTable jTable;
	private JComboBox timeComboBox;
	private JDateChooser jDateChooser1;
	private JButton printButton;
	private JTextField search;
	private JButton searchButton;
	private Controller controller = new Controller();

	public Booking() {

		//formating date
		String pattern = "MM-dd-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		final String[] date = {null};
		final String[] time = {null};

		// create instance for Book
		Book from = new Book();

		//set default data
		from.setServiceId(1);
		from.setBarbedId(1);
		from.setDateTime(simpleDateFormat.format(new Date()) + " " + controller.getTime()[0]);

		//define table
		final BookingTableModel[] bookingTableModel = {new BookingTableModel(controller.getBooking())};

		jTable.setModel(bookingTableModel[0]);
		jTable.getAutoCreateRowSorter();

		initialComponent();

		/**
		 * Override button action
		 * This button used for send data to controller
		 */
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (date[0] == null) date[0] = simpleDateFormat.format(new Date());
				if (time[0] == null) time[0] = controller.getTime()[0];

				try {
					String dateTime = date[0] + " " + time[0];
					if (!controller.getAvailableBookingByDateAndBarber(dateTime, from.getBarbedId())) {
						JOptionPane.showMessageDialog(null, "Not Available");
					} else {
						from.setCode(UUID.randomUUID().toString());
						from.setMobileNumber(mobileNumberTextField.getText());
						from.setName(nameTextField.getText());
						from.setDateTime(dateTime);
						System.out.println(from.getDateTime());

						controller.addBooking(from);
						System.out.println("Successfully add booking ");

					}

					jTable.revalidate();

					bookingTableModel[0] = new BookingTableModel(controller.getBooking());
					jTable.setModel(bookingTableModel[0]);


				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});

		/**
		 * capture value when select combo box barber
		 */
		barberComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox jComboBox = (JComboBox) e.getSource();

				Barber barber = (Barber) jComboBox.getSelectedItem();

				from.setBarbedId(barber.getId());
			}
		});

		/**
		 * capture value when select combo box category service
		 */
		serviceComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox jComboBox = (JComboBox) e.getSource();

				CategoryService categoryService = (CategoryService) jComboBox.getSelectedItem();

				from.setServiceId(categoryService.getId());
			}
		});

		/**
		 * capture value when select Date
		 */
		jDateChooser1.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if ("date".equals(e.getPropertyName())) {
					date[0] = simpleDateFormat.format(Date.from(jDateChooser1.getDate().toInstant()));
				}

			}

		});

		/**
		 * capture value when select combo box "time"
		 */
		timeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				time[0] = timeComboBox.getSelectedItem().toString();
			}
		});

		/**
		 * when double click from "mouse" we need to capture id and show Option Dialog
		 */
		jTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable jTable = (JTable) e.getSource();

					String[] options = {"Confirm", "Cancel Booking", "Quit"};

					int result = JOptionPane.showOptionDialog(null, "Are you want to ?", "Confirmation Booking",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
							null, options, null);
					if (result == JOptionPane.YES_OPTION) {
						controller.updateStatus(CONFIRM.name(), jTable.getValueAt(jTable.getSelectedRow(), 0).toString());
					} else if (result == JOptionPane.NO_OPTION) {
						controller.updateStatus(CANCELLED.name(), jTable.getValueAt(jTable.getSelectedRow(), 0).toString());
					}

				}

				bookingTableModel[0] = new BookingTableModel(controller.getBooking());
				jTable.setModel(bookingTableModel[0]);

			}
		});

		/**
		 * this button used to print data from Table
		 */
		printButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					jTable.print(JTable.PrintMode.FIT_WIDTH, null, null);
				} catch (PrinterException ex) {
					ex.printStackTrace();
				}
			}
		});

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bookingTableModel[0] = new BookingTableModel(controller.search(search.getText()));
				jTable.setModel(bookingTableModel[0]);
			}
		});
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("BookingForm");

		frame.setContentPane(new Booking().panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private void initialComponent() {
		// get all barber from controller
		DefaultComboBoxModel<Barber> barberDefaultComboBoxModel = new DefaultComboBoxModel<>(controller.getBarber().toArray(new Barber[0]));

		barberComboBox.setModel(barberDefaultComboBoxModel);
		// get all category service from controller
		DefaultComboBoxModel<CategoryService> categoryServiceDefaultComboBoxModel = new DefaultComboBoxModel<>(controller.getCategoryServices().toArray(new CategoryService[0]));

		serviceComboBox.setModel(categoryServiceDefaultComboBoxModel);

		// get all "time" from controller
		DefaultComboBoxModel<String> timDefaultComboBoxModel = new DefaultComboBoxModel<>(controller.getTime());
		timeComboBox.setModel(timDefaultComboBoxModel);
	}

	/**
	 * setup layout table
	 */
	private static class BookingTableModel extends AbstractTableModel {
		private final String[] columnNames = {"Code", "Name", "Date Time", "Mobile Number", "Status", "Barber Name", "Service"};

		private List<Book.VBook> bookList;

		public BookingTableModel(List<Book.VBook> bookList) {
			this.bookList = bookList;
		}

		@Override
		public int getRowCount() {
			return bookList.size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			String s;

			switch (columnIndex) {
				case 0:
					s = bookList.get(rowIndex).getCode();
					break;
				case 1:
					s = bookList.get(rowIndex).getName();
					break;
				case 2:
					s = bookList.get(rowIndex).getDateTime();
					break;
				case 3:
					s = bookList.get(rowIndex).getMobileNumber();
					break;
				case 4:
					s = bookList.get(rowIndex).getStatus();
					break;
				case 5:
					s = bookList.get(rowIndex).getBarberName();
					break;
				case 6:
					s = bookList.get(rowIndex).getCategoryServiceName();
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + columnIndex);
			}

			return s;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return super.getColumnClass(columnIndex);
		}
	}

	/**
	 * pre define JdateChooser Component
	 */
	public void createUIComponents() {
		jDateChooser1 = new JDateChooser(Date.from(Instant.now()));
		jDateChooser1.setDateFormatString("dd MMMM yyyy");

	}
}
