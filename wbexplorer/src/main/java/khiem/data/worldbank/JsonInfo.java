package khiem.data.worldbank;

/**
 * This class represents the information about the data loaded from worldbank via webservices.
 * It contains some info about page number, total items, number of items per page, ...
 */
public class JsonInfo {
  String page;
  String pages;
  String perPage;
  String total;
  
  public String getPage() {
    return page;
  }
  public void setPage(String page) {
    this.page = page;
  }
  public String getPages() {
    return pages;
  }
  public void setPages(String pages) {
    this.pages = pages;
  }
  public String getPerPage() {
    return perPage;
  }
  public void setPerPage(String perPage) {
    this.perPage = perPage;
  }
  public String getTotal() {
    return total;
  }
  public void setTotal(String total) {
    this.total = total;
  }
  
  
}
