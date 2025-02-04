package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.CustomerDAO;
import lk.ijse.gdse.pcstore.entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public String getNextId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select customer_id from customer order by customer_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("C%03d", newIdIndex);
        }

        return "C001";
    }

    @Override
    public ArrayList<Customer> getAll() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from customer");
        ArrayList<Customer> customers = new ArrayList<>();

        while (rst.next()) {
            Customer customer = new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
            customers.add(customer);
        }
        return customers;
    }

    @Override
    public boolean save(Customer customer) throws SQLException {
        return SQLUtil.execute(
                "insert into customer values (?,?,?,?,?,?)",
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getCustomerAddress(),
                customer.getCustomerNic(),
                customer.getCustomerPhone(),
                customer.getCustomerEmail()
        );
    }

    @Override
    public boolean update(Customer customer) throws SQLException {
        return SQLUtil.execute(
                "update customer set name=?, address = ?, nic=?, phone=?, email=? where customer_id=?",
                customer.getCustomerName(),
                customer.getCustomerAddress(),
                customer.getCustomerNic(),
                customer.getCustomerPhone(),
                customer.getCustomerEmail(),
                customer.getCustomerId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return SQLUtil.execute("delete from customer where customer_id=?", id);
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select customer_id from customer");

        ArrayList<String> customerIds = new ArrayList<>();

        while (rst.next()) {
            customerIds.add(rst.getString(1));
        }

        return customerIds;
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        ResultSet rst = SQLUtil.execute("select name from customer");

        ArrayList<String> customerNames = new ArrayList<>();

        while (rst.next()) {
            customerNames.add(rst.getString(1));
        }

        return customerNames;
    }

    @Override
    public Customer findById(String id) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from customer where customer_id = ?", id);

        if (rst.next()) {
            return new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
        }
        return null;
    }

    @Override
    public Customer findByName(String name) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from customer where name = ?", name);

        if (rst.next()) {
            return new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
        }
        return null;
    }
}
