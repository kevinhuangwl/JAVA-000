package io.kimmking.rpcfx.demo.api;

import io.kimmking.rpcfx.Rpc;

@Rpc
public interface OrderService {

    Order findOrderById(int id);

}
