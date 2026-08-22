package dev.spooky.socialmediaapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun AvatarComponent(size: AvatarComponentSize = AvatarComponentSize.MEDIUM) {
    Box(
        Modifier
            .padding(8.dp)
            .size(size.measure.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            Icons.Default.Person,
            null,
            Modifier.size(50.dp),
            tint = MaterialTheme.colorScheme.secondary,
        )
    }
}

enum class AvatarComponentSize(
    size: Int,
) {
    SMALL(32),
    MEDIUM(48),
    LARGE(64),
    ;

    val measure: Int = size
}
