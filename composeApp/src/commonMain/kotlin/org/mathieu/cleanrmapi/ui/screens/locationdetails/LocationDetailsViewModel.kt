package org.mathieu.cleanrmapi.ui.screens.locationdetails

import org.koin.core.component.inject
import org.mathieu.cleanrmapi.domain.character.CharacterRepository
import org.mathieu.cleanrmapi.domain.character.models.Character
import org.mathieu.cleanrmapi.domain.character.models.CharacterGender
import org.mathieu.cleanrmapi.domain.character.models.CharacterStatus
import org.mathieu.cleanrmapi.domain.episode.models.Episode
import org.mathieu.cleanrmapi.domain.location.LocationRepository
import org.mathieu.cleanrmapi.domain.location.models.Location
import org.mathieu.cleanrmapi.ui.core.Destination
import org.mathieu.cleanrmapi.ui.core.ViewModel

sealed interface LocationDetailsAction {
    data class SelectCharacter(val character: Character) : LocationDetailsAction
}

class LocationDetailsViewModel :
    ViewModel<LocationDetailsState>(LocationDetailsState.Loading) {

    private val locationRepository: LocationRepository by inject()

    fun init(locationId: Int) {

        fetchData(
            source = { locationRepository.getLocation(id = locationId) }
        ) {

            onSuccess { details ->
                if (details != null) {
                    updateState {
                        LocationDetailsState.Loaded(
                            name = details.name,
                            type = details.type,
                            dimension = details.dimension,
                            residents = details.residents
                        )
                    }
                }
            }

            onFailure {
                updateState {
                    LocationDetailsState.Error(message = it.message ?: it.toString())
                }
            }


        }


    }

    fun handleAction(action: LocationDetailsAction) {
        when (action) {
            is LocationDetailsAction.SelectCharacter ->
                sendEvent(Destination.CharacterDetails(action.character.id.toString()))
        }
    }


}

sealed interface LocationDetailsState {
    data object Loading : LocationDetailsState

    data class Error(val message: String) : LocationDetailsState

    data class Loaded(
        val name: String = "",
        val type: String = "",
        val dimension: String = "",
        val residents: List<Character> = emptyList(),
    ) : LocationDetailsState

}