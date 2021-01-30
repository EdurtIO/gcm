#!/bin/bash

: <<!
    System checker script
!

function checker_command() {
    : '
        Check command is exists on system
        e.g: checker_command <command>
            If 0 is returned, the command exists in the system
            If 1 is returned, the command does not exist
    '
    echo "Check $1 command is exists on system"
    which "$1" >/dev/null && hasDependency=true
    if [ "$hasDependency" = "true" ]; then
        return 0
    else
        return 1
    fi
}

function checker_fd() {
    : '
        Check whether the file exists and has read and write permissions
        e.g: checker_file <file_path>|<directory_path>
            If 0 is returned, the file exists in the system
            If 1 is returned, the file does not exist
    '
    echo "Check $1 file is exists on system path $(pwd)"
    if [ -f "$1" ] || [ -d "$1" ]; then
        return 0
    else
        return 1
    fi
}
