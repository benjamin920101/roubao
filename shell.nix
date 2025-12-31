{ pkgs ? import <nixpkgs> {} }:

let
  androidComposition = pkgs.androidenv.composeAndroidPackages {
    cmdLineToolsVersion = "11.0";
    platformToolsVersion = "35.0.2";
    buildToolsVersions = [ "34.0.0" ];
    platformVersions = [ "34" ];
    includeEmulator = false;
    includeNDK = false;
    useGoogleAPIs = false;
    abiVersions = [ "x86_64" "arm64-v8a" ];
  };
  androidSdk = androidComposition.androidsdk;

  # FHS environment for running non-NixOS binaries
  fhs = pkgs.buildFHSEnv {
    name = "android-fhs";
    targetPkgs = pkgs: with pkgs; [
      jdk17
      git
      glibc
      zlib
      ncurses
      stdenv.cc.cc.lib
    ];
    runScript = "bash";
    profile = ''
      export ANDROID_HOME="${androidSdk}/libexec/android-sdk"
      export ANDROID_SDK_ROOT="${androidSdk}/libexec/android-sdk"
    '';
  };
in
pkgs.mkShell {
  buildInputs = [
    fhs
    pkgs.jdk17
    androidSdk
  ];

  ANDROID_HOME = "${androidSdk}/libexec/android-sdk";
  ANDROID_SDK_ROOT = "${androidSdk}/libexec/android-sdk";

  shellHook = ''
    echo "Use 'android-fhs' to enter FHS environment for building"
  '';
}
