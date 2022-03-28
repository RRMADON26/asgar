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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static controller.Controller.addBooking;
import static controller.Controller.getAvailableBookingByDate;
import static controller.Controller.getBarber;
import static controller.Controller.getBooking;
import static controller.Controller.getCategoryServices;
import static controller.Controller.getTime;

public class BookingForm {
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

	public BookingForm() {
		String pattern = "MM-dd-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		Book from = new Book();

		from.setServiceId(1);
		from.setBarbedId(1);
		from.setDateTime(simpleDateFormat.format(new Date()));

		final BookingTableModel[] bookingTableModel = {new BookingTableModel(getBooking())};

		jTable.setModel(bookingTableModel[0]);

		initialComponent();

		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (!getAvailableBookingByDate(from.getDateTime())) {
						JOptionPane.showMessageDialog(null, "Not Available");
					} else {
						from.setCode(UUID.randomUUID().toString());
						from.setMobileNumber(mobileNumberTextField.getText());
						from.setName(nameTextField.getText());

						addBooking(from);
						System.out.println("Successfully add booking ");
						bookingTableModel[0] = new BookingTableModel(getBooking());
						jTable.setModel(bookingTableModel[0]);
					}


				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});

		barberComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox jComboBox = (JComboBox) e.getSource();

				Barber barber = (Barber) jComboBox.getSelectedItem();

				from.setBarbedId(barber.getId());
			}
		});

		serviceComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox jComboBox = (JComboBox) e.getSource();

				CategoryService categoryService = (CategoryService) jComboBox.getSelectedItem();

				from.setServiceId(categoryService.getId());
			}
		});

		jDateChooser1.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if ("date".equals(e.getPropertyName())) {
					from.setDateTime(simpleDateFormat.format(Date.from(jDateChooser1.getDate().toInstant())));
				}
			}

		});

		timeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				from.setDateTime(from.getDateTime() + " " + timeComboBox.getSelectedItem());
			}
		});

		jTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable jTable = (JTable) e.getSource();

					String[] options = {"Confirm", "Cancel Booking", "Quit"};

					int result = JOptionPane.showOptionDialog(null, "Are you want to ?", "Confirmation booking",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
							null, options, null);
					if (result == JOptionPane.YES_OPTION) {
						Controller.updateStatus("CONFIRM", jTable.getValueAt(jTable.getSelectedRow(), 0).toString());
					} else if (result == JOptionPane.NO_OPTION) {
						Controller.updateStatus("CANCELLED", jTable.getValueAt(jTable.getSelectedRow(), 0).toString());
					}

				}

				bookingTableModel[0] = new BookingTableModel(getBooking());
				jTable.setModel(bookingTableModel[0]);


			}
		});

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("BookingForm");

		frame.setContentPane(new BookingForm().panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private void initialComponent() {
		DefaultComboBoxModel<Barber> barberDefaultComboBoxModel = new DefaultComboBoxModel<>(getBarber().toArray(new Barber[0]));

		barberComboBox.setModel(barberDefaultComboBoxModel);
		DefaultComboBoxModel<CategoryService> categoryServiceDefaultComboBoxModel = new DefaultComboBoxModel<>(getCategoryServices().toArray(new CategoryService[0]));

		serviceComboBox.setModel(categoryServiceDefaultComboBoxModel);

		DefaultComboBoxModel<String> timDefaultComboBoxModel = new DefaultComboBoxModel<>(getTime());
		timeComboBox.setModel(timDefaultComboBoxModel);
	}

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

	public void createUIComponents() {
		jDateChooser1 = new JDateChooser(Date.from(Instant.now()));
		jDateChooser1.setDateFormatString("dd MMMM yyyy");

	}
}
