package ie.futurecollars.invoicing.model;

import lombok.Data;

@Data
public class Company {

  private String taxIdentification;
  private String address;
  private String name;

  public Company(String taxIdentification, String address, String name) {

    this.taxIdentification = taxIdentification;
    this.address = address;
    this.name = name;
  }
}
