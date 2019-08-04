package com.zhuzichu.mvvm.repository

import com.zhuzichu.mvvm.db.AppDatabase
import com.zhuzichu.mvvm.db.SearchHistory
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

object DbRepositoryImpl : DbRepository {
    override suspend fun deleteSearchHistory(list: List<SearchHistory>) {
        return withContext(IO){
            AppDatabase.getInstance().historyDao().deleteSearchHistory(list)
        }
    }

    override fun getSearchHistoryList(): Flowable<List<SearchHistory>> {
        return AppDatabase.getInstance().historyDao().queryAllSearchHistory()
    }

    override suspend fun addSearchHistory(keyWord: String) {
        withContext(IO) {
            AppDatabase.getInstance().historyDao().insert(
                SearchHistory(
                    keyWord
                )
            )
        }
    }

}