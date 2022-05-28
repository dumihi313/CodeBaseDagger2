package vn.metamon.data.repository

import kotlinx.coroutines.flow.flow
import vn.metamon.data.model.KingDom
import vn.metamon.data.model.MetaResult
import vn.metamon.data.model.Metamon
import vn.metamon.data.service.MetamonService
import vn.metamon.utils.MetaFlow
import javax.inject.Inject

class MetamonRepositoryImpl @Inject constructor(
    private val service: MetamonService
) : MetamonRepository {
    override fun getKingDom(): MetaFlow<List<KingDom>> {
        return flow {
            listOf<KingDom>()
        }
//        return service.getKingDom()
    }

    override fun getMetamons(kingdomId: Int): MetaFlow<List<Metamon>> {
        return flow {
            emit(
                MetaResult.Success(
                    listOf(
                        Metamon(
                            1,
                            1,
                            "rare",
                            name = "BiBi",
                            cover = "https://www.pngall.com/wp-content/uploads/5/Cute-Monster-PNG-Image.png"
                        ),
                        Metamon(
                            2,
                            1,
                            "rare",
                            name = "MeoMeo",
                            cover = "https://www.pngall.com/wp-content/uploads/5/Real-Monster-PNG.png"
                        ),
                        Metamon(
                            3,
                            1,
                            "rare",
                            name = "BiBi",
                            cover = "https://www.pngall.com/wp-content/uploads/5/Cute-Monster-Transparent.png"
                        ),
                        Metamon(
                            4,
                            1,
                            "rare",
                            name = "LakaSi",
                            cover = "https://www.pngall.com/wp-content/uploads/5/Monster-PNG-Picture.png"
                        )
                    )
                )
            )

        }
//        return service.getMetamons(kingdomId)
    }
}