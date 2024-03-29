package com.example.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.myapplication.models.Article

@Database(
    entities = [Article::class],
    version = 1
)

@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun getArticleDao(): ArticleDAO

    companion object{
        //thay doi o 1 luong se thay doi ca o cac luong khac
        @Volatile
        private var instance : ArticleDatabase? = null
        private var LOCK = Any()



        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance?: createDatabase(context).also{
                instance = it
            }
        }
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}