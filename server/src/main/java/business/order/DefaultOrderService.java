package business.order;

import api.ApiException;
import business.BookstoreDbException;
import business.JdbcUtils;
import business.book.Book;
import business.book.BookDao;
import business.book.BookForm;
import business.cart.ShoppingCart;
import business.cart.ShoppingCartItem;
import business.customer.Customer;
import business.customer.CustomerDao;
import business.customer.CustomerForm;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {

	private BookDao bookDao;
	private OrderDao orderDao;
	private LineItemDao lineItemDao;
	private CustomerDao customerDao;

	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}
	public void setOrderDao(OrderDao orderDao) { this.orderDao = orderDao;}
	public void setLineItemDao(LineItemDao lineItemDao) {this.lineItemDao = lineItemDao;}
	public void setCustomerDao(CustomerDao customerDao) {this.customerDao = customerDao;}

	@Override
	public OrderDetails getOrderDetails(long orderId) {
		Order order = orderDao.findByOrderId(orderId);
		Customer customer = customerDao.findByCustomerId(order.getCustomerId());
		List<LineItem> lineItems = lineItemDao.findByOrderId(orderId);
		List<Book> books = lineItems
				.stream()
				.map(lineItem -> bookDao.findByBookId(lineItem.getBookId()))
				.collect(Collectors.toList());
		return new OrderDetails(order, customer, lineItems, books);
	}

	@Override
    public long placeOrder(CustomerForm customerForm, ShoppingCart cart) {

		validateCustomer(customerForm);
		validateCart(cart);

		try (Connection connection = JdbcUtils.getConnection()) {
			Date date = getDate(
					customerForm.getCcExpiryMonth(),
					customerForm.getCcExpiryYear());
			return performPlaceOrderTransaction(
					customerForm.getName(),
					customerForm.getAddress(),
					customerForm.getPhone(),
					customerForm.getEmail(),
					customerForm.getCcNumber(),
					date, cart, connection);
		} catch (SQLException e) {
			throw new BookstoreDbException("Error during close connection for customer order", e);
		}
	}

	private Date getDate(String monthString, String yearString) {
		return new Date(); // TODO Implement this correctly
	}

	private long performPlaceOrderTransaction(
			String name, String address, String phone,
			String email, String ccNumber, Date date,
			ShoppingCart cart, Connection connection) {
		try {
			connection.setAutoCommit(false);
			long customerId = customerDao.create(
					connection, name, address, phone, email,
					ccNumber, date);
			long customerOrderId = orderDao.create(
					connection,
					cart.getComputedSubtotal() + cart.getSurcharge(),
					generateConfirmationNumber(), customerId);
			for (ShoppingCartItem item : cart.getItems()) {
				lineItemDao.create(connection, customerOrderId, item.getBookId(),
						item.getQuantity());
			}
			connection.commit();
			return customerOrderId;
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new BookstoreDbException("Failed to roll back transaction", e1);
			}
			return 0;
		}
	}

	private int generateConfirmationNumber() {
		Random rnd = new Random();
		return rnd.nextInt(999999999);
	}

	private void validateCustomer(CustomerForm customerForm) {

    	String name = customerForm.getName();

		if (name == null || name.equals("") || name.length() > 45 || name.length() < 4) {
			throw new ApiException.InvalidParameter("Invalid name field - length");
		}

		String address = customerForm.getAddress();

		if (address == null || address.equals("")|| name.length() > 45 || name.length() < 4) {
			throw new ApiException.InvalidParameter("Invalid address field - length");
		}

		String phone = customerForm.getPhone();
		if (phone == null || phone.equals("")) {
			throw new ApiException.InvalidParameter("Invalid phone field - no input");
		}else {
			String digits = phone.replaceAll("\\D", "");
			if (digits.length() != 10) {
				throw new ApiException.InvalidParameter("Invalid phone field - not 10 digits");
			}
		}

		String email = customerForm.getEmail();
		if (email == null || email.length() == 0 || doesNotLookLikeAnEmail(email)) {
			throw new ApiException.InvalidParameter("Invalid email field - length");
		} else {
			if(!email.contains("@")) throw new ApiException.InvalidParameter("Invalid email field - no '@' ");
			if(email.endsWith(".")) throw new ApiException.InvalidParameter("Invalid email field - ends with .");
		}

		String ccNumber = customerForm.getCcNumber();
		if (ccNumber == null || ccNumber.equals("")) {
			throw new ApiException.InvalidParameter("Invalid ccNumber field - no input");
		} else {
			String digits = ccNumber.replaceAll("\\D", "");
			if (digits.length() < 14 || digits.length() > 16){
				throw new ApiException.InvalidParameter("Invalid ccNumber field - length");
			}
		}

		if (expiryDateIsInvalid(customerForm.getCcExpiryMonth(), customerForm.getCcExpiryYear())) {
			throw new ApiException.InvalidParameter("Invalid expiry date");

		}
	}

	private static Pattern SIMPLE_EMAIL_REGEX = Pattern.compile("^\\S+@\\S+$");

	private boolean doesNotLookLikeAnEmail(String email) {
		return !SIMPLE_EMAIL_REGEX.matcher(email).matches();
	}

	private boolean expiryDateIsInvalid(String ccExpiryMonth, String ccExpiryYear) {
		try {
			int month = Integer.parseInt(ccExpiryMonth);
			int year = Integer.parseInt(ccExpiryYear);

			YearMonth ccExpiryDate = YearMonth.of(year, month);

			return YearMonth.now().isAfter(ccExpiryDate);
		} catch (NumberFormatException | NullPointerException | DateTimeException ex) {
			return true;
		}
	}

	private void validateCart(ShoppingCart cart) {

		if (cart.getItems().size() <= 0) {
			throw new ApiException.InvalidParameter("Cart is empty.");
		}

		cart.getItems().forEach(item-> {
			if (item.getQuantity() < 0 || item.getQuantity() > 99) {
				throw new ApiException.InvalidParameter("Invalid quantity");
			}
			BookForm requested= item.getBookForm();
			Book databaseBook = bookDao.findByBookId(item.getBookId());
			// TODO: complete the required validations
			if (requested.getPrice() != databaseBook.getPrice()) {
				throw new ApiException.InvalidParameter("Invalid price");
			}
			if (requested.getCategoryId() != databaseBook.getCategoryId()) {
				throw new ApiException.InvalidParameter("Invalid category");
			}
		});
	}

}
