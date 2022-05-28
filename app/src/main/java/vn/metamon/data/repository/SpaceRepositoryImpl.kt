package vn.metamon.data.repository

import vn.metamon.data.model.Space
import vn.metamon.data.model.SpaceUniverse
import vn.metamon.data.service.SpaceService
import vn.metamon.utils.MetaFlow
import javax.inject.Inject

class SpaceRepositoryImpl @Inject constructor(
    private val spaceService: SpaceService
): SpaceRepository {
    override fun getUniSpaces(): MetaFlow<List<SpaceUniverse>> {
        return spaceService.getUniSpaces()
    }

    override fun getSpaces(uniSpaceId: Int): MetaFlow<List<Space>> {
        return spaceService.getSpaces(uniSpaceId)
    }
}