package cr.ac.una.gps.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class PuntoPoly (
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val latitud: Double,
    val longitud: Double,
)