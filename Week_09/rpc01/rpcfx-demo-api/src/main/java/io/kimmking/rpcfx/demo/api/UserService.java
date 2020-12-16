package io.kimmking.rpcfx.demo.api;

import io.kimmking.rpcfx.Rpc;

@Rpc
public interface UserService {

    User findById(int id);

}
