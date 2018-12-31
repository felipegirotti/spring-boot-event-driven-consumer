package com.drz.search.business;

import com.drz.PlaceGeoLocationRequest;
import com.drz.PlaceGeoLocationResponse;
import com.drz.PlaceGeoLocationServiceGrpc;
import com.drz.PlaceResponse;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GRpcService
public class PlaceSearchServiceImpl extends PlaceGeoLocationServiceGrpc.PlaceGeoLocationServiceImplBase {

    private SearchService searchService;

    @Autowired
    public PlaceSearchServiceImpl(SearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public void near(PlaceGeoLocationRequest request, StreamObserver<PlaceGeoLocationResponse> responseObserver) {

        System.out.println(request);

        List<PlaceResponse> response = searchService.search(
                0,
                10,
                request.getLat(),
                request.getLon(),
                request.getClientId(),
                request.getDistance()
        )
                .stream()
                .map(p -> {
                    return PlaceResponse.newBuilder()
//                            .setClientId(p.getClientId())
                            .setLat(p.getLatitude())
                            .setLon(p.getLongitude())
                            .setName(p.getName())
                            .build();
                })
                .collect(Collectors.toList());

        responseObserver.onNext(PlaceGeoLocationResponse.newBuilder().addAllData(response).build());

        responseObserver.onCompleted();
    }
}
