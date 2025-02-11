package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.custom.PaymentBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.custom.PaymentDAO;
import lk.ijse.gdse.pcstore.dto.PaymentDTO;
import lk.ijse.gdse.pcstore.entity.Payment;

import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentBOImpl implements PaymentBO {

    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);

    @Override
    public String getNextPaymentId() throws SQLException {
        return paymentDAO.getNextId();
    }

    @Override
    public boolean savePayment(PaymentDTO paymentDTO) throws SQLException {
        return paymentDAO.save(new Payment(paymentDTO.getPaymentId(), paymentDTO.getOrdersId(), paymentDTO.getPaymentDate(), paymentDTO.getPaymentTime(), paymentDTO.getPaymentMethod(), paymentDTO.getPaymentAmount(), paymentDTO.getBalance()));
    }

    @Override
    public ArrayList<PaymentDTO> getAllPayments() throws SQLException {
        ArrayList<PaymentDTO> paymentDTOArrayList = new ArrayList<>();
        ArrayList<Payment> payments = paymentDAO.getAll();

        for (Payment payment : payments) {
            PaymentDTO paymentDTO = new PaymentDTO(
                    payment.getPaymentId(),
                    payment.getOrdersId(),
                    payment.getPaymentDate(),
                    payment.getPaymentTime(),
                    payment.getPaymentMethod(),
                    payment.getPaymentAmount(),
                    payment.getBalance()
            );
            paymentDTOArrayList.add(paymentDTO);
        }

        return paymentDTOArrayList;
    }

    @Override
    public boolean updatePayment(PaymentDTO paymentDTO) throws SQLException {
        return paymentDAO.update(new Payment(paymentDTO.getPaymentId(), paymentDTO.getOrdersId(), paymentDTO.getPaymentDate(), paymentDTO.getPaymentTime(), paymentDTO.getPaymentMethod(), paymentDTO.getPaymentAmount(), paymentDTO.getBalance()));
    }

    @Override
    public boolean deletePayment(String paymentId) throws SQLException {
        return paymentDAO.delete(paymentId);
    }
}
