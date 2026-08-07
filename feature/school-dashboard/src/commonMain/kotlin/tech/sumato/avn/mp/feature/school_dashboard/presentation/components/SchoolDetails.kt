package tech.sumato.avn.mp.feature.school_dashboard.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coronavirus
import androidx.compose.material.icons.filled.PanoramaFishEye
import androidx.compose.material.icons.filled.PanoramaPhotosphere
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.MultiAspectCarouselScope
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.components.app.AppChip

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import qrgenerator.qrkitpainter.PatternType
import qrgenerator.qrkitpainter.QrBallType
import qrgenerator.qrkitpainter.QrFrameType
import qrgenerator.qrkitpainter.QrKitBrush
import qrgenerator.qrkitpainter.QrKitColors
import qrgenerator.qrkitpainter.QrKitLogo
import qrgenerator.qrkitpainter.QrKitShapes
import qrgenerator.qrkitpainter.QrPixelType
import qrgenerator.qrkitpainter.customBrush
import qrgenerator.qrkitpainter.getSelectedFrameShape
import qrgenerator.qrkitpainter.getSelectedPattern
import qrgenerator.qrkitpainter.getSelectedPixel
import qrgenerator.qrkitpainter.getSelectedQrBall
import qrgenerator.qrkitpainter.rememberQrKitPainter
import tech.sumato.avn.mp.designsystem.components.app.AppCarousel


@Preview(showBackground = true)
@Composable
fun SchoolDetails() {


    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppChip(modifier = Modifier.wrapContentWidth()) {
                    Text(
                        "Primary School",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Text(
                    "Govt. Middle School, Jang Village",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "UDISE: 12010201105",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        "Established: 1992",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Box(modifier = Modifier.size(80.dp)) {
                val painter = rememberQrKitPainter("hello world") {
                    shapes = QrKitShapes(
                        ballShape = getSelectedQrBall(QrBallType.RoundCornersQrBall(radius = 0.2f)),
                        darkPixelShape = getSelectedPixel(QrPixelType.RoundCornerPixel()),
                        frameShape = getSelectedFrameShape(QrFrameType.RoundCornersFrame(corner = 0.1f)),
                        codeShape = getSelectedPattern(PatternType.SquarePattern),
                    )
                    colors = QrKitColors(
                        darkBrush = QrKitBrush.customBrush {
                            Brush.linearGradient(
                                0f to Color.White,
                                1f to Color.White,
                                end = Offset(it, it)
                            )
                        }
                    )
//                    logo = QrKitLogo(centerLogo)
                }

                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = Dp.Hairline)

        Spacer(modifier = Modifier.height(8.dp))

        AppCarousel(
            modifier = Modifier.fillMaxWidth(), images = listOf(
                "https://ik.imagekit.io/5vqnph4a8/assetm/tr:q-75,w-400,h-400/uploads/images/i9GjxMNHcrpbeHxv6U7SnKFFvNvw0dMG0McxACUn.jpg?ik-sdk-version=php-2.0.0",
                "https://ik.imagekit.io/5vqnph4a8/assetm/tr:q-75,w-400,h-400/uploads/images/i9GjxMNHcrpbeHxv6U7SnKFFvNvw0dMG0McxACUn.jpg?ik-sdk-version=php-2.0.0",
                "https://ik.imagekit.io/5vqnph4a8/assetm/tr:q-75,w-400,h-400/uploads/images/i9GjxMNHcrpbeHxv6U7SnKFFvNvw0dMG0McxACUn.jpg?ik-sdk-version=php-2.0.0"
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.End),
        ) {
            AppChip(modifier = Modifier.wrapContentWidth()) {
                Row(
                    modifier = Modifier.wrapContentWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(Icons.Default.PhotoAlbum, "", modifier = Modifier.size(16.dp))
                    Text("All photos", style = MaterialTheme.typography.bodySmall)
                }
            }

            AppChip(modifier = Modifier.wrapContentWidth()) {
                Row(
                    modifier = Modifier.wrapContentWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(Icons.Default.PanoramaPhotosphere, "", modifier = Modifier.size(16.dp))
                    Text("Virtual Tour", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = Dp.Hairline)

        Spacer(Modifier.height(8.dp))

        SchoolLiveAttendance()

        Spacer(Modifier.height(8.dp))

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = Dp.Hairline)

        Spacer(Modifier.height(8.dp))


    }


}
