package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.database.dao.BookMarkDao
import com.example.domain.entity.BookMarkEntity

@Database(entities = [BookMarkEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookMarkDao(): BookMarkDao

    companion object {
        private const val DB_NAME = "lezhin_task.db"

        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                }).fallbackToDestructiveMigration().build()
        }
    }
}