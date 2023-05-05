package cr.ac.una.gps.db;

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cr.ac.una.gps.dao.UbicacionDao
import cr.ac.una.gps.converter.Converters
import cr.ac.una.gps.dao.PuntoPolyDao
import cr.ac.una.gps.entity.PuntoPoly
import cr.ac.una.gps.entity.Ubicacion

@Database(entities = [cr.ac.una.gps.entity.Ubicacion::class, cr.ac.una.gps.entity.PuntoPoly::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ubicacionDao(): UbicacionDao
    abstract fun puntoPolyDao(): PuntoPolyDao
    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "ubicaciones-database"
                    ).build()
                }
            }
            return instance!!
        }
    }
}