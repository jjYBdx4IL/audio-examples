#!/bin/bash
# vim:set sw=2 et:
set -Eex
set -o pipefail

builddir=$1

# patched sources
cd $builddir
git clone https://github.com/jjYBdx4IL/xt-audio.git git-xt-audio
install -d generated-sources
for f in java java-sample java-driver; do
  destdir=$builddir/generated-sources/xt-audio-$f
  rm -rf $destdir
  mv -f git-xt-audio/src/$f $destdir
done

# unpatched native libs, rename according to JNA rules
cd $builddir/unpacked-bins/java-xt
for f in *-x??; do
  dest=${f/-x86/-x86-32}
  dest=${dest/-x64/-x86-64}
  dest=$builddir/classes/$dest
  install -d $dest
  cp -f $f/* $dest/.
done

