#!/usr/bin/perl -w
#
# Simple cvs commit logger/mailer.  Many ideas copyied from
# original 'log.pl' scripts that ships with CVS under 'contrib'
#
# Copyright (C) 2002 Sam Clegg
# http://www.superduper.net/cgi-bin/viewcvs.cgi/cvs-log
#
# A new, improved version of this script, written in python is
# also available but you may need this if, for example, you use
# sourceforge as they only have python1.5.
#
# To use this script:
#
# 1. add log.pl to your CVSROOT
# 2. add log.pl following line to your checkoutlist:
# 3. add the following line to your CVSROOT/loginfo:
#    <pat> $CVSROOT/CVSROOT/log.pl -f <logfile> -m <emailaddr> %{svV}
#
# $superduper: log.pl,v 1.18 2001/07/20 03:05:11 samc Exp $

use strict;
use Getopt::Std;

my $users;
my $login;
my @files;
my $logfile;
my $viewcvs = "http://lamscvs.melcoe.mq.edu.au/cgi-bin/viewcvs.cgi";

sub print_usage()
{
    print STDERR "Usage:  log.pl [-m address] [-f logfile] \"module file ...\"\n";
    print STDERR "\n";
    print STDERR "\t-m <mailaddr>\t- email address(s) to mail log to.\n";
    print STDERR "\t-f <logfile>\t- logfile to append to.\n";
}

# turn off setgid
$) = $(;

# parse command line arguments
my %opts;
if (!getopts('f:m:', \%opts))
{
    print_usage();
    exit(-1);
}

if (defined $opts{'m'}) { $users   = $opts{'m'}; }
if (defined $opts{'f'}) { $logfile = $opts{'f'}; }

if (!($users || $logfile) || ! defined $ARGV[0])
{
    print_usage();
    exit(-1);
}

@files = split(/ /, $ARGV[0]);

# the first argument is the module location relative to $CVSROOT
my $modulepath = shift @files;

my $mailcmd = "| Mail -s 'CVS update: $modulepath'";

# Initialise some date and time arrays
my @mos = ("January","February","March","April","May","June","July",
           "August","September", "October","November","December");
my @days = ("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday");

(my $sec,  my $min,  my $hour,
 my $mday, my $mon,  my $year,
 my $wday, my $yday, my $isdst) = localtime;

$year += 1900;

$login = getlogin || (getpwuid($<))[0] || "nobody";

if ($logfile)
{
    open(LOG, ">>" . $logfile) or die "Could not open(" . $logfile . "): $!\n";
}

if ($users) 
{
    $mailcmd = "$mailcmd $users";
    open(MAIL, $mailcmd) or die "Could not Exec($mailcmd): $!\n";
}

# print out the log Header
if (fileno LOG)
{
    print LOG "\n****************************************\n";
    print LOG "Date:\t$days[$wday] $mos[$mon] $mday, $year @ $hour:" . sprintf("%02d", $min) . "\n";
    print LOG "Author:\t$login\n\n";
}

if (fileno (MAIL)) 
{
    print MAIL "\nDate:\t$days[$wday] $mos[$mon] $mday, $year @ $hour:" . sprintf("%02d", $min) . "\n";
    print MAIL "Author:\t$login\n\n";
}

# print the stuff from logmsg that comes in on stdin.
while (<STDIN>) 
{
    if (fileno (LOG))  { print LOG;  }
    if (fileno (MAIL)) { print MAIL; }
}

if (fileno LOG) { print LOG "\n" };

my $version;
my $lastversion;
my $file;
if ($viewcvs)
{
    if (fileno LOG)  { print LOG "web diffs:\n";  }
    if (fileno MAIL) { print MAIL "web diffs:\n"; }
  foreach $file (@files) 
  {
      next if ($file eq "Imported" || $file eq "sources" || $file eq "-" || $file eq "New" || $file eq "directory");
      ($file, $version, $lastversion) = split(/,/, $file);
#
# When files are added or removed one of the verions is "NONE"
# No need to diff in this case.
#
      if ($version eq "NONE" || $lastversion eq "NONE")
      {
          next;
      }
      my $url = "$viewcvs/$modulepath/$file.diff?r1=$lastversion&r2=$version";
      if (fileno LOG)  { print LOG "  File: $file\n  Diff: $url\n\n";  }
      if (fileno MAIL) { print MAIL "  File: $file\n  Diff: $url\n\n";  }
  }
}

close(LOG);
die "Write to $logfile failed" if $?;

close(MAIL);
die "Pipe to $mailcmd failed" if $?;

exit 0;
