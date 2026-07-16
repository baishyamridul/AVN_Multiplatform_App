#!/bin/bash
set -e

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"

die() { echo "❌ $1" >&2; exit 1; }
info() { echo "   $1"; }
ok() { echo "   ✅ $1"; }
warn() { echo "   ⚠️  $1"; }

# Portable sed in-place
if [[ "$(uname)" == "Darwin" ]]; then
    SED_INPLACE=("sed" "-i" "")
else
    SED_INPLACE=("sed" "-i")
fi

echo ""
echo "═══════════════════════════════════════"
echo "      KMP Package Rename Tool"
echo "═══════════════════════════════════════"
echo ""
echo "Renames a Kotlin package across the entire project:"
echo "  - Directory structures"
echo "  - Package declarations & imports"
echo "  - Gradle namespaces, applicationId, mainClass"
echo "  - iOS bundle identifier"
echo "  - Theme function name (if derived from last segment)"
echo ""

read -rp "Current package name (e.g. tech.sumato.kmptemplate): " OLD_PACKAGE
[[ -z "$OLD_PACKAGE" ]] && die "Package name cannot be empty"

read -rp "New package name (e.g. tech.sumato.avn.mp): " NEW_PACKAGE
[[ -z "$NEW_PACKAGE" ]] && die "Package name cannot be empty"

if [[ "$OLD_PACKAGE" == "$NEW_PACKAGE" ]]; then
    die "Old and new package names are the same"
fi

echo ""
echo "  Current:  $OLD_PACKAGE"
echo "  New:      $NEW_PACKAGE"
echo ""
read -rp "Proceed? [y/N]: " CONFIRM
[[ "$CONFIRM" =~ ^[Yy]$ ]] || die "Aborted"

OLD_PATH=$(echo "$OLD_PACKAGE" | tr '.' '/')
NEW_PATH=$(echo "$NEW_PACKAGE" | tr '.' '/')

# ── 1. Rename directories ──────────────────────────────────────────
info "Step 1/3 — Renaming directories..."
DIR_COUNT=0

find "$ROOT_DIR" -type d -path "*/$OLD_PATH" \
    -not -path "*/.git/*" \
    -not -path "*/build/*" 2>/dev/null | while read -r old_dir; do

    base_dir="${old_dir%/$OLD_PATH}"
    new_dir="$base_dir/$NEW_PATH"
    mkdir -p "$new_dir"

    # Move files (including dotfiles) from old dir to new dir
    for item in "$old_dir"/* "$old_dir"/.[!.]*; do
        [[ -e "$item" ]] && mv "$item" "$new_dir/" 2>/dev/null || true
    done

    # Remove empty old directories bottom-up
    rmdir "$old_dir" 2>/dev/null || true
    # Clean up empty ancestor directories (e.g. intermediate old-package dirs)
    ancestor="$old_dir"
    while [[ "$ancestor" != "$ROOT_DIR" && "$ancestor" != "$base_dir" ]]; do
        ancestor="$(dirname "$ancestor")"
        rmdir "$ancestor" 2>/dev/null || break
    done

    echo -ne "\r   Moved: $old_dir → $new_dir\033[K"
    ((DIR_COUNT++))
done

echo -e "\r   ✅ Directories renamed                            "

# ── 2. Replace in file contents ────────────────────────────────────
info "Step 2/3 — Updating package references in files..."

find "$ROOT_DIR" \
    -not -path "*/.git/*" \
    -not -path "*/build/*" \
    \( -name "*.kt" -o -name "*.kts" -o -name "*.xml" -o -name "*.sh" \
       -o -name "*.md" -o -name "*.html" -o -name "*.plist" \
       -o -name "*.xcconfig" -o -name "*.properties" -o -name "*.pbxproj" \) \
    2>/dev/null \
    -exec "${SED_INPLACE[@]}" "s/$OLD_PACKAGE/$NEW_PACKAGE/g" {} +

ok "All package references updated"

# ── 3. Theme function rename ────────────────────────────────────────
info "Step 3/3 — Checking for derived names..."

OLD_LAST="${OLD_PACKAGE##*.}"
NEW_LAST="${NEW_PACKAGE##*.}"

if [[ "$OLD_LAST" != "$NEW_LAST" ]]; then
    # CamelCase the last segment
    OLD_CAMEL="$(tr '[:lower:]' '[:upper:]' <<< "${OLD_LAST:0:1}")${OLD_LAST:1}"
    NEW_CAMEL="$(tr '[:lower:]' '[:upper:]' <<< "${NEW_LAST:0:1}")${NEW_LAST:1}"

    OLD_THEME="${OLD_CAMEL}Theme"
    NEW_THEME="${NEW_CAMEL}Theme"

    matches=$(grep -rl "$OLD_THEME" "$ROOT_DIR" \
        --include="*.kt" --include="*.md" 2>/dev/null \
        | grep -v "/.git/" | grep -v "/build/" || true)

    if [[ -n "$matches" ]]; then
        while IFS= read -r f; do
            "${SED_INPLACE[@]}" "s/$OLD_THEME/$NEW_THEME/g" "$f"
        done <<< "$matches"
        ok "Theme function: $OLD_THEME → $NEW_THEME"
    else
        info "   No theme function ($OLD_THEME) found — skipped"
    fi

    # Warn about possible app display names
    for app_file in \
        "$ROOT_DIR/androidApp/src/main/res/values/strings.xml" \
        "$ROOT_DIR/webApp/src/webMain/resources/index.html" \
        "$ROOT_DIR/desktopApp/src/main/kotlin"/*.kt \
        "$ROOT_DIR/iosApp/Configuration/Config.xcconfig"; do
        if [[ -f "$app_file" ]] && grep -q "$OLD_CAMEL" "$app_file" 2>/dev/null; then
            warn "$app_file references '$OLD_CAMEL' — update display name manually"
        fi
    done
fi

echo ""
echo "═══════════════════════════════════════"
echo "   ✅  Package rename complete!"
echo ""
echo "      Old: $OLD_PACKAGE"
echo "      New: $NEW_PACKAGE"
echo ""
echo "   Next steps:"
echo "     1. Review for any remaining old references:"
echo "        grep -r '$OLD_LAST' --include='*.kt' --include='*.kts' \\"
echo "            --include='*.xml' --include='*.pbxproj'"
echo "     2. Update iOS display name in Info.plist if needed"
echo "     3. Clean & rebuild:"
echo "        ./gradlew clean && ./gradlew compileKotlinJvm"
echo "═══════════════════════════════════════"
echo ""
