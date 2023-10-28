package POMtests;

import base.TestUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductPage;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LoginTests extends TestUtil {
    @Test
    public void successfulLogin() {
        LoginPage loginPage = new LoginPage(driver);
        ProductPage productPage = loginPage.login("standard_user", "secret_sauce");

        Assert.assertTrue(productPage.isAT());
    }

    @Test
    public void unSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_userw", "secret_sauwce");

        Assert.assertTrue(loginPage.isAT());
    }

    @Test (dataProvider = "wrongUsers")
    public void unSuccessfulLogin1(String userName, String password) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(userName, password);

        Assert.assertTrue(loginPage.isAT());
    }

    @Test(dataProvider = "validUsersFromCsv")
    public void successfulLogin(String userName, String password) {
        LoginPage loginPage = new LoginPage(driver);
        ProductPage productPage = loginPage.login(userName, password);

        Assert.assertTrue(productPage.isAT());
    }

    @DataProvider(name = "wrongUsers")
    public Object[] [] getWrongUsers() {
        return new Object[][]{
                {"wrongUsername", "secret_sauce"},
                {"standard_user", "wrongPassword"},
                {"wrongUsername", "wrongPassword"}
        };
    }

    @DataProvider(name = "validUsersFromCsv")
    public Object[][] readValidUsersFromCsv(){
        try {
            CSVReader csvReader = new CSVReader(new FileReader("src/test/resources/correctUsers.csv"));
            List<String[]> csvData = csvReader.readAll();
            Object[] [] csvDataObj = new Object[csvData.size()][2];

            for (int i = 0; i < csvData.size(); i++) {
                csvDataObj[i] = csvData.get(i);
            }
            return csvDataObj ;
        } catch (IOException e) {
            System.out.println("Not possible to find the CSV file");
            return null;
        } catch (CsvException e) {
            System.out.println("Not possible to work with the CSV file");
            return null;
        }
    }
}