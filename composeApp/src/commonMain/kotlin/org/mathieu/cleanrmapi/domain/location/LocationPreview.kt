package org.mathieu.cleanrmapi.domain.location

/**
 * Represents a minimal information from Location object.
 *
 * @property id The unique identifier for the location.
 * @property name The name of the location.
 */
data class LocationPreview(
    val id: Int,
    val name: String
)