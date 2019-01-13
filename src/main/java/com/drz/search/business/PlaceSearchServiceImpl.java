package com.drz.search.business;

import com.drz.PlaceGeoLocationRequest;
import com.drz.PlaceGeoLocationResponse;
import com.drz.PlaceGeoLocationServiceGrpc;
import com.drz.PlaceResponse;
import com.drz.search.persistence.entity.GeoPoint;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GRpcService
public class PlaceSearchServiceImpl extends PlaceGeoLocationServiceGrpc.PlaceGeoLocationServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceSearchServiceImpl.class);

    private SearchService searchService;

    @Autowired
    public PlaceSearchServiceImpl(SearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public void near(PlaceGeoLocationRequest request, StreamObserver<PlaceGeoLocationResponse> responseObserver) {
        LOGGER.info(request.toString());

        List<PlaceResponse> response = searchService.search(
                request.getFrom(),
                request.getSize(),
                new GeoPoint(request.getTopLeft().getLat(), request.getTopLeft().getLon()),
                new GeoPoint(request.getBottomRight().getLat(), request.getBottomRight().getLon()),
                request.getClientId()
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
