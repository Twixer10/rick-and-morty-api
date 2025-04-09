package org.mathieu.cleanrmapi.data.repositories

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mathieu.cleanrmapi.data.local.LocationDao
import org.mathieu.cleanrmapi.data.local.objects.LocationObject
import org.mathieu.cleanrmapi.data.local.objects.toDBObject
import org.mathieu.cleanrmapi.data.local.objects.toModel
import org.mathieu.cleanrmapi.data.remote.CharacterApi
import org.mathieu.cleanrmapi.data.remote.LocationApi
import org.mathieu.cleanrmapi.domain.location.LocationRepository
import org.mathieu.cleanrmapi.domain.location.models.Location

internal class LocationRepositoryImpl(
    private val locationApi: LocationApi,
    private val characterApi: CharacterApi
) : LocationRepository {

    override suspend fun getLocation(id: Int): Location? {
        val locationLocal = GetLocationObjectIfExists(id)

        return locationLocal
            .toModel(
                idsToCharacterConverter = { characterIds ->
                    if (characterIds.contains(",")) {
                        characterApi.getCharactersFromIds(ids = characterIds)
                            .map { it.toDBObject().toModel() }
                    } else {
                        characterApi.getCharacter(characterIds.toInt())
                            ?.toDBObject()
                            ?.toModel()
                            ?.let { listOf(it) } ?: emptyList()
                    }
                }
            )
    }
}

private object GetLocationObjectIfExists : KoinComponent {

    private val locationApi: LocationApi by inject()
    private val locationLocal: LocationDao by inject()

    suspend operator fun invoke(locationId: Int): LocationObject =
        tryToGetLocationLocally(locationId)
            .fetchRemotelyIfNotFound(locationId)
            .throwIfWeCannotFindIt()

    private suspend fun tryToGetLocationLocally(id: Int) = locationLocal.getLocationById(id)

    private suspend fun LocationObject?.fetchRemotelyIfNotFound(id: Int): LocationObject? {
        if (this != null) return this

        return locationApi.getLocation(id = id)
            ?.toDBObject()
            ?.also { newLoc ->
                locationLocal.insertLocation(newLoc)
            }
    }

    private fun LocationObject?.throwIfWeCannotFindIt(): LocationObject {
        if (this != null) return this
        throw IllegalStateException("Location not found in local or from remote API")
    }

}