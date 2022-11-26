package com.irlab.testappkotlin.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class CoolenjoyPagingSource(
    private val apiService: CoolenjoyService
) : PagingSource<Int, ItemModel>() {
    override fun getRefreshKey(state: PagingState<Int, ItemModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemModel> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE_INDEX

            val response = apiService.getDataFromAPI(nextPage)
            val responseHtml = response.body()!!.string()
            val jsoupParse = Jsoup.parse(responseHtml)
            val responseData = jsoupGetData(jsoupParse, nextPage)

            LoadResult.Page(
                data = responseData,
                prevKey = null,
                nextKey = nextPage + 1
            )
        }catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }

    // 댓글 수를 판별 후, 0이면 0 표시
    private fun commentZeroOrNot(comment: String): String {
        return if (comment == "" || comment == null) "0"
        else comment.replace("["," ").replace("]","")
    }

    // 블라인드 처리된 제목일 때
    private fun titleNullOrNot(title: String): String {
        return if (title == "" || title == null || title == " ") "블라인드 처리된 글입니다."
        else title
    }

    // 시간이 아닌 날짜일 때
    private fun timeOrNot(date: String): String {
        return if(date.contains("-")) date.substring(3)
        else date
    }

    private fun jsoupGetData(doc: Document, page: Int): MutableList<ItemModel> {
        val list = mutableListOf<ItemModel>()
        val itemList = doc.select("body div div div form div table tbody tr") // 아이템 리스트 가져오기

        if(page == 1) {
            for (i in 1..24) {
                val title =
                    itemList[i].select(".td_subject a").textNodes().first().text()
                val category = itemList[i].select(".td_num").text()
                val comment = itemList[i].select(".td_subject a .cnt_cmt").text()
                val date = itemList[i].select(".td_date").text()
                val like = itemList[i].select(".td_hit .list_good2").first()!!.text()
                val itemUrl = itemList[i].select(".td_subject a")
                    .attr("href")
                list.add(
                    ItemModel(
                        titleNullOrNot(title),
                        "쿨엔조이 | ",
                        timeOrNot(date),
                        category,
                        "♡  $like",
                        "\uD83D\uDCAC  " + commentZeroOrNot(comment),
                        "",
                        itemUrl
                    )
                )
            }
        }else {
            for (i in 0..24) {
                val title =
                    itemList[i].select(".td_subject a").textNodes().first().text()
                val category = itemList[i].select(".td_num").text()
                val comment = itemList[i].select(".td_subject a .cnt_cmt").text()
                val date = itemList[i].select(".td_date").text()
                val like = itemList[i].select(".td_hit .list_good2").first()!!.text()
                val itemUrl = itemList[i].select(".td_subject a")
                    .attr("href")
                list.add(
                    ItemModel(
                        titleNullOrNot(title),
                        "쿨엔조이 · ",
                        timeOrNot(date),
                        category,
                        "♡  $like",
                        "\uD83D\uDCAC  " + commentZeroOrNot(comment),
                        "",
                        itemUrl
                    )
                )
            }
        }
        return list
    }
}