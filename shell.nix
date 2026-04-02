{pkgs ? import <nixpkgs> {}}:
(pkgs.buildFHSEnv {
  name = "android-dev";
  targetPkgs = pkgs:
    with pkgs; [
      xorg.libX11
      xorg.libXext
      xorg.libXrender
      xorg.libXtst
      xorg.libXi
      xorg.libxcb
      xorg.libXfixes
      xorg.libXcursor
      xorg.libXrandr
      libGL
      mesa
      wayland
      vulkan-loader
      alsa-lib
      pulseaudio
      dbus
      glib
      stdenv.cc.cc.lib
      zlib
      jdk21
      gradle
    ];
  # This is the key — multiPkgs covers /lib and /lib64
  multiPkgs = pkgs:
    with pkgs; [
      stdenv.cc.cc.lib
      zlib
    ];
  runScript = "bash";
}).env
