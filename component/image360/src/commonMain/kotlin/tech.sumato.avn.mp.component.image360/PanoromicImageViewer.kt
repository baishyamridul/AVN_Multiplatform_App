package tech.sumato.avn.mp.component.image360

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData


@Composable
fun PanoromicImageViewer(modifier: Modifier, configUrl: String) {

    val htmlContent = getHtmlContent(configUrl)

    val webViewState = rememberWebViewStateWithHTMLData(
        data = htmlContent
    )

    LaunchedEffect(Unit) {
        webViewState.webSettings.apply {
            logSeverity = KLogSeverity.Debug
            customUserAgentString =
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_1) AppleWebKit/625.20 (KHTML, like Gecko) Version/14.3.43 Safari/625.20"
        }
    }

    val loadingState = webViewState.loadingState
    if (loadingState is LoadingState.Loading) {
        LinearProgressIndicator(
            progress = { loadingState.progress },
            modifier = Modifier.fillMaxWidth(),
        )
    }


    WebView(
        modifier = modifier,
        state = webViewState,
    )

}


fun getHtmlContent(configUrl: String): String {


    return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">

            <meta
                    name="viewport"
                    content="width=device-width, initial-scale=1.0"
            >

            <title>360° Test</title>

            <link
                    rel="stylesheet"
                    href="https://cdn.jsdelivr.net/npm/pannellum@2.5.6/build/pannellum.css"
            >

            <script
                    src="https://cdn.jsdelivr.net/npm/pannellum@2.5.6/build/pannellum.js">
            </script>

            <style>
                html,
                body,
                #panorama {
                    width: 100%;
                    height: 100%;
                    margin: 0;
                }

                body {
                    overflow: hidden;
                    background: black;
                }
            </style>
        </head>

        <body>

        <div id="panorama"></div>

        <script>
            fetch("${configUrl}")
                .then(response => {
                    if (!response.ok) {
                        throw new Error(
                            `Config failed: ${'$'}{response.status}`
                        );
                    }

                    return response.json();
                })
                .then(config => {

                    // Generated paths are relative to tiles/
        <!--            config.multiRes.basePath = "imgtest3/";-->
                    config.multiRes.basePath = "https://mridx.github.io/360img";

                    config.autoLoad = true;

                    config.showControls = false;

                    pannellum.viewer(
                        "panorama",
                        config
                    );
                })
                .catch(error => {
                    console.error(
                        "Panorama error:",
                        error
                    );
                });
        </script>

        </body>
        </html>
    """.trimIndent()

}