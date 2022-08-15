package com.example.jetnews.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [BookDBLikeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {
    internal abstract fun bookLikeEntityDao(): BookDbLikeEntityDao
}

@Dao
internal interface BookDbLikeEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(like: BookDBLikeEntity)

    @Query("SELECT * FROM book_db_like_entity")
    fun getAll(): Flow<List<BookDBLikeEntity>>

    @Query("DELETE FROM book_db_like_entity WHERE id = :id")
    suspend fun delete(id: Long)

}