package com.miraeldev.data.local.dao.popularCategory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miraeldev.data.local.models.popularCategory.PopularCategoryRemoteKeys

@Dao
interface PopularCategoryRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<PopularCategoryRemoteKeys>)

    @Query("Select * From popular_category_remote_key Where animeId = :id")
    suspend fun getRemoteKeyByAnimeId(id: Int): PopularCategoryRemoteKeys?

    @Query("Delete From popular_category_remote_key")
    suspend fun clearRemoteKeys()

    @Query("Select createdAt From popular_category_remote_key Order By createdAt DESC LIMIT 1")
    suspend fun getCreationTime(): Long?

}