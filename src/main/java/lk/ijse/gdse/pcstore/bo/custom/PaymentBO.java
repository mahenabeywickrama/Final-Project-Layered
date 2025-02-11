package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.PaymentDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PaymentBO extends SuperBO {
    String getNextPaymentId() throws SQLException;
    boolean savePayment(PaymentDTO paymentDTO) throws SQLException;
    ArrayList<PaymentDTO> getAllPayments() throws SQLException;
    boolean updatePayment(PaymentDTO paymentDTO) throws SQLException;
    boolean deletePayment(String paymentId) throws SQLException;
}
