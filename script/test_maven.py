#!/usr/bin/env python  
# -*- coding:utf-8 _*-
""" 
@Author: shicheng 
@License: Apache Licence 
@File: test_maven.py 
@Time: 2021-02-07 16:25
@Contact: shicheng 
@Site: ${SITE} 
@Software: incubator-gcm
"""
from unittest import TestCase

import maven


class TestMaven(TestCase):

    def test_get_version(self):
        self.assertIsNotNone(maven.get_version())

    def test_set_version(self):
        maven.set_version(version='1.0.8-SNAPSHOT')
        self.assertTrue(True)
