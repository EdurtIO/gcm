#!/usr/bin/env python
# -*- coding:utf-8 _*-
"""
@Author: qianmoq
@License: Apache Licence
@File: checker.py
@Time: 2021-01-28 22:23:49
@Contact: qianmoq
@Site:
@Software: incubator-gcm
"""


def checker_command(command=None):
    """
        Check command is exists on system
        e.g: checker_command <command>
            If 0 is returned, the command exists in the system
            If 1 is returned, the command does not exist
    """
    from shutil import which
    print('Check <%s> command is exists on system' % command)
    if which(command) is not None:
        return 0
    else:
        return 1


def checker_fd(source=None):
    """
        Check whether the file exists and has read and write permissions
            e.g: checker_file <file_path>|<directory_path>
            If 0 is returned, the file exists in the system
            If 1 is returned, the file does not exist
    """
    import os
    print('Check <%s> file is exists on system path %s' % (source, os.getcwd()))
    if os.path.exists(source) is True:
        return 0
    else:
        return 1
