package com.accessone.Database;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
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
    private static String CONNECTION_STRING = "jdbc:mariadb://localhost/projecttitan";
    private static String CONNECTION_USERNAME = "root";
    private static String CONNECTION_PASSWORD = "root";

    private static MariaDBFacade ourInstance = new MariaDBFacade();

    public static MariaDBFacade getInstance()
    {
        return ourInstance;
    }

    public MariaDBFacade()
    {
        CreateConnection();
    }

    private boolean CreateConnection()
    {
        try
        {
            m_DBConnection = DriverManager.getConnection(
                    CONNECTION_STRING,
                    CONNECTION_USERNAME,
                    CONNECTION_PASSWORD);

            return true;
        }
        catch (SQLException e)
        {
            System.err.println("Unable to create connection : " + CONNECTION_STRING + " with username/password : " +
            CONNECTION_USERNAME + "//" +CONNECTION_PASSWORD);
            return false;
        }
    }

    public JsonObject GetCardholderRecord(int nCardholderID)
    {
        Statement sqlStatement = null;
        ResultSet resultSet = null;

        try
        {
            sqlStatement = m_DBConnection.createStatement();

            // or alternatively, if you don't know ahead of time that
            // the query will be a SELECT...

            if (sqlStatement.execute("SELECT * FROM cardholders WHERE CardholderID = " + nCardholderID))
            {
                resultSet = sqlStatement.getResultSet();

                if (resultSet.next())
                {
                    JsonBuilderFactory factory = Json.createBuilderFactory(null);

                    String strEmployeeNumber = resultSet.getString("EmployeeNumber");
                    if (strEmployeeNumber == null)
                        strEmployeeNumber = new String();

                    String strDepartmentID = resultSet.getString("DepartmentID");
                    if (strDepartmentID == null)
                        strDepartmentID = new String();

                    String strEmailAddress = resultSet.getString("EmailAddress");
                    if (strEmailAddress == null)
                        strEmailAddress = new String();

                    JsonObject joRecordDetails = factory.createObjectBuilder()
                            .add("cardholderid", resultSet.getString("CardholderID"))
                            .add("title", resultSet.getString("Title"))
                            .add("firstname", resultSet.getString("FirstName"))
                            .add("surname", resultSet.getString("Surname"))
                            .add("employeenumber", strEmployeeNumber)
                            .add("departmentid", strDepartmentID)
                            .add("emailaddress", strEmailAddress)
                            .build();

                    System.out.println("Cardholder message body : " + joRecordDetails.toString());
                    return joRecordDetails;
                }
            }
        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
        finally
        {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (resultSet != null)
            {
                try
                {
                    resultSet.close();
                }
                catch (SQLException sqlEx) {} // ignore

                resultSet = null;
            }

            if (sqlStatement != null)
            {
                try
                {
                    sqlStatement.close();
                }
                catch (SQLException sqlEx) { } // ignore

                sqlStatement = null;
            }
        }
        return null;
    }
}
