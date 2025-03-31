package com.nttdata.creditproducts.service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Account
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-03-30T12:02:01.431172500-05:00[America/Lima]")
public class Account {

  private String id;

  private String customerId;

  /**
   * Gets or Sets customerType
   */
  public enum CustomerTypeEnum {
    PERSONAL("PERSONAL"),
    
    EMPRESARIAL("EMPRESARIAL");

    private String value;

    CustomerTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CustomerTypeEnum fromValue(String value) {
      for (CustomerTypeEnum b : CustomerTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CustomerTypeEnum customerType;

  /**
   * Gets or Sets accountType
   */
  public enum AccountTypeEnum {
    AHORRO("AHORRO"),
    
    CORRIENTE("CORRIENTE"),
    
    PLAZO_FIJO("PLAZO_FIJO");

    private String value;

    AccountTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AccountTypeEnum fromValue(String value) {
      for (AccountTypeEnum b : AccountTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private AccountTypeEnum accountType;

  private Double balance;

  private Integer monthlyLimit;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate lastDepositDate;

  @Valid
  private List<String> holders;

  public Account id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Account customerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * Get customerId
   * @return customerId
  */
  
  @Schema(name = "customerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("customerId")
  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public Account customerType(CustomerTypeEnum customerType) {
    this.customerType = customerType;
    return this;
  }

  /**
   * Get customerType
   * @return customerType
  */
  
  @Schema(name = "customerType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("customerType")
  public CustomerTypeEnum getCustomerType() {
    return customerType;
  }

  public void setCustomerType(CustomerTypeEnum customerType) {
    this.customerType = customerType;
  }

  public Account accountType(AccountTypeEnum accountType) {
    this.accountType = accountType;
    return this;
  }

  /**
   * Get accountType
   * @return accountType
  */
  
  @Schema(name = "accountType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accountType")
  public AccountTypeEnum getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountTypeEnum accountType) {
    this.accountType = accountType;
  }

  public Account balance(Double balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Get balance
   * @return balance
  */
  
  @Schema(name = "balance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("balance")
  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  public Account monthlyLimit(Integer monthlyLimit) {
    this.monthlyLimit = monthlyLimit;
    return this;
  }

  /**
   * Only applicable for CORRIENTE accounts
   * @return monthlyLimit
  */
  
  @Schema(name = "monthlyLimit", description = "Only applicable for CORRIENTE accounts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("monthlyLimit")
  public Integer getMonthlyLimit() {
    return monthlyLimit;
  }

  public void setMonthlyLimit(Integer monthlyLimit) {
    this.monthlyLimit = monthlyLimit;
  }

  public Account lastDepositDate(LocalDate lastDepositDate) {
    this.lastDepositDate = lastDepositDate;
    return this;
  }

  /**
   * Only applicable for PLAZO_FIJO accounts
   * @return lastDepositDate
  */
  @Valid 
  @Schema(name = "lastDepositDate", description = "Only applicable for PLAZO_FIJO accounts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastDepositDate")
  public LocalDate getLastDepositDate() {
    return lastDepositDate;
  }

  public void setLastDepositDate(LocalDate lastDepositDate) {
    this.lastDepositDate = lastDepositDate;
  }

  public Account holders(List<String> holders) {
    this.holders = holders;
    return this;
  }

  public Account addHoldersItem(String holdersItem) {
    if (this.holders == null) {
      this.holders = new ArrayList<>();
    }
    this.holders.add(holdersItem);
    return this;
  }

  /**
   * Get holders
   * @return holders
  */
  
  @Schema(name = "holders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("holders")
  public List<String> getHolders() {
    return holders;
  }

  public void setHolders(List<String> holders) {
    this.holders = holders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Account account = (Account) o;
    return Objects.equals(this.id, account.id) &&
        Objects.equals(this.customerId, account.customerId) &&
        Objects.equals(this.customerType, account.customerType) &&
        Objects.equals(this.accountType, account.accountType) &&
        Objects.equals(this.balance, account.balance) &&
        Objects.equals(this.monthlyLimit, account.monthlyLimit) &&
        Objects.equals(this.lastDepositDate, account.lastDepositDate) &&
        Objects.equals(this.holders, account.holders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, customerId, customerType, accountType, balance, monthlyLimit, lastDepositDate, holders);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Account {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    customerType: ").append(toIndentedString(customerType)).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    monthlyLimit: ").append(toIndentedString(monthlyLimit)).append("\n");
    sb.append("    lastDepositDate: ").append(toIndentedString(lastDepositDate)).append("\n");
    sb.append("    holders: ").append(toIndentedString(holders)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

