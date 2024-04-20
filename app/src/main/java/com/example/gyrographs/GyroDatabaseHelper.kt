package com.example.gyrographs

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GyroDatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "gyro_database"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "sensor_data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_X = "x"
        private const val COLUMN_Y = "y"
        private const val COLUMN_Z = "z"
        private const val COLUMN_TIMESTAMP = "timestamp"

        @Volatile
        private var instance: GyroDatabaseHelper? = null

        fun getInstance(context: Context): GyroDatabaseHelper {
            return instance ?: synchronized(this) {
                val newInstance = GyroDatabaseHelper(context.applicationContext)
                instance = newInstance
                newInstance
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_X REAL,
                $COLUMN_Y REAL,
                $COLUMN_Z REAL,
                $COLUMN_TIMESTAMP LONG
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertSensorData(x: Float, y: Float, z: Float) {
        val contentValues = ContentValues().apply {
            put(COLUMN_X, x)
            put(COLUMN_Y, y)
            put(COLUMN_Z, z)
            put(COLUMN_TIMESTAMP, System.currentTimeMillis())
        }
        writableDatabase.insert(TABLE_NAME, null, contentValues)
    }

    // Add a method to retrieve orientation data
    fun getAllOrientationData(): List<OrientationData> {
        val orientationDataList = mutableListOf<OrientationData>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
        cursor.use {
            while (cursor.moveToNext()) {
                val x = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_X))
                val y = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_Y))
                val z = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_Z))
                val timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                val orientationData = OrientationData(x, y, z, timestamp)
                orientationDataList.add(orientationData)
            }
        }
        return orientationDataList
    }

}

data class OrientationData(
    val x: Float,
    val y: Float,
    val z: Float,
    val timestamp: Long
)
