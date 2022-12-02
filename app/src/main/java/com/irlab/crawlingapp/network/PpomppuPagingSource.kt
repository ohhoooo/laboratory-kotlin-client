package com.irlab.crawlingapp.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class PpomppuPagingSource(
    private val apiService: PpomppuService
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

            val response = apiService.getDataFromAPI("ppomppu",nextPage,75)
            val responseHtml = response.body()!!.string()
            val jsoupParse = Jsoup.parse(responseHtml)
            val responseData = jsoupGetData(jsoupParse, nextPage)

            LoadResult.Page(
                data = responseData,
                prevKey = null,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }

    // 댓글 수를 판별 후, 0이면 0 표시
    private fun commentZeroOrNot(comment: String): String {
        return if (comment == "") "0"
        else comment
    }

    // "-" 를 포함하지 않으면 0을 반환, 그렇지 않으면 다듬어진 좋아요 개수를 반환
    private fun likeZeroOrNot(like: String): String {
        return if (!like.contains("-")) "0"
        else like.substring(13,15)
    }

    // 시간이나 날짜를 판단하여 다르게 표기
    private fun timeOrDate(date: String): String {
        return if(date.contains(":")) date.substring(30,35)
        else date.substring(33,38).replace("/","-")
    }

    private fun jsoupGetData(doc: Document, page: Int): MutableList<ItemModel> {
        val list = mutableListOf<ItemModel>()
        val baseUrl = "https://www.ppomppu.co.kr/zboard/"
        val itemList = doc.select(".wrapper .contents .container .info_bg tbody tr")

        if(page == 1) {
            for(i in 6..63 step 3) {
                val title = itemList[i].select("table tbody tr div a font").text()
                val category = itemList[i].select("td table tbody tr td div span")
                    .last()
                    .toString()
                    .substring(42)
                    .replace("]","")
                    .replace("</span>", "")
                val comment = itemList[i].select(".list_comment2 span").text()
                val date = itemList[i].select("td nobr").last().toString()
                val like = itemList[i].select("td").textNodes().toString()
                val itemImage = itemList[i].select("img")
                    .attr("src")
                val itemUrl = itemList[i].select("tbody tr td a")
                    .attr("href")
                list.add(
                    ItemModel(
                        title,
                        "뽐뿌 · ",
                        timeOrDate(date),
                        category,
                        "♡  " + likeZeroOrNot(like),
                        "\uD83D\uDCAC  " + commentZeroOrNot(comment),
                        "https:$itemImage",
                        baseUrl + itemUrl
                    )
                )
            }
        }else{
            for(i in 3..60 step 3) {
                val title = itemList[i].select("table tbody tr div a font").text()
                val category = itemList[i].select("td table tbody tr td div span")
                    .last()
                    .toString()
                    .substring(42)
                    .replace("]","")
                    .replace("</span>", "")
                val comment = itemList[i].select(".list_comment2 span").text()
                val date = itemList[i].select("td nobr").last().toString()
                val like = itemList[i].select("td").textNodes().toString()
                val itemImage = itemList[i].select("img")
                    .attr("src")
                val itemUrl = itemList[i].select("tbody tr td a")
                    .attr("href")
                list.add(
                    ItemModel(
                        title,
                        "뽐뿌 · ",
                        timeOrDate(date),
                        category,
                        "♡  " + likeZeroOrNot(like),
                        "\uD83D\uDCAC  " + commentZeroOrNot(comment),
                        "https:$itemImage",
                        baseUrl + itemUrl
                    )
                )
            }
        }
        return list
    }
}