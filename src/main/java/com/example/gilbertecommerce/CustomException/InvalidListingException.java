package com.example.gilbertecommerce.CustomException;

public class InvalidListingException extends RuntimeException {

  private String details;
  private final String source; //Definerer hvilken form, fejlen kommer fra. Vil vi bruge i thymeleaf til at vise fejlen det rigtige sted i formularen
  private final String field;

  public InvalidListingException(String message, String field, String source) {
        super(message);
        this.field = field;
        this.source = source;
    }

    public String getField() {
    return field;
    }
    public String getSource() {
      return source;
    }
  public String getDetails() {
    return "Field: " + field + ", Origin: " + source;
  }
}
