package vn.metamon.data.service

import vn.metamon2.grpc.GetKingdomRequest
import vn.metamon2.grpc.GetMetamonRequest
import vn.metamon2.grpc.PublicGrpc
import vn.metamon.data.exception.ApiException
import vn.metamon.data.remote.GRpcClient
import vn.metamon.data.remote.isSuccess
import vn.metamon.data.model.*
import vn.metamon.utils.MetaFlow
import javax.inject.Inject

class GrpcMetamonService @Inject constructor(
    gRpcClient: GRpcClient
) : BaseGRpcService(), MetamonService {

    private val apiStub = PublicGrpc.newBlockingStub(gRpcClient.channel)

    override fun getKingDom(): MetaFlow<List<KingDom>> {
        return execute {
            val request = GetKingdomRequest.getDefaultInstance()

            val response = apiStub.getKingdom(request)
            val status = response.status
            if (status.isSuccess()) {
                MetaResult.Success(response.kingdomsList.map {
                    KingDom(id = it.id, name = it.name)
                })
            } else {
                MetaResult.Error(ApiException.wrap(status))
            }
            MetaResult.Error(Throwable("x"))
        }
    }

    override fun getMetamons(kingdomId: Int): MetaFlow<List<Metamon>> {
        return execute {
            val request = GetMetamonRequest.newBuilder()
                .setKingdomId(kingdomId)
                .build()

            val response = apiStub.getMetamon(request)
            val status = response.status
            if (status.isSuccess()) {
                MetaResult.Success(response.metamonList.map {
                    Metamon(
                        id = it.monId,
                        kingdomId = it.kingdomId,
                        name = it.name,
                        rarity = it.rarity,
                    )
                })
            } else {
                MetaResult.Error(ApiException.wrap(status))
            }
        }
    }
}