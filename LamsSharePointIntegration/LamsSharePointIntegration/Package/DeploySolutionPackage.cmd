REM DevInstall.cmd
REM @GOTO end

@SET STSADM="c:\program files\common files\microsoft shared\web server extensions\12\bin\stsadm.exe"

Echo Retracting Solution Package LamsSharePointIntegration.wsp
%STSADM% -o retractsolution -name LamsSharePointIntegration.wsp -immediate
%STSADM% -o execadmsvcjobs

Echo Deleting Solution Package LamsSharePointIntegration.wsp
%STSADM% -o deletesolution -name LamsSharePointIntegration.wsp -override
%STSADM% -o execadmsvcjobs

Echo Generating Solution Package LamsSharePointIntegration.wsp
if EXIST LamsSharePointIntegration.wsp del LamsSharePointIntegration.wsp
cd ..
makecab /f Solution\cab.ddf
cd package

Echo Installing LamsSharePointIntegration.wsp in WSS Solution Package Store
%STSADM% -o addsolution -filename LamsSharePointIntegration.wsp
%STSADM% -o execadmsvcjobs

Echo Deploying Solution Package LamsSharePointIntegration.wsp
%STSADM% -o deploysolution -name LamsSharePointIntegration.wsp -immediate -allowGacDeployment -force
%STSADM% -o execadmsvcjobs

IISRESET
:end