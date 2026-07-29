#!/bin/sh

# Compose Hot Reload's default DevTools window (an AWT/Swing overlay attached to the app window) fails
# to initialize on this NixOS/XWayland setup: it crashes at startup (see
# desktopApp/build/run/jvmMain/shutdown.log after a run) and silently takes the whole
# recompile-and-push orchestration channel down with it. The symptom is confusing because it looks
# like hot reload is *working*: every edit still triggers a Gradle rebuild that reports "BUILD
# SUCCESSFUL" in jvmMain.chr.log, but the new bytecode is never actually pushed into the running JVM,
# so the app just never updates. -Pcompose.reload.devToolsHeadless=true keeps the reload/orchestration
# machinery but skips that overlay window, which avoids the crash. Verified by editing a running
# screen's text with both settings: without this flag the change compiles but never appears in the
# window; with it, the edit shows up a few seconds after saving.
is_project_root="$(stat ./gradlew 2>/dev/null)"
while [ ! "$is_project_root" ]; do
    cd ..
    is_project_root="$(stat ./gradlew 2>/dev/null)"
done

#export NUCLEUS_TAO_LINUX_RENDERER=x11

nix-shell --run './gradlew :desktopApp:hotRunJvm --mainClass=MainKt --auto -Pcompose.reload.devToolsHeadless=true'
