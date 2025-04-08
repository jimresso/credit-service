package com.nttdata.creditproducts.service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;

import java.util.Objects;

/**
 * TaxedTransactionLimit
 */

@JsonTypeName("taxedTransactionLimit")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-04-07T18:03:11.536817500-05:00[America/Lima]")
public class TaxedTransactionLimit {

  private String id;

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

  private Double monto;

  public TaxedTransactionLimit id(String id) {
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

  public TaxedTransactionLimit accountType(AccountTypeEnum accountType) {
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

  public TaxedTransactionLimit monto(Double monto) {
    this.monto = monto;
    return this;
  }

  /**
   * Get monto
   * @return monto
  */
  
  @Schema(name = "monto", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("monto")
  public Double getMonto() {
    return monto;
  }

  public void setMonto(Double monto) {
    this.monto = monto;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TaxedTransactionLimit taxedTransactionLimit = (TaxedTransactionLimit) o;
    return Objects.equals(this.id, taxedTransactionLimit.id) &&
        Objects.equals(this.accountType, taxedTransactionLimit.accountType) &&
        Objects.equals(this.monto, taxedTransactionLimit.monto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, accountType, monto);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TaxedTransactionLimit {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
    sb.append("    monto: ").append(toIndentedString(monto)).append("\n");
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

