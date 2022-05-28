package vn.metamon.data.di

import dagger.Module
import dagger.Provides
import vn.metamon.core.di.RemoteHost
import vn.metamon.core.di.RemotePort
import vn.metamon.data.MetamonUserManager
import vn.metamon.data.local.UserStorage
import vn.metamon.data.model.ConnectionManager
import vn.metamon.data.model.GRpcConnectionManager
import vn.metamon.data.remote.DefaultGrpcInterceptor
import vn.metamon.data.remote.GRpcClient
import vn.metamon.data.repository.*
import vn.metamon.data.repository.SpaceRepository
import vn.metamon.data.repository.SpaceRepositoryImpl
import vn.metamon.data.service.*

@Module
class DataModule {
    @Provides
    @DataScoped
    fun provideGRpcClient(
        @RemoteHost host: String,
        @RemotePort port: Int?,
        interceptor: DefaultGrpcInterceptor
    ) = GRpcClient(host, port, true, interceptor)

    @Provides
    fun provideConnectionManager(gRpcClient: GRpcClient): ConnectionManager =
        GRpcConnectionManager(gRpcClient)

    @Provides
    fun provideUserManager(localStorage: UserStorage): MetamonUserManager {
        return MetamonUserManager.getInstance(localStorage)
    }

    @Provides
    fun providesSpaceService(grpcClient: GRpcClient): SpaceService = GrpcSpaceService(grpcClient)

    @Provides
    fun providesSpaceRepository(service: SpaceService): SpaceRepository = SpaceRepositoryImpl(service)

    @Provides
    fun providesMetamonService(grpcClient: GRpcClient): MetamonService = GrpcMetamonService(grpcClient)

    @Provides
    fun providesMetamonRepository(service: MetamonService): MetamonRepository = MetamonRepositoryImpl(service)

    @Provides
    fun providesPassportRepository(passportService: PassportService): PassportRepository =
        PassportRepositoryImpl(passportService)

    @Provides
    fun providesPassportService(client: GRpcClient): PassportService =
        GRpcPassportService(client)
}