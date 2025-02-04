package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.CategoryDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryModel {
    public String getNextCategoryId() throws SQLException {
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

    public boolean saveCategory(CategoryDTO categoryDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into category values (?,?)",
                categoryDTO.getCategoryId(),
                categoryDTO.getDescription()
        );
    }

    public ArrayList<CategoryDTO> getAllCategories() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from category");

        ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();

        while (rst.next()) {
            CategoryDTO categoryDTO = new CategoryDTO(
                    rst.getString(1),
                    rst.getString(2)
            );
            categoryDTOS.add(categoryDTO);
        }
        return categoryDTOS;
    }

    public ArrayList<String> getAllCategoryIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select description from category");
        ArrayList<String> categoryDescriptions = new ArrayList<>();

        while (rst.next()) {
            categoryDescriptions.add(rst.getString(1));
        }

        return categoryDescriptions;
    }

    public String findByDescription(String selectedCategoryDescription) throws SQLException {
        ResultSet rst = SQLUtil.execute("select category_id from category where description=?", selectedCategoryDescription);

        if (rst.next()) {
            return rst.getString(1);
        }

        return null;
    }

    public String findById(String selectedCategoryId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select description from category where category_id=?", selectedCategoryId);

        if (rst.next()) {
            return rst.getString(1);
        }

        return null;
    }
}
