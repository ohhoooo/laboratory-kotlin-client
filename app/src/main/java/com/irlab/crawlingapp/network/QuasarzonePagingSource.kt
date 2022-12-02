package com.irlab.crawlingapp.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

// PagingSource - 데이터 소스를 정의하고 이 소스에서 데이터를 가져오는 방법을 정의
class QuasarzonePagingSource(
    private val apiService: QuasarzoneService
) : PagingSource<Int, ItemModel>() {
    override fun getRefreshKey(state: PagingState<Int, ItemModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    // load : 사용자가 스크롤 할 때마다 데이터를 비동기적으로 가져온다.
    // loadParams : 실행할 로드 작업에 관한 정보를 포함(로드할 키, 로드할 항목 수)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemModel> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE_INDEX

            val response = apiService.getDataFromAPI(nextPage)
            val responseHtml = response.body()!!.string()
            val jsoupParse = Jsoup.parse(responseHtml)
            val responseData = jsoupGetData(jsoupParse)

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

    // 블라인드 처리된 제목일 때
    private fun titleNullOrNot(title: String): String {
        return if (title == "") "블라인드 처리된 글입니다."
        else title
    }

    // 댓글 수를 판별 후, 0이면 0 표시
    private fun commentZeroOrNot(comment: String): String {
        return if (comment == "") "0"
        else comment
    }

    private fun jsoupGetData(doc: Document): MutableList<ItemModel> {
        val list = mutableListOf<ItemModel>()
        val url = "https://quasarzone.com"
        val itemList = doc.select("tbody tr") // 아이템 리스트 가져오기

        // 리스트에 아이템의 각 파트를 만들어 추가하는 부분
        for (i in 0..29) {
            val title =
                itemList[i].select(".ellipsis-with-reply-cnt").text()// 제목
            val category = itemList[i].select(".category").text() // 카테고리
            val comment = itemList[i].select(".ctn-count ").text() // 댓글 수
            val date = itemList[i].select(".date").text() // 날짜
            val like = itemList[i].select("td").first()!!.text() // 추천 수
            val itemImage = itemList[i].select(".img-background-wrap") // 이미지
                .attr("abs:style") // style 속성 안의 내용을 가져 옴
                .substring(22) // 22번째 문자부터 끝 까지 가져옴
                .replace(")", "") // oldValue 의 값을 newValue 의 값으로 바꿔 줌
            val itemUrl = itemList[i].select(".tit .subject-link ") // 해당 아이템 주소
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
                    url + itemUrl
                )
            )
        }
        return list
    }
}