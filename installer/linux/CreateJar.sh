#! /usr/bin/bash

cd ../../bin
# jar cmf META-INF/MANIFEST.MF lingtree.jar .
jar cf lingtree.jar .
cp lingtree.jar ../installer/linux/input > nul
rm lingtree.jar > nul
cd ../installer/linux
