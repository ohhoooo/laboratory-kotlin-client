package com.irlab.crawlingapp.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class TotalPagingSource(
    private val apiService1: QuasarzoneService,
    private val apiService2: PpomppuService,
    private val apiService3: CoolenjoyService
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

            val response1 = apiService1.getDataFromAPI(nextPage)
            val response2 = apiService2.getDataFromAPI("ppomppu",nextPage,75)
            val response3 = apiService3.getDataFromAPI(nextPage)

            val responseHtml1 = response1.body()!!.string()
            val responseHtml2 = response2.body()!!.string()
            val responseHtml3 = response3.body()!!.string()

            val jsoupParse1 = Jsoup.parse(responseHtml1)
            val jsoupParse2 = Jsoup.parse(responseHtml2)
            val jsoupParse3 = Jsoup.parse(responseHtml3)

            val responseData = jsoupGetData(jsoupParse1, jsoupParse2, jsoupParse3, nextPage)

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

    // (퀘이사존) 블라인드 처리된 제목일 때
    private fun titleNullOrNot(title: String): String {
        return if (title == "") "블라인드 처리된 글입니다."
        else title
    }

    // (퀘이사존, 뽐뿌)댓글 수를 판별 후, 0이면 0 표시
    private fun commentZeroOrNot(comment: String): String {
        return if (comment == "") "0"
        else comment
    }

    // (뽐뿌) "-" 를 포함하지 않으면 0을 반환, 그렇지 않으면 다듬어진 좋아요 개수를 반환
    private fun likeZeroOrNot(like: String): String {
        return if (!like.contains("-")) "0"
        else like.substring(13,15)
    }

    // (뽐뿌) 시간이나 날짜를 판단하여 다르게 표기
    private fun timeOrDatePpomppu(date: String): String {
        return if(date.contains(":")) date.substring(30,35)
        else date.substring(33,38).replace("/","-")
    }

    // (쿨엔조이) 시간이 아닌 날짜일 때
    private fun timeOrNotCoolenjoy(date: String): String {
        return if(date.contains("-")) date.substring(3)
        else date
    }

    // (쿨엔조이)
    private fun commentZeroOrNotCoolenjoy(comment: String): String {
        return if (comment == "" || comment == null) "0"
        else comment.replace("["," ").replace("]","")
    }

    // (쿨엔조이) 블라인드 처리된 제목일 때
    private fun titleNullOrNotCoolenjoy(title: String): String {
        return if (title == "" || title == null || title == " ") "블라인드 처리된 글입니다."
        else title
    }

    private fun jsoupGetData(doc1: Document, doc2: Document, doc3: Document, page: Int): MutableList<ItemModel> {
        val list = mutableListOf<ItemModel>()

        val urlQuasarzone = "https://quasarzone.com"
        val urlPpomppu = "https://www.ppomppu.co.kr/zboard/"

        val itemListQuasarzone = doc1.select("tbody tr")
        val itemListPpomppu = doc2.select(".wrapper .contents .container .info_bg tbody tr")
        val itemListCoolenjoy = doc3.select("body div div div form div table tbody tr")

        if(page == 1) {
            for (i in 0..29) {
                val title =
                    itemListQuasarzone[i].select(".ellipsis-with-reply-cnt").text()// 제목
                val category = itemListQuasarzone[i].select(".category").text() // 카테고리
                val comment = itemListQuasarzone[i].select(".ctn-count ").text() // 댓글 수
                val date = itemListQuasarzone[i].select(".date").text() // 날짜
                val like = itemListQuasarzone[i].select("td").first()!!.text() // 추천 수
                val itemImage = itemListQuasarzone[i].select(".img-background-wrap") // 이미지
                    .attr("abs:style") // style 속성 안의 내용을 가져 옴
                    .substring(22) // 22번째 문자부터 끝 까지 가져옴
                    .replace(")", "") // oldValue 의 값을 newValue 의 값으로 바꿔 줌
                val itemUrl = itemListQuasarzone[i].select(".tit .subject-link ") // 해당 아이템 주소
                    .attr("href") // URL
                    .removeRange(30, 37)
                list.add(
                    ItemModel(
                        titleNullOrNot(title),
                        "퀘이사존 · ",
                        date,
                        category,
                        "♡  $like",
                        "\uD83D\uDCAC  " + commentZeroOrNot(comment),
                        itemImage,
                        urlQuasarzone + itemUrl
                    )
                )
            }
            for(i in 6..63 step 3) {
                val title = itemListPpomppu[i].select("table tbody tr div a font").text()
                val category = itemListPpomppu[i].select("td table tbody tr td div span")
                    .last()
                    .toString()
                    .substring(42)
                    .replace("]","")
                    .replace("</span>", "")
                val comment = itemListPpomppu[i].select(".list_comment2 span").text()
                val date = itemListPpomppu[i].select("td nobr").last().toString()
                val like = itemListPpomppu[i].select("td").textNodes().toString()
                val itemImage = itemListPpomppu[i].select("img")
                    .attr("src")
                val itemUrl = itemListPpomppu[i].select("tbody tr td a")
                    .attr("href")
                list.add(
                    ItemModel(
                        title,
                        "뽐뿌 · ",
                        timeOrDatePpomppu(date),
                        category,
                        "♡  " + likeZeroOrNot(like),
                        "\uD83D\uDCAC  " + commentZeroOrNot(comment),
                        "https:$itemImage",
                        urlPpomppu + itemUrl
                    )
                )
            }
            for (i in 1..24) {
                val title =
                    itemListCoolenjoy[i].select(".td_subject a").textNodes().first().text()
                val category = itemListCoolenjoy[i].select(".td_num").text()
                val comment = itemListCoolenjoy[i].select(".td_subject a .cnt_cmt").text()
                val date = itemListCoolenjoy[i].select(".td_date").text()
                val like = itemListCoolenjoy[i].select(".td_hit .list_good2").first()!!.text()
                val itemUrl = itemListCoolenjoy[i].select(".td_subject a")
                    .attr("href")
                list.add(
                    ItemModel(
                        titleNullOrNotCoolenjoy(title),
                        "쿨엔조이 · ",
                        timeOrNotCoolenjoy(date),
                        category,
                        "♡  $like",
                        "\uD83D\uDCAC  " + commentZeroOrNotCoolenjoy(comment),
                        "",
                        itemUrl
                    )
                )
            }
        }else {
            for (i in 0..29) {
                val title =
                    itemListQuasarzone[i].select(".ellipsis-with-reply-cnt").text()// 제목
                val category = itemListQuasarzone[i].select(".category").text() // 카테고리
                val comment = itemListQuasarzone[i].select(".ctn-count ").text() // 댓글 수
                val date = itemListQuasarzone[i].select(".date").text() // 날짜
                val like = itemListQuasarzone[i].select("td").first()!!.text() // 추천 수
                val itemImage = itemListQuasarzone[i].select(".img-background-wrap") // 이미지
                    .attr("abs:style") // style 속성 안의 내용을 가져 옴
                    .substring(22) // 22번째 문자부터 끝 까지 가져옴
                    .replace(")", "") // oldValue 의 값을 newValue 의 값으로 바꿔 줌
                val itemUrl = itemListQuasarzone[i].select(".tit .subject-link ") // 해당 아이템 주소
                    .attr("href") // URL
                    .removeRange(30, 37)
                list.add(
                    ItemModel(
                        titleNullOrNot(title),
                        "퀘이사존 · ",
                        date,
                        category,
                        "♡  $like",
                        "\uD83D\uDCAC  " + commentZeroOrNot(comment),
                        itemImage,
                        urlQuasarzone + itemUrl
                    )
                )
            }
            for(i in 3..60 step 3) {
                val title = itemListPpomppu[i].select("table tbody tr div a font").text()
                val category = itemListPpomppu[i].select("td table tbody tr td div span")
                    .last()
                    .toString()
                    .substring(42)
                    .replace("]","")
                    .replace("</span>", "")
                val comment = itemListPpomppu[i].select(".list_comment2 span").text()
                val date = itemListPpomppu[i].select("td nobr").last().toString()
                val like = itemListPpomppu[i].select("td").textNodes().toString()
                val itemImage = itemListPpomppu[i].select("img")
                    .attr("src")
                val itemUrl = itemListPpomppu[i].select("tbody tr td a")
                    .attr("href")
                list.add(
                    ItemModel(
                        title,
                        "뽐뿌 · ",
                        timeOrDatePpomppu(date),
                        category,
                        "♡  " + likeZeroOrNot(like),
                        "\uD83D\uDCAC  " + commentZeroOrNot(comment),
                        "https:$itemImage",
                        urlPpomppu + itemUrl
                    )
                )
            }
            for (i in 0..24) {
                val title =
                    itemListCoolenjoy[i].select(".td_subject a").textNodes().first().text()
                val category = itemListCoolenjoy[i].select(".td_num").text()
                val comment = itemListCoolenjoy[i].select(".td_subject a .cnt_cmt").text()
                val date = itemListCoolenjoy[i].select(".td_date").text()
                val like = itemListCoolenjoy[i].select(".td_hit .list_good2").first()!!.text()
                val itemUrl = itemListCoolenjoy[i].select(".td_subject a")
                    .attr("href")
                list.add(
                    ItemModel(
                        titleNullOrNotCoolenjoy(title),
                        "쿨엔조이 · ",
                        timeOrNotCoolenjoy(date),
                        category,
                        "♡  $like",
                        "\uD83D\uDCAC  " + commentZeroOrNotCoolenjoy(comment),
                        "",
                        itemUrl
                    )
                )
            }
        }
        return list
    }
}