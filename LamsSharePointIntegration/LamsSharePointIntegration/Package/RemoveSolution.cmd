
@SET STSADM="c:\program files\common files\microsoft shared\web server extensions\12\bin\stsadm.exe" 

Echo Removing LamsSharePointIntegration solution.

%STSADM% -o retractsolution -name LamsSharePointIntegration.wsp -immediate
%STSADM% -o execadmsvcjobs
%STSADM% -o deletesolution -name LamsSharePointIntegration.wsp -override
%STSADM% -o execadmsvcjobs
IISRESET
:end