package com.vivek.orderitems.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.vivek.orderitems.domain.OrderList} entity.
 */
public class OrderListDTO implements Serializable {

    private Long id;

    private String productCode;

    private String productName;

    private Float quantity;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderListDTO orderListDTO = (OrderListDTO) o;
        if (orderListDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderListDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderListDTO{" +
            "id=" + getId() +
            ", productCode='" + getProductCode() + "'" +
            ", productName='" + getProductName() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}
