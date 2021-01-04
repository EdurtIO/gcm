#!/bin/bash

HOME=$(
    cd $(dirname ${BASH_SOURCE[0]})/..
    pwd
)

python $HOME/bin/code.py $1 $2
