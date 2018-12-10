package com.alihnaqvi.findmymeal

import android.provider.BaseColumns

object MealsDbContract {
    object MealsDbEntry : BaseColumns {
        const val TABLE_NAME = "meals"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_URL = "thumbnail_url"
        const val COLUMN_NAME_TITLE = "name"
    }
}