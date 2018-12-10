package com.alihnaqvi.findmymeal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


class MealsDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTERIES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "FavoriteMeals.db"
        const val DATABASE_VERSION = 1

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${MealsDbContract.MealsDbEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${MealsDbContract.MealsDbEntry.COLUMN_NAME_ID} TEXT," +
                    "${MealsDbContract.MealsDbEntry.COLUMN_NAME_TITLE} TEXT," +
                    "${MealsDbContract.MealsDbEntry.COLUMN_NAME_URL} TEXT)"

        private const val SQL_DELETE_ENTERIES = "DROP TABLE IF EXISTS ${MealsDbContract.MealsDbEntry.TABLE_NAME}"
    }
}