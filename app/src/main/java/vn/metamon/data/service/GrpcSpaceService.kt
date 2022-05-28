package vn.metamon.data.service

import vn.metamon.data.exception.ApiException
import vn.metamon.data.model.MetaResult
import vn.metamon.data.model.Space
import vn.metamon.data.model.SpaceUniverse
import vn.metamon.data.remote.GRpcClient
import vn.metamon.data.remote.isSuccess
import vn.metamon2.grpc.GetSpacesRequest
import vn.metamon2.grpc.GetUniSpacesRequest
import vn.metamon2.grpc.PublicGrpc
import vn.metamon.utils.MetaFlow
import javax.inject.Inject

class GrpcSpaceService @Inject constructor(
    gRpcClient: GRpcClient
) : BaseGRpcService(), SpaceService {

    private val apiStub = PublicGrpc.newBlockingStub(gRpcClient.channel)

    override fun getUniSpaces(): MetaFlow<List<SpaceUniverse>> {
        return execute {
            val request = GetUniSpacesRequest.getDefaultInstance()

            val response = apiStub.getUniSpaces(request)
            val status = response.status
            if (status.isSuccess()) {
                MetaResult.Success(response.uniSpaceList.map {
                    SpaceUniverse(id = it.id, name = it.name)
                })
            } else {
                MetaResult.Error(ApiException.wrap(status))
            }
        }
    }

    override fun getSpaces(uniSpaceId: Int): MetaFlow<List<Space>> {
        return execute {
            val request = GetSpacesRequest.newBuilder()
                .setUniId(uniSpaceId)
                .build()

            val response = apiStub.getSpaces(request)
            val status = response.status
            if (status.isSuccess()) {
                MetaResult.Success(response.spaceList.map {
                    Space(
                        id = it.spaceId,
                        uniSpaceId = it.uniSpaceId,
                        name = it.name,
                        cover = it.cover,
                        rarity = it.rarity,
                        totalViewer = it.totalView,
                        totalShare = it.totalShare,
                        totalHeart = it.totalHeart,
                        totalDonate = it.totalDonate,
                        totalSubscriber = it.totalSubsciber
                    )
                })
            } else {
                MetaResult.Error(ApiException.wrap(status))
            }
        }
    }
}