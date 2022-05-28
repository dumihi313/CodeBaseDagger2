package vn.metamon.data.service

import vn.metamon.data.model.KingDom
import vn.metamon.data.model.Metamon
import vn.metamon.utils.MetaFlow

interface MetamonService {
    fun getKingDom(): MetaFlow<List<KingDom>>
    fun getMetamons(kingdomId: Int): MetaFlow<List<Metamon>>
}