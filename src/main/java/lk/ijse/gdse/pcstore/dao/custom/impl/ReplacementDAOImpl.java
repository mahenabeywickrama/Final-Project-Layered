package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.ReplacementDAO;
import lk.ijse.gdse.pcstore.dto.ReplacementDTO;
import lk.ijse.gdse.pcstore.entity.Replacement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ReplacementDAOImpl implements ReplacementDAO {
    @Override
    public String getNextId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select replacement_id from replacement order by replacement_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("R%03d", newIdIndex);
        }
        return "R001";
    }

    @Override
    public ArrayList<Replacement> getAll() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from replacement");

        ArrayList<Replacement> replacements = new ArrayList<>();

        while (rst.next()) {
            Replacement replacement = new Replacement(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    Integer.parseInt(rst.getString(5)),
                    LocalDate.parse(rst.getString(6)),
                    LocalTime.parse(rst.getString(7)),
                    rst.getString(8)
            );
            replacements.add(replacement);
        }
        return replacements;
    }

    @Override
    public boolean save(Replacement replacement) throws SQLException {
        return SQLUtil.execute(
                "insert into replacement values (?,?,?,?,?,?,?,?)",
                replacement.getReplacementId(),
                replacement.getOrdersId(),
                replacement.getFaultyItemId(),
                replacement.getReplacedItemId(),
                replacement.getQty(),
                replacement.getDate(),
                replacement.getTime(),
                replacement.getRemarks()
        );
    }

    @Override
    public boolean update(Replacement replacement) throws SQLException {
        return SQLUtil.execute(
                "update replacement set orders_id=?, faulty_item_id=?, replacement_item_id=?, qty=?, date=?, time=?, remarks=? where replacement_id=?",
                replacement.getOrdersId(),
                replacement.getFaultyItemId(),
                replacement.getReplacedItemId(),
                replacement.getQty(),
                replacement.getDate(),
                replacement.getTime(),
                replacement.getRemarks(),
                replacement.getReplacementId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return SQLUtil.execute("delete from replacement where replacement_id=?", id);
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
    public Replacement findById(String id) throws SQLException {
        return null;
    }

    @Override
    public Replacement findByName(String name) throws SQLException {
        return null;
    }
}
