package org.mathieu.cleanrmapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.mathieu.cleanrmapi.data.local.objects.LocationObject

@Dao
interface LocationDao {
    @Query("SELECT * FROM ${RMDatabase.LOCATION_TABLE} WHERE id = :id")
    suspend fun getLocationById(id: Int): LocationObject?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationObject)
}