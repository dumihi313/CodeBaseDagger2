package vn.metamon.data.service

import vn.metamon.data.model.Space
import vn.metamon.data.model.SpaceUniverse
import vn.metamon.utils.MetaFlow

interface SpaceService {
    fun getUniSpaces(): MetaFlow<List<SpaceUniverse>>
    fun getSpaces(uniSpaceId: Int): MetaFlow<List<Space>>
}