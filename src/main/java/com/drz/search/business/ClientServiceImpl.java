package com.drz.search.business;

import com.drz.ClientRequest;
import com.drz.ClientResponse;
import com.drz.ClientServiceGrpc;
import com.drz.ClientsResponse;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GRpcService
public class ClientServiceImpl extends ClientServiceGrpc.ClientServiceImplBase {

    private SearchService searchService;

    @Autowired
    public ClientServiceImpl(SearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public void all(ClientRequest request, StreamObserver<ClientsResponse> responseObserver) {
        List<ClientResponse> response = searchService.getClients(Long.valueOf(request.getFrom()).intValue(), Long.valueOf(request.getSize()).intValue())
                .stream()
                .map(c -> {
                    return ClientResponse.newBuilder()
                            .setId(c.getId())
                            .setName(c.getName())
                            .setLat(c.getLatitude())
                            .setLon(c.getLongitude())
                            .build();
                })
                .collect(Collectors.toList());

        responseObserver.onNext(ClientsResponse.newBuilder().addAllData(response).build());
        responseObserver.onCompleted();
    }
}
