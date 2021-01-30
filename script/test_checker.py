#!/usr/bin/env python  
# -*- coding:utf-8 _*-
""" 
@Author: shicheng 
@License: Apache Licence 
@File: test_checker.py 
@Time: 2021-01-29 12:28
@Contact: shicheng 
@Site: ${SITE} 
@Software: incubator-gcm
"""
from unittest import TestCase

import checker


class TestChecker(TestCase):

    def test_checker_command(self):
        self.assertTrue(checker.checker_command(command="pwd") == 0)
        self.assertTrue(checker.checker_command(command="pwd1") == 1)

    def test_checker_fd(self):
        self.assertTrue(checker.checker_fd(source="checker.py") == 0)
        self.assertTrue(checker.checker_fd(source="checker1.py") == 1)
