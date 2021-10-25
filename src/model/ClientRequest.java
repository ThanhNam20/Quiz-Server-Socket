package model;

public class ClientRequest {
  private Integer code;
  private String data;

  public ClientRequest(Integer code, String data) {
    this.code = code;
    this.data = data;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

}
