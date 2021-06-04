package com.example.mangletext.savedquotes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

@Database(entities = arrayOf(QATObject::class), version = 1, exportSchema = false)
abstract class QATDB : RoomDatabase() {
    abstract val qATDao: QATDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var INSTANCE: QATDB? = null

        fun getInstance(context: Context): QATDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        QATDB::class.java,
                        "saved_quotes_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}