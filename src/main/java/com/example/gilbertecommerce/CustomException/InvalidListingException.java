package com.example.gilbertecommerce.CustomException;

public class InvalidListingException extends RuntimeException {

  private String details;
  private final String source; //Definerer hvilken form, fejlen kommer fra. Vil vi bruge i thymeleaf til at vise fejlen det rigtige sted i formularen

  public InvalidListingException(String message, String details, String source) {
        super(message);
        this.details = details;
        this.source = source;
    }

    public String getDetails() {
      return details;
    }
    public String getSource() {
      return source;
    }
}
