@echo off
if EXIST LamsSharePointIntegration.wsp del LamsSharePointIntegration.wsp
cd ..
Echo Creating solution package for LamsSharePointIntegration
makecab /f Solution\cab.ddf

Echo Package created.
cd package

copy LamsSharePointIntegration.wsp Z:\Package


