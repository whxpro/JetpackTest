package com.whx.jetpacktest.coroutines

import com.whx.jetpacktest.viewmodel.Meizi

class MeiziUseCase(private val mzRepo: ReposCoro) {

    suspend operator fun invoke(page: String): List<Meizi> {
        val mapList = mzRepo.fetchImageData(page)

        return mapList.map { Meizi(it["who"].toString(), it["url"].toString(), false) }
    }
}