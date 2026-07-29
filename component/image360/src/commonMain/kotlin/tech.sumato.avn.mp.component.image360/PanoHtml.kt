package tech.sumato.avn.mp.component.image360

fun generateHtml(configUrl: String): String = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>360° Image</title>
    <link rel="stylesheet" href="./pannellum.css">
    <script src="./pannellum.js"></script>
    <style>
        html, body, #panorama { width: 100%; height: 100%; margin: 0; }
        body { overflow: hidden; background: black; }
    </style>
</head>
<body>
<div id="panorama"></div>
<script>
    fetch("${configUrl}")
        .then(response => {
            if (!response.ok) throw new Error("Config failed: " + response.status);
            return response.json();
        })
        .then(config => {
            config.multiRes.basePath = "https://mridx.github.io/360img";
            config.autoLoad = true;
            config.showControls = false;
            pannellum.viewer("panorama", config);
        })
        .catch(error => console.error("Panorama error:", error));
</script>
</body>
</html>
""".trimIndent()
