package com.vivek.orderitems.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class OrderListMapperTest {

    private OrderListMapper orderListMapper;

    @BeforeEach
    public void setUp() {
        orderListMapper = new OrderListMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(orderListMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(orderListMapper.fromId(null)).isNull();
    }
}
