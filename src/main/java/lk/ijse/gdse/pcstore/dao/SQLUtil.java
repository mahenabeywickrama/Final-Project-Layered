package lk.ijse.gdse.pcstore.dao;

import lk.ijse.gdse.pcstore.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLUtil {
    public static <T>T execute(String sql,Object... obj) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pst = connection.prepareStatement(sql);

        for (int i=0;i<obj.length;i++){
            pst.setObject((i+1),obj[i]);
        }

        if (sql.startsWith("select") || sql.startsWith("SELECT")){
            return (T) pst.executeQuery();
        } else {
            return (T) ((Boolean) (pst.executeUpdate() > 0));
        }
    }
}
