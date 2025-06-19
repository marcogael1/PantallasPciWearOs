package com.example.plantillasproyecto.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.plantillasproyecto.ui.screens.HomeScreen
import com.example.plantillasproyecto.ui.theme.PCIStoreTheme

@Preview(
    name = "Pantalla Pequeña 320x320",
    device = "spec:width=320dp,height=320dp,dpi=240,isRound=true,chinSize=0dp,orientation=portrait",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun HomeScreenSmallPreview() {
    PCIStoreTheme {
        HomeScreen(rememberNavController())
    }
}

@Preview(
    name = "Pantalla Mediana 390x390",
    device = "spec:width=390dp,height=390dp,dpi=240,isRound=true,chinSize=0dp,orientation=portrait",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun HomeScreenMediumPreview() {
    PCIStoreTheme {
        HomeScreen(rememberNavController())
    }
}

@Preview(
    name = "Pantalla Grande 454x454",
    device = "spec:width=454dp,height=454dp,dpi=240,isRound=true,chinSize=0dp,orientation=portrait",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun HomeScreenLargePreview() {
    PCIStoreTheme {
        HomeScreen(rememberNavController())
    }
}

@Preview(
    name = "Galaxy Watch 4",
    device = Devices.WEAR_OS_LARGE_ROUND,
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun HomeScreenGalaxyWatch4Preview() {
    PCIStoreTheme {
        HomeScreen(rememberNavController())
    }
}