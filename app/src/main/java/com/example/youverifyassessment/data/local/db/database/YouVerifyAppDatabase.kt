package com.example.youverifyassessment.data.local.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.youverifyassessment.data.local.db.dao.ProductDao
import com.example.youverifyassessment.data.local.db.dao.ShoppingCartDao
import com.example.youverifyassessment.data.local.db.entities.ProductEntity
import com.example.youverifyassessment.data.local.db.entities.ShoppingCartEntity
import com.example.youverifyassessment.data.local.db.typeConverters.RoomDataTypeConverters
import com.example.youverifyassessment.utils.AppConstants.DB_NAME
import com.example.youverifyassessment.utils.AppConstants.DB_VERSION

@Database(
    entities = [ProductEntity::class, ShoppingCartEntity::class],
    version = DB_VERSION,
    exportSchema = false
)
@TypeConverters(
    RoomDataTypeConverters::class
)
abstract class YouVerifyAppDatabase : RoomDatabase() {
    abstract fun getProductsDao(): ProductDao
    abstract fun getShoppingCartDao(): ShoppingCartDao

    companion object {
        @Volatile
        private var INSTANCE: YouVerifyAppDatabase? = null

        fun getDatabase(context: Context): YouVerifyAppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context): YouVerifyAppDatabase =
            Room.databaseBuilder(context, YouVerifyAppDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}