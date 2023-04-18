package cr.ac.una.gps.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cr.ac.una.gps.entity.Ubicacion

@Dao
interface UbicacionDao {
    @Insert
    fun insert(entity: cr.ac.una.gps.entity.Ubicacion)

    @Query("SELECT * FROM ubicacion")
    fun getAll(): List<cr.ac.una.gps.entity.Ubicacion?>?
}