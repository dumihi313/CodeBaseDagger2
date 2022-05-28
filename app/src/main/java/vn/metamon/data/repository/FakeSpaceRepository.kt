package vn.metamon.data.repository

import kotlinx.coroutines.flow.flow
import vn.metamon.data.model.MetaResult
import vn.metamon.data.model.Space
import vn.metamon.data.model.SpaceUniverse
import vn.metamon.utils.MetaFlow

class FakeSpaceRepository : SpaceRepository {
    override fun getUniSpaces(): MetaFlow<List<SpaceUniverse>> {
        return flow {
            emit(
                MetaResult.Success(
                    listOf(
                        SpaceUniverse(1, "Aries"),
                        SpaceUniverse(2, "Taurus"),
                        SpaceUniverse(3, "Phang"),
                        SpaceUniverse(4, "Phang 1")
                    )
                )
            )
        }
    }

    override fun getSpaces(uniSpaceId: Int): MetaFlow<List<Space>> {
        return flow {
            val space1 = listOf(
                Space(1, 1,"Aries 1", "", "",12,),
                Space(3, 1, "Aries 3","", "",0),
                Space(10,1, "Aries 10","", "",831),
            )
            val space2 = listOf(
                Space(2, 2,"Aries 2","", "",121),
                Space(7,2, "Aries 7","", "", 465)
            )
            val space3 = listOf(
                Space(5, 3,"Aries 5","", "",0),
                Space(9,3, "Aries 9","", "", 0)
            )
            val space4 = listOf(
                Space(4, 4,"Aries 4","", "",1300),
                Space(6,4, "Aries 6","", "", 1567),
                Space(8,4, "Aries 8","", "",0)
            )
            emit(
                MetaResult.Success(
                    when(uniSpaceId) {
                        2 -> space2
                        3 -> space3
                        4 -> space4
                        else -> space1
                    }
                )
            )
        }
    }
}