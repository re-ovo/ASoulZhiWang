package me.rerere.zhiwang.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.rerere.zhiwang.api.wiki.WikiUtil
import javax.inject.Inject

class WikiRepo @Inject constructor(
    private val wikiUtil: WikiUtil
) {
    suspend fun loadWiki() = withContext(Dispatchers.IO) { wikiUtil.loadWiki() }
}