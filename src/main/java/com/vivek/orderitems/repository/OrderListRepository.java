package com.vivek.orderitems.repository;

import com.vivek.orderitems.domain.OrderList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the OrderList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderListRepository extends JpaRepository<OrderList, Long> {

    OrderList findByProductCode(String sku);

}
