package com.example.mangletext.savedquotes

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "quote_author_translation_table")
@Parcelize
data class QATObject(
    @ColumnInfo(name = "quotation")val quotation: String,
    @ColumnInfo(name = "author")val author: String,
    @ColumnInfo(name = "translation")val translation: String,
    @ColumnInfo(name = "date")val date: String,
    @PrimaryKey @ColumnInfo(name = "qat_id") val id: String = UUID.randomUUID().toString(),

) : Parcelable