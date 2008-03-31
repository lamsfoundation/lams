REM @SET SPDIR="c:\program files\common files\microsoft shared\web server extensions\12"
REM %SPDIR%\bin\stsadm -o addsolution -filename CustomApplicationPages.wsp

@SET STSADM="c:\program files\common files\microsoft shared\web server extensions\12\bin\stsadm.exe"

Echo Installing LamsSharePointIntegration.wsp in WSS Solution Package Store
%STSADM% -o addsolution -filename LamsSharePointIntegration.wsp
%STSADM% -o execadmsvcjobs

Echo Deploying Solution Package LamsSharePointIntegration.wsp
%STSADM% -o deploysolution -name LamsSharePointIntegration.wsp -immediate -allowGacDeployment -force
%STSADM% -o execadmsvcjobs

IISRESET
:end