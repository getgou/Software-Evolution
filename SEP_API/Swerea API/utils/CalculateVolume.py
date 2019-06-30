#! /usr/bin/env python3
'''
VOLUME CALCULATION STL binary MODELS
Author: Mar Canet (mar.canet@gmail.com) - september 2012
Description: useful to calculate cost in a 3D printing ABS or PLA usage
Modified by:
Author: Saijin_Naib (Synper311@aol.com)
Date: 2016-06-26 03:55:13.879187
Description: Added input call for print material (ABS or PLA), added print of object mass, made Python3 compatible, changed tabs for spaces
Material Mass Source: https://www.toybuilderlabs.com/blogs/news/13053117-filament-volume-and-length
'''

import _struct
import sys
class STLUtils:
    def resetVariables(self):
        self.normals = []
        self.points = []
        self.triangles = []
        self.bytecount = []
        self.fb = []  # debug list

    # Calculate volume for the 3D mesh using Tetrahedron volume
    # based on: http://stackoverflow.com/questions/1406029/how-to-calculate-the-volume-of-a-3d-mesh-object-the-surface-of-which-is-made-up
    def signedVolumeOfTriangle(self, p1, p2, p3):
        v321 = p3[0] * p2[1] * p1[2]
        v231 = p2[0] * p3[1] * p1[2]
        v312 = p3[0] * p1[1] * p2[2]
        v132 = p1[0] * p3[1] * p2[2]
        v213 = p2[0] * p1[1] * p3[2]
        v123 = p1[0] * p2[1] * p3[2]
        return (1.0 / 6.0) * (-v321 + v231 + v312 - v132 - v213 + v123)

    def unpack(self, sig, l):
        s = self.f.read(l)
        self.fb.append(s)
        return _struct.unpack(sig, s)

    def read_triangle(self):
        n = self.unpack("<3f", 12)
        p1 = self.unpack("<3f", 12)
        p2 = self.unpack("<3f", 12)
        p3 = self.unpack("<3f", 12)
        b = self.unpack("<h", 2)

        self.normals.append(n)
        l = len(self.points)
        self.points.append(p1)
        self.points.append(p2)
        self.points.append(p3)
        self.triangles.append((l, l + 1, l + 2))
        self.bytecount.append(b[0])
        return self.signedVolumeOfTriangle(p1, p2, p3)

    def read_length(self):
        length = _struct.unpack("@i", self.f.read(4))
        return length[0]

    def read_header(self):
        self.f.seek(self.f.tell() + 80)

    def cm3_To_inch3Transform(self, v):
        return v * 0.0610237441

    def calculateVolume(self):
        self.resetVariables()
        totalVolume = 0
        try:
            stlfile = "E:/SEP/2018Group13/downloads/stl/{}".format(buildfile);
            self.f = open(stlfile, "rb")
            self.read_header()
            l = self.read_length()
            try:
                while True:
                    totalVolume += self.read_triangle()
            except Exception as e:
                pass
            totalVolume = (totalVolume/1000)
            print(totalVolume)
            
        except Exception as e:
            print(e)
        return totalVolume


if __name__ == '__main__':
        mySTLUtils = STLUtils()
        mySTLUtils.calculateVolume()