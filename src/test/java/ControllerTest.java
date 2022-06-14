import controller.Controller;
import model.Book;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ControllerTest {

	@Mock
	Controller controller;

	@Mock
	Book.VBook vBook;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getBookingListTest() {
		List<Book.VBook> list = new ArrayList<Book.VBook>();
		Book.VBook vBook1 = new Book.VBook(1, "001", "Justin", "22 March 2022 01:00PM", "022983566", "BOOK", "John", "Premium Haircut");

		//vBook1.setMobileNumber("09838383"); cara lain penulisan assign data
		//Book.VBook vBook2 = new Book.VBook()

		list.add(vBook1);

		when(controller.getBooking()).thenReturn(list);

		List<Book.VBook> bookList = controller.getBooking();

		assertEquals(1, bookList.size());

	}

	@Test
	public void addBooking() throws SQLException {
		Book book = new Book();

		book.setCode("CODE1");
		book.setName("Justin");
		book.setDateTime("22 March 2022 01:00PM");
		book.setMobileNumber("303 020203");
		book.setStatus("BOOK");

		when(controller.addBooking(book)).thenReturn(book);

		Book tested = controller.addBooking(book);

		assertEquals(book.getCode(), tested.getCode());
		assertEquals(book.getName(), tested.getName());
		assertEquals(book.getDateTime(), tested.getDateTime());
		assertEquals(book.getMobileNumber(), tested.getMobileNumber());
		assertEquals(book.getStatus(), tested.getStatus());


		///////////////////////////////
		Book oBook = new Book();

		oBook.setCode("0001");



		oBook.getCode();


	}



}
