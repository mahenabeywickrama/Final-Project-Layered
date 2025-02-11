package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.custom.CategoryBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.custom.CategoryDAO;
import lk.ijse.gdse.pcstore.dto.CategoryDTO;
import lk.ijse.gdse.pcstore.dto.CustomerDTO;
import lk.ijse.gdse.pcstore.entity.Category;
import lk.ijse.gdse.pcstore.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryBOImpl implements CategoryBO {

    CategoryDAO categoryDAO = (CategoryDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.CATEGORY);

    @Override
    public String getNextId() throws SQLException {
        return categoryDAO.getNextId();
    }

    @Override
    public ArrayList<CategoryDTO> getAll() throws SQLException {
        ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
        ArrayList<Category> categories = categoryDAO.getAll();
        for (Category category : categories) {
            categoryDTOS.add(new CategoryDTO(category.getCategoryId(), category.getDescription()));
        }
        return categoryDTOS;
    }

    @Override
    public boolean save(CategoryDTO dto) throws SQLException {
        return categoryDAO.save(new Category(dto.getCategoryId(), dto.getDescription()));
    }

    @Override
    public boolean update(CategoryDTO dto) throws SQLException {
        return categoryDAO.update(new Category(dto.getCategoryId(), dto.getDescription()));
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return categoryDAO.delete(id);
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        return categoryDAO.getAllIds();
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        return categoryDAO.getAllNames();
    }

    @Override
    public CategoryDTO findById(String id) throws SQLException {
        Category category = categoryDAO.findById(id);
        return new CategoryDTO(category.getCategoryId(), category.getDescription());
    }

    @Override
    public CategoryDTO findByName(String name) throws SQLException {
        Category category = categoryDAO.findByName(name);
        return new CategoryDTO(category.getCategoryId(), category.getDescription());
    }
}
