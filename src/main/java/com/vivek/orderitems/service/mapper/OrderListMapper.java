package com.vivek.orderitems.service.mapper;

import com.vivek.orderitems.domain.*;
import com.vivek.orderitems.service.dto.OrderListDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderList} and its DTO {@link OrderListDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderListMapper extends EntityMapper<OrderListDTO, OrderList> {



    default OrderList fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderList orderList = new OrderList();
        orderList.setId(id);
        return orderList;
    }
}
