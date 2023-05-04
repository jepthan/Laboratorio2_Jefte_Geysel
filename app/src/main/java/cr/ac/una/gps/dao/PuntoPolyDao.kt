package cr.ac.una.gps.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cr.ac.una.gps.entity.PuntoPoly

@Dao
interface PuntoPolyDao {
    @Insert
    fun insert(entity: cr.ac.una.gps.entity.PuntoPoly)

    @Query("SELECT * FROM ubicacion")
    fun getAll(): List<cr.ac.una.gps.entity.PuntoPoly?>?
}