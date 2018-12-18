package com.drz.search.business;

import com.drz.PlaceGeoLocationRequest;
import com.drz.PlaceGeoLocationResponse;
import com.drz.PlaceGeoLocationServiceGrpc;
import com.drz.PlaceResponse;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class PlaceSearchServiceImpl extends PlaceGeoLocationServiceGrpc.PlaceGeoLocationServiceImplBase {

    private SearchService searchService;

    @Autowired
    public PlaceSearchServiceImpl(SearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public void near(PlaceGeoLocationRequest request, StreamObserver<PlaceGeoLocationResponse> responseObserver) {

        PlaceResponse placeResponse = PlaceResponse.newBuilder()
                .setClientId(1)
                .setLat(1D)
                .setLon(2D)
                .setName("Name")
                .build();
        System.out.println(request);
        responseObserver.onNext(PlaceGeoLocationResponse.newBuilder().addData(placeResponse).build());

        responseObserver.onCompleted();
    }
}
