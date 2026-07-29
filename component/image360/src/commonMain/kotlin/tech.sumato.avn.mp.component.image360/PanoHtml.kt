package tech.sumato.avn.mp.component.image360

fun viewerHtml(configUrl: String): String = """
<!DOCTYPE html>
<html lang="en">
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
    var configUrl = "$configUrl";
    if (configUrl) {
        fetch(configUrl)
            .then(function (r) { if (!r.ok) throw new Error("HTTP " + r.status); return r.json(); })
            .then(function (cfg) {
                if (cfg.multiRes) { cfg.multiRes.basePath = "https://mridx.github.io/360img"; }
                cfg.autoLoad = true;
                cfg.showControls = false;
                pannellum.viewer("panorama", cfg);
            })
            .catch(function (e) { console.error("Panorama error:", e); document.body.innerHTML = '<div style="color:white;padding:24px;text-align:center">Error: ' + e.message + '</div>'; });
    }
</script>
</body>
</html>
""".trimIndent()
