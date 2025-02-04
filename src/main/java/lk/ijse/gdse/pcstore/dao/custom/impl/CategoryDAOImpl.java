package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.CategoryDAO;
import lk.ijse.gdse.pcstore.entity.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDAOImpl implements CategoryDAO {
    @Override
    public String getNextId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select category_id from category order by category_id desc limit 1");

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
    public ArrayList<Category> getAll() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from category");

        ArrayList<Category> categorys = new ArrayList<>();

        while (rst.next()) {
            Category category = new Category(
                    rst.getString(1),
                    rst.getString(2)
            );
            categorys.add(category);
        }
        return categorys;
    }

    @Override
    public boolean save(Category category) throws SQLException {
        return SQLUtil.execute(
                "insert into category values (?,?)",
                category.getCategoryId(),
                category.getDescription()
        );
    }

    @Override
    public boolean update(Category dto) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        return null;
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        ResultSet rst = SQLUtil.execute("select description from category");
        ArrayList<String> categoryDescriptions = new ArrayList<>();

        while (rst.next()) {
            categoryDescriptions.add(rst.getString(1));
        }

        return categoryDescriptions;
    }

    @Override
    public Category findById(String id) throws SQLException {
        ResultSet rst = SQLUtil.execute("select description from category where category_id=?", id);

        if (rst.next()) {
            return new Category(
                    rst.getString(1),
                    rst.getString(2)
            );
        }

        return null;
    }

    @Override
    public Category findByName(String name) throws SQLException {
        ResultSet rst = SQLUtil.execute("select category_id from category where description=?", name);

        if (rst.next()) {
            return new Category(
                    rst.getString(1),
                    rst.getString(2)
            );
        }

        return null;
    }
}
