#!/bin/bash

:<<!
GPG signature tool installation script
!

dependency=gpg
temp_dir=tmp
temp_installation_dir=$temp_dir/gnupg-2.2.27
download_remote=https://www.gnupg.org/ftp/gcrypt/gnupg/gnupg-2.2.27.tar.bz2
download_file=$temp_dir/gnupg.tar.bz2

mkdir $temp_dir

function checker() {
    which $dependency > /dev/null
    if [ $? -eq 0 ]; then
        return 0
    else
        return 1
    fi
}

function installation() {
    echo "Start installation $download_file from local cache"
#    tar -xvzf $download_file -C $temp_dir
#    cd $temp_installation_dir
#    ./configure
#    make
#    `make install`
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

checker
if [ $? -eq 0 ]; then
    echo 'Skip installation '$dependency
else
    download
fi
