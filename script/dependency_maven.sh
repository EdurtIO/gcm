#!/bin/bash

: <<!
    Dependency maven installation script
!

source checker.sh

HOME=$(pwd)
CONFIG="$HOME/config/maven.conf"
VERSIONS=$(sed '/^SUPPORT_VERSION=/!d;s/.*=//' $CONFIG)
DOWNLOAD_URL=$(sed '/^DOWNLOAD_URL=/!d;s/.*=//' $CONFIG)
TEMP_DIR=$HOME/tmp
DEFAULT_VERSION=$(sed '/^DEFAULT_VERSION=/!d;s/.*=//' $CONFIG)
DOWNLOAD_FILE=$TEMP_DIR/apache-maven-$DEFAULT_VERSION-bin.tar.gz
M_HOME=apache-maven-3.6.3
SUPPORT_VERSIONS=($(echo $VERSIONS | awk -v f=1 -v t=6 'BEGIN{FS=",";OFS=" "} {for(i=f;i<=t;i++) printf("%s%s",$i,(i==t)?"\n":OFS)}'))

function print_support_versions() {
    printf "Currently only the following versions are supported:\n"
    for version in "${SUPPORT_VERSIONS[@]}"; do
        echo "$version"
    done
}

REAL_URL=${DOWNLOAD_URL//\$\{VERSION\}/$DEFAULT_VERSION}

function download() {
    if [ ! -f "$DOWNLOAD_FILE" ]; then
        echo "Start download $DOWNLOAD_FILE from $REAL_URL"
        curl -o $DOWNLOAD_FILE $REAL_URL
    fi
}

function installation() {
    download
    mkdir -p ~/.tools
    cd ~/.tools
    tar -xvf $DOWNLOAD_FILE -C ~/.tools
    echo "MAVEN_HOME=~/.tools/$M_HOME" >> ~/.bash_profile
    echo "export MAVEN_HOME" >> ~/.bash_profile
    echo 'export PATH=${PATH}:${MAVEN_HOME}/bin' >> ~/.bash_profile
    source ~/.bash_profile
    rm -rf $DOWNLOAD_FILE
}

printf "Install home path <%s>\n" $HOME
print_support_versions
printf "Current install version <%s>\n" $DEFAULT_VERSION
installation
