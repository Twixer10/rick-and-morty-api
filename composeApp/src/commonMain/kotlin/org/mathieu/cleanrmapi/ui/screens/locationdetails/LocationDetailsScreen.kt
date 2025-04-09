package org.mathieu.cleanrmapi.ui.screens.characterdetails

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mathieu.cleanrmapi.ui.core.composables.BackArrow
import org.mathieu.cleanrmapi.ui.core.composables.CharacterCard
import org.mathieu.cleanrmapi.ui.core.composables.PreviewContent
import org.mathieu.cleanrmapi.ui.core.composables.Screen
import org.mathieu.cleanrmapi.ui.core.theme.PrimaryColor
import org.mathieu.cleanrmapi.ui.core.theme.SurfaceColor
import org.mathieu.cleanrmapi.ui.screens.locationdetails.LocationDetailsAction
import org.mathieu.cleanrmapi.ui.screens.locationdetails.LocationDetailsState
import org.mathieu.cleanrmapi.ui.screens.locationdetails.LocationDetailsViewModel

@Composable
fun LocationDetailsScreen(
    navController: NavController,
    id: Int
) {
    Screen(
        viewModel = viewModel { LocationDetailsViewModel() },
        navController = navController
    ) { state, viewModel ->

        LaunchedEffect(key1 = Unit) {
            viewModel.init(locationId = id)
        }

        Content(
            state = state,
            onClickBack = navController::popBackStack,
            onAction = viewModel::handleAction
        )

    }

}

@Composable
private fun Content(
    state: LocationDetailsState = LocationDetailsState.Loading,
    onAction: (LocationDetailsAction) -> Unit = { },
    onClickBack: () -> Unit = { }
) = Box(
    modifier = Modifier
        .fillMaxSize()
        .padding(),
    contentAlignment = Alignment.Center
) {

    BackArrow(
        modifier = Modifier
            .align(Alignment.TopStart)
            .zIndex(1f),
        onClick = onClickBack
    )

    Crossfade(targetState = state) {
        when (it) {
            is LocationDetailsState.Error -> ErrorView(error = it.message)
            is LocationDetailsState.Loaded -> LocationDetailsContent(
                state = it,
                onAction = onAction
            )

            is LocationDetailsState.Loading -> {
                /** TODO: Could display a Loading Animation */
            }
        }
    }
}


@Composable
private fun ErrorView(error: String) {
    Text(
        modifier = Modifier.padding(16.dp),
        text = error,
        textAlign = TextAlign.Center,
        color = PrimaryColor,
        fontSize = 32.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 36.sp
    )
}


private object LocationDetailsContent {

    @Composable
    operator fun invoke(
        state: LocationDetailsState.Loaded,
        onAction: (LocationDetailsAction) -> Unit
    ) {

        var offsetY by remember {
            mutableFloatStateOf(0f)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Header(
                state = state,
                offsetY = offsetY
            )

            LazyColumn {
                itemsIndexed(state.residents) { index, character ->
                    if (index == 0) {
                        Box(modifier = Modifier.onGloballyPositioned {
                            offsetY = it.positionInParent().y
                        })
                    }


                    CharacterCard(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                onAction(LocationDetailsAction.SelectCharacter(character))
                            },
                        character = character
                    )

                }

            }

        }


    }


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun Header(
        state: LocationDetailsState.Loaded,
        offsetY: Float
    ) {

        val density = LocalDensity.current

        val additionalHeight: Dp = with(density) { offsetY.toDp() }

        val animatedHeight by animateDpAsState(targetValue = 200.dp + additionalHeight)

        Box(
            modifier = Modifier.height(animatedHeight)
        ) {

            Column(
                modifier = Modifier
                    .background(SurfaceColor.copy(alpha = 0.3f))
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {

                Text(
                    modifier = Modifier
                        .background(SurfaceColor, RoundedCornerShape(4.dp))
                        .basicMarquee(iterations = Int.MAX_VALUE)
                        .padding(8.dp),
                    text = state.name,
                    fontSize = 21.sp,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center
                )

            }
        }
    }
}

@Preview
@Composable
private fun LocationDetailsPreview() = PreviewContent {
    Content()
}

