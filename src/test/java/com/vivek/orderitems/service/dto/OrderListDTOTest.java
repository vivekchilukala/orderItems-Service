package com.vivek.orderitems.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vivek.orderitems.web.rest.TestUtil;

public class OrderListDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderListDTO.class);
        OrderListDTO orderListDTO1 = new OrderListDTO();
        orderListDTO1.setId(1L);
        OrderListDTO orderListDTO2 = new OrderListDTO();
        assertThat(orderListDTO1).isNotEqualTo(orderListDTO2);
        orderListDTO2.setId(orderListDTO1.getId());
        assertThat(orderListDTO1).isEqualTo(orderListDTO2);
        orderListDTO2.setId(2L);
        assertThat(orderListDTO1).isNotEqualTo(orderListDTO2);
        orderListDTO1.setId(null);
        assertThat(orderListDTO1).isNotEqualTo(orderListDTO2);
    }
}
