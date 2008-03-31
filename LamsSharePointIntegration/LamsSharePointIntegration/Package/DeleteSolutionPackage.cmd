@SET STSADM="c:\program files\common files\microsoft shared\web server extensions\12\bin\stsadm.exe"

%STSADM% -o retractsolution -name CustomApplicationPages.wsp -immediate
%STSADM% -o execadmsvcjobs
%STSADM% -o deletesolution -name CustomApplicationPages.wsp

