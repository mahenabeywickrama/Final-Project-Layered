package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.PaymentDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentModel {
    public String getNextPaymentId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select payment_id from payment order by payment_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("P%03d", newIdIndex);
        }
        return "P001";
    }

    public boolean savePayment(PaymentDTO paymentDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into payment values (?,?,?,?,?,?,?)",
                paymentDTO.getPaymentId(),
                paymentDTO.getOrdersId(),
                paymentDTO.getPaymentDate(),
                paymentDTO.getPaymentTime(),
                paymentDTO.getPaymentMethod(),
                paymentDTO.getPaymentAmount(),
                paymentDTO.getBalance()
        );
    }

    public ArrayList<PaymentDTO> getAllPayments() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from payment");

        ArrayList<PaymentDTO> paymentDTOS = new ArrayList<>();

        while (rst.next()) {
            PaymentDTO paymentDTO = new PaymentDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDate(3).toLocalDate(),
                    rst.getTime(4).toLocalTime(),
                    rst.getString(5),
                    Double.parseDouble(rst.getString(6)),
                    Double.parseDouble(rst.getString(7))
            );
            paymentDTOS.add(paymentDTO);
        }
        return paymentDTOS;
    }

    public boolean updatePayment(PaymentDTO paymentDTO) throws SQLException {
        return SQLUtil.execute(
                "update payment set orders_id=?, date=?, time=?, method=?, total_amount=?, balance=? where payment_id=?",
                paymentDTO.getOrdersId(),
                paymentDTO.getPaymentDate(),
                paymentDTO.getPaymentTime(),
                paymentDTO.getPaymentMethod(),
                paymentDTO.getPaymentAmount(),
                paymentDTO.getBalance(),
                paymentDTO.getPaymentId()
        );
    }

    public boolean deletePayment(String paymentId) throws SQLException {
        return SQLUtil.execute("delete from payment where payment_id=?", paymentId);
    }
}
