package khiem.dataprj;

import khiem.dataprj.XBRLDatastore.MySQLAccess;

public class MyTest {

  public void testCompanyInsert() {
    try {
      MySQLAccess mySQL = new MySQLAccess();
      int id = mySQL.storeCompany("1234", "ABCD", "test company");
      System.out.println(id);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
