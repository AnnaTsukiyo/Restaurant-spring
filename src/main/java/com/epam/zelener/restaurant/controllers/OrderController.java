package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.ManagerRequestDto;
import com.epam.zelener.restaurant.dtos.OrderDto;
import com.epam.zelener.restaurant.exceptions.UserNotFoundSuchElementException;
import com.epam.zelener.restaurant.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@Log4j2
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create a new order")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "A new order is created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data is provided. Order is not created.", content = @Content)})
    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object Order is to be created")
                                              @RequestBody @Valid OrderDto orderDto) {
        log.info("Request to create a new Order :{}", orderDto);
        orderService.createAnOrder(orderDto);
        return new ResponseEntity<>(orderDto.getOrderId() + " -- A new order with id {} is created", HttpStatus.OK);
    }


    @Operation(summary = "Get all orders")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found all orders in database successfully"),
            @ApiResponse(responseCode = "404", description = "No orders are found. No orders data!")})
    @GetMapping("/all")
    public ResponseEntity<Object> findAllOrders() {
        log.info("Request to find all OrderDto :");
        List<OrderDto> orders = orderService.showAllOrder();
        if (!orders.isEmpty()) {
            log.info("Orders are found successfully");
            return new ResponseEntity<>("Found all orders in database successfully", HttpStatus.OK);
        } else {
            log.error("No orders are found! No orders data!");
            return new ResponseEntity<>("No orders data!", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a order by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the order successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ManagerRequestDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id provided", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order is not found", content = @Content)})

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable String id) {
        try {
            if (orderService.findOrderById(Long.valueOf(id)) == null) {
                log.warn("There is no order with a given id : {} !", id);
                return new ResponseEntity<>(id + " –- Invalid id provided.", HttpStatus.BAD_REQUEST);
            } else {
                orderService.findOrderById(Long.valueOf(id));
                log.info("Request to get a OrderDto by the id :{}", id);
                return new ResponseEntity<>(id + " -- Order with a given id {} is found successfully", HttpStatus.OK);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(id + " -- Order with such id {} doesn't exist ", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update order status by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "The order status is updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid id is  provided"),
            @ApiResponse(responseCode = "404", description = "Order is not found")})

    @PatchMapping(value = "/{name}")
    public ResponseEntity<Object> updateOrder(@Valid @RequestBody OrderDto orderDto, @PathVariable String id) {
        try {
            orderService.updateOrder(orderDto, Long.parseLong(id));
            log.info("Request to update a Order Status with order id:{}", id);
            return new ResponseEntity<>(orderDto.getOrderId() + "Order status with order id {} is updated successfully", HttpStatus.OK);
        } catch (UserNotFoundSuchElementException e) {
            return new ResponseEntity<>(orderDto.getOrderId() + " --Order with such id doesn't exist ", HttpStatus.NOT_FOUND);
        }
    }
}
