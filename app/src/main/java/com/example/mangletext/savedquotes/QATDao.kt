package com.example.mangletext.savedquotes

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QATDao {
    @Query("SELECT * FROM quote_author_translation_table")
     fun getAll(): LiveData<List<QATObject>>

    @Insert
    suspend fun insert(qatObject: QATObject)

    @Delete
    suspend fun delete(qatObject: QATObject)
}