#!/bin/bash

: <<!
    GPG signature tool installation script
!

source checker.sh

dependency=gpg
temp_dir=tmp
download_remote=https://www.gnupg.org/ftp/gcrypt/gnupg/gnupg-2.2.27.tar.bz2
download_file=$temp_dir/gnupg.tar.bz2

#mkdir $temp_dir

function installation() {
    echo "Start installation $download_file from local cache"
    brew install $dependency
}

function download() {
    if [ ! -f "$download_file" ]; then
        echo "Start download $download_file from $download_remote"
        curl -o $download_file $download_remote
    else
        installation
    fi
}

checker_command $dependency
if [ "$?" = "0" ]; then
    echo "Command $dependency exists on system, skip installation $dependency"
else
    echo 1
#    download
fi
