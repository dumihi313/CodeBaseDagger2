package vn.metamon.data.repository

import vn.metamon.data.model.KingDom
import vn.metamon.data.model.Metamon
import vn.metamon.utils.MetaFlow

interface MetamonRepository  {
    fun getKingDom(): MetaFlow<List<KingDom>>
    fun getMetamons(kingdomId: Int): MetaFlow<List<Metamon>>
}