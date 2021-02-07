#!/usr/bin/env python  
# -*- coding:utf-8 _*-
""" 
@Author: shicheng 
@License: Apache Licence 
@File: maven.py 
@Time: 2021-02-07 16:10
@Contact: shicheng 
@Site:  
@Software: incubator-gcm
"""


def get_version():
    """
    get version form maven project
    :return: version
    """
    print('Get version from project, please ...')
    import os
    command = './mvnw'
    import checker
    if checker.checker_command(command='mvn') == 0:
        command = 'mvn'
    version = os.popen('{} help:evaluate -Dexpression=project.version -q -DforceStdout'.format(command))
    return version.read()


def set_version(version=None):
    """
    Set a new version. When the new version is consistent with the old version, skip the operation
    :param version: new version
    """
    if version is None:
        print('set version must not null')
    old_version = get_version()
    if version == old_version:
        print('Set new version <%s> equals old version <%s>, skip it!' % (version, old_version))
    else:
        import os
        os.system('mvn versions:set -DnewVersion=%s' % version)
