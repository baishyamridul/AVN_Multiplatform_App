package tech.sumato.avn.mp.component.image360


fun basicViewerHtml(imageUrl: String): String = """
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>360° Viewer</title>
    <link rel="stylesheet" href="pannellum.css">
    <script src="pannellum.js"></script>
    <style>
        html, body, #panorama { width: 100%; height: 100%; margin: 0; }
        body { overflow: hidden; background: black; }
    </style>
</head>
<body>

<div id="panorama"></div>
<script>
pannellum.viewer('panorama', {
    "type": "equirectangular",
    "autoLoad": true,
    "showControls":false,
    "panorama": "$imageUrl"
});
</script>

</body>
</html>
""".trimIndent()