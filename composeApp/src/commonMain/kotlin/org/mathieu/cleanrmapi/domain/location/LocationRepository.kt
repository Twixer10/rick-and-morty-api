package org.mathieu.cleanrmapi.domain.location

import org.mathieu.cleanrmapi.domain.location.models.Location

interface LocationRepository {
    /**
     * Fetches a list of all available locations.
     *
     * @return A list of [Location] objects representing all locations.
     */
    suspend fun getLocation(id: Int): Location?
}