package com.nttdata.creditproducts.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;

import java.util.Objects;

/**
 * WithdrawRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2025-04-10T18:06:18.888891-05:00[America/Lima]")
public class WithdrawRequest {

  private Double monto;

  public WithdrawRequest monto(Double monto) {
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
    WithdrawRequest withdrawRequest = (WithdrawRequest) o;
    return Objects.equals(this.monto, withdrawRequest.monto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(monto);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WithdrawRequest {\n");
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

