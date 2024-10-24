package dk.via.sep.server;

import dk.via.sep.HouseOwner;
import dk.via.sep.HouseOwnerRequest;
import dk.via.sep.HouseOwnerResponse;
import dk.via.sep.HouseOwnerServiceGrpc;
import io.grpc.stub.StreamObserver;

public class HousePalServiceImpl extends
    HouseOwnerServiceGrpc.HouseOwnerServiceImplBase
{
 // private HouseOwner owners;

public HousePalServiceImpl(HouseOwnerRequest request, StreamObserver<HouseOwnerResponse> responseObserver){


}

}
