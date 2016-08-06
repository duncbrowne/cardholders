package com.accessone.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Current Project cardholders
 * Created by duncan.browne on 06/08/2016.
 */
public class MariaDBFacade
{
    private Connection m_DBConnection;


    public MariaDBFacade() throws java.sql.SQLException
    {
        m_DBConnection = DriverManager.getConnection(
                "jdbc:mariadb://localhost/projecttitan",
                "root",
                "root");
    }

    public boolean GetCardholderRecord(int nCardholderID, String strCardholderRecordJSON)
    {
        Statement stmt = null;
        ResultSet rs = null;

        try
        {
            stmt = m_DBConnection.createStatement();

            // or alternatively, if you don't know ahead of time that
            // the query will be a SELECT...

            if (stmt.execute("SELECT FirstName,Surname FROM cardholders WHERE CardholderID = " + nCardholderID))
            {
                rs = stmt.getResultSet();

                while (rs.next())
                {
                    String FirstName = rs.getString("FirstName");
                    String Surname = rs.getString("Surname");
                    System.out.println("First name " + FirstName + " Surname " + Surname);
                }
            }
            return true;
        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        finally
        {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException sqlEx) {} // ignore

                rs = null;
            }

            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException sqlEx) { } // ignore

                stmt = null;
            }
        }
    }
}
