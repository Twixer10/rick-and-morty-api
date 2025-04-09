package org.mathieu.cleanrmapi.data.local.objects

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.mathieu.cleanrmapi.common.tryOrNull
import org.mathieu.cleanrmapi.data.extensions.extractIdsFromUrls
import org.mathieu.cleanrmapi.data.local.RMDatabase
import org.mathieu.cleanrmapi.data.remote.responses.LocationResponse
import org.mathieu.cleanrmapi.data.validators.annotations.MustBeCommaSeparatedIds
import org.mathieu.cleanrmapi.domain.character.models.Character
import org.mathieu.cleanrmapi.domain.location.models.Location
import org.mathieu.cleanrmapi.domain.location.models.LocationPreview

@Entity(tableName = RMDatabase.LOCATION_TABLE)
class LocationObject(
    @PrimaryKey
    val id: Int,
    val name: String,
    val dimension: String,
    val type: String,
    @MustBeCommaSeparatedIds
    val residents: String,
)

internal suspend fun LocationObject.toModel(
    idsToCharacterConverter: suspend (characterIds: String) -> List<Character> = { emptyList() }
) = Location(
    id = id,
    name = name,
    type = type,
    dimension = dimension,
    residents = idsToCharacterConverter(residents)
)

internal fun LocationObject.toPreviewModel() = LocationPreview(
    id = id,
    name = name
)

internal fun LocationResponse.toDBObject() = LocationObject(
    id = id,
    name = name,
    dimension = dimension,
    type = type,
    residents = residents.extractIdsFromUrls(),
)