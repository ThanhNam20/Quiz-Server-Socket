package service;

import controller.DBConnection;
import controller.DBQuery;

public class DBservice {
  protected QueryBuilder queryBuilder;
  protected DBConnection dbConnection;
  protected DBQuery dbq;

  public DBservice() {
    this.dbConnection = DBConnection.getInstance();
    this.dbq = new DBQuery(this.dbConnection.getConnection());
    this.queryBuilder = new QueryBuilder();
  }

}
