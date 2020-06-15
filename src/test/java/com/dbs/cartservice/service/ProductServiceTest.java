package com.dbs.cartservice.service;

import com.dbs.cartservice.dto.ProductDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private Queue queue;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldPublishDataSuccessfullyIntoQueue() {
        doNothing().when(jmsTemplate).convertAndSend(isA(Queue.class), isA(String.class));
        ProductDetails productDetails = ProductDetails.builder()
                                                      .name("Dettol")
                                                      .description("Hand santi")
                                                      .price(new BigDecimal("10.0"))
                                                      .build();

        List<ProductDetails> details  = productService.addProduct(Arrays.asList(productDetails));

        assertEquals(1, details.size());
    }

}
