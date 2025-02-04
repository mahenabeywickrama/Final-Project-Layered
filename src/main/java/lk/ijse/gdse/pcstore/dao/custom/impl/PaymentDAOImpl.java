package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.PaymentDAO;
import lk.ijse.gdse.pcstore.entity.Payment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public String getNextId() throws SQLException {
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

    @Override
    public ArrayList<Payment> getAll() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from payment");

        ArrayList<Payment> payments = new ArrayList<>();

        while (rst.next()) {
            Payment payment = new Payment(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDate(3).toLocalDate(),
                    rst.getTime(4).toLocalTime(),
                    rst.getString(5),
                    Double.parseDouble(rst.getString(6)),
                    Double.parseDouble(rst.getString(7))
            );
            payments.add(payment);
        }
        return payments;
    }

    @Override
    public boolean save(Payment payment) throws SQLException {
        return SQLUtil.execute(
                "insert into payment values (?,?,?,?,?,?,?)",
                payment.getPaymentId(),
                payment.getOrdersId(),
                payment.getPaymentDate(),
                payment.getPaymentTime(),
                payment.getPaymentMethod(),
                payment.getPaymentAmount(),
                payment.getBalance()
        );
    }

    @Override
    public boolean update(Payment payment) throws SQLException {
        return SQLUtil.execute(
                "update payment set orders_id=?, date=?, time=?, method=?, total_amount=?, balance=? where payment_id=?",
                payment.getOrdersId(),
                payment.getPaymentDate(),
                payment.getPaymentTime(),
                payment.getPaymentMethod(),
                payment.getPaymentAmount(),
                payment.getBalance(),
                payment.getPaymentId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return SQLUtil.execute("delete from payment where payment_id=?", id);
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        return null;
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        return null;
    }

    @Override
    public Payment findById(String id) throws SQLException {
        return null;
    }

    @Override
    public Payment findByName(String name) throws SQLException {
        return null;
    }
}
