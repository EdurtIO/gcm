#!/usr/bin/env python
# -*- coding:utf-8 _*-
"""
@Author: qianmoq
@License: Apache Licence
@File: code.py
@Time: 2021-01-04 20:53:49
@Contact: qianmoq
@Site:
@Software: incubator-compass
"""
import os
import sys
import time

root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SERVER_NAME = "Gcm"
PROJECT_NAME = 'incubator-gcm'


def type_enum(type=None):
    types = {
        'branch': 'The branch information passed cannot be empty!'
    }
    return types.get(type)


def command_enum(command='', parameter=None):
    commands = {
        'check': check,
        'check-style': check_style,
        'check-bugs': check_bugs,
        'branch-new': set_new_branch
    }
    try:
        method = commands.get(command, parameter)
        if method:
            method(parameter)
        else:
            print_help()
    except TypeError as err:
        method()
    except Exception as err:
        print(str(err))
        print_help()


def print_help():
    print('Usage: Compass {check|check-style|check-bugs|branch-new}')
    print('''
        echo       check:             check source code
        echo       check-style:       check source code style
        echo       check-bugs:        check source code bugs
        echo       new-branch:        checkout a new branch
    ''')
    sys.exit(1)


def print_template():
    print('execute {} project root path：{}'.format(SERVER_NAME, root))
    print('execute {} command，operation time：{}'.format(SERVER_NAME, time.strftime('%Y-%m-%d %H:%M:%S')))


def check_parameter(type=None, parameter=None):
    if parameter is None or parameter == '':
        print(type_enum(type=type))
        sys.exit(1)


def check(token=None):
    check_parameter(type='user', parameter=token)
    command = './mvnw clean install checkstyle:check findbugs:check'
    os.chdir(root)
    print('execute command：{}'.format(command))
    os.system(command)


def check_style():
    os.chdir(root)
    command = './mvnw clean install checkstyle:check -DskipTests'
    print('execute command：{}'.format(command))
    os.system(command)


def set_new_branch(branch=None):
    check_parameter(type='branch', parameter=branch)
    print('set a new branch：{}'.format(branch))
    os.chdir(root)
    command = 'git checkout -b ' + branch
    print('execute command：{}'.format(command))
    os.system(command)


def check_bugs():
    os.chdir(root)
    command = './mvnw clean install findbugs:check -DskipTests'
    os.system(command)


if __name__ == '__main__':
    print_template()
    if len(sys.argv) == 1:
        print_help()
    if len(sys.argv) < 3:
        command_enum(command=sys.argv[1])
    else:
        command_enum(command=sys.argv[1], parameter=sys.argv[2])
