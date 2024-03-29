# Simple Amazon Glacier Uploader Changelog

### 2022/11/01 - Version 0.77.1

- Bump dependency versions
  - [AWS SDK for Java version numbers from 1.9.5 to 1.10.31 are incompatible with TLS 1.3.](
    https://aws.amazon.com/blogs/developer/tls-1-3-incompatibility-with-aws-sdk-for-java-versions-1-9-5-to-1-10-31/)

### 2020/09/21 - Version 0.77.0

- Add new region: South America (São Paulo), Brasil.
- Fix class test for Windows.
- Update URL website to https://simpleamazonglacieruploader.github.io

### 2018/12/09 - Version 0.76.0

- Add new regions: EU (Frankfurt, EU (London), EU (Paris), Asia Pacific (Seoul), Asia Pacific (Osaka-Local),
Asia Pacific (Mumbai), Asia Pacific (Singapore), China (Beijing), China (Ningxia), Canada (Central), US East (Ohio),
AWS GovCloud (US)

### 2016/02/07 - Version 0.75.0

- Upgrade to Java version 8 (i.e. Java 8 is required).
- Redesign upload window. Add progress bar of current file.
- If properties not present in working dir, use '.sagu' dir in user's home.
- Add --properties-dir command line argument.
- Add escaping to YAML logs.
- Add enclosing of values to CSV log. Values in CSV log are now enclosed in double quotes. Double quotes in values
are being escaped by second double quote.
- Remove trailing comma from CSV logs. It's forbidden in CSV specification. See RFC 4180.
- One can now easily traverse between input fields and buttons using tab key.

### 2015/10/13 - Version 0.74.7

- Implemented logging to the well parsable YAML format
- Error Message is displayed if log file to open does not exist
- Code enhancements courtesy Fensterbank
- Additional endpoints and refactorings by Libor Rysavy
- Fixes by richardneish and philosophicles

### 2013/08/02 - Version 0.74.6

- Update all dependencies
- Add something of a progressbar on multi-file uploads courtesy GitHubber greenwoodma (https://github.com/greenwoodma)
- Code enhancements courtesy greenwoodma

### 2012/12/14 - Version 0.74.5

- Regex clean file description for upload. This should reduce
InvalidSignatureExceptions.

### 2012/12/04 - Version 0.74.4

Done with classes! Back to work on SAGU.

- Fix issue with errors downloading from servers other than US East.
- Updated AWS SDK to 1.3.26
- Fixed bug listing only the first 10 vaults on a server.

### 2012/10/03 - Version 0.74.3

- Bug-patch: File descriptions under 0.74.2 were getting uploaded with a "?" character as a result of the Unicode
cleaning. Fixed. Check Glacier.txt and Glacier.csv for file descriptions of files uploaded under 0.74.2.

### 2012/10/02 - Version 0.74.2

- Increased length of time before socket timeout and doubled number of retries. Should reduce timeout errors for large
files on slower connections.
- Upload errors are now logged to GlacierErrors.txt.
- Clean non-unicode characters from upload file path description to prevent possible signature exception.

### 2012/09/24 - Version 0.74.1

- Inventory request is saved as format "[vaultname][requesteddatetime].txt" now.
- Minor misc. fixes.

### 2012/09/23 - Version 0.74

- Request a vault inventory from AWS. Current version is rough. Saves unparsed response to file.
    (NB: This is not a replacement for maintaining a local log file. 
    Amazon does not store named files, only ArchiveID's. 
    Archives uploaded with previous versions SAGU have included the filename as a descriptor, 
    but archives uploaded with other programs may not. Use the inventory retrieval to acquire 
    the Archive ID's within a particular vault. 
    It takes AWS approximately 4 hours to prepare your inventory for download.)
- Various fixes.
- Updated AWS Java SDK to 1.3.21.

### 2012/09/20 - SAGU goes Git!

- Development stall for a couple of days while I learned Git.
- Check out the public repository here: SAGU Source at Github

### 2012/09/15 - Version 0.73

- Masked secret key.
- Added a button to refresh vaults.
- Added logo.

### 2012/09/11 - Version 0.72

- Updated AWS SDK to 1.3.19.
- Removed 4GB file limit. The updated SDK should allow for files larger than 4GB. Please report errors.

### 2012/09/09 - Version 0.71

- Enhancements to upload bar. See what's uploading and what's done.
- Program now regenerates tree-hashes more frequently. Should result in fewer timeouts.
- Bug-fixes, code cleaning.

### 2012/09/06 - Version 0.7

- The threaded update. UI no longer sticks during upload/download.
- Indeterminate progressbar added. To be enhanced in upcoming releases.
- Batch multiple upload requests at once, even to different servers.
- Have several download requests processing at once.
- UI tweaks and graphical assets.
- SAGU now creates a Glacier.log file, a Glacier.txt file, and a Glacier.csv file by default.
- Glacier.log files are intended to be parsed by future versions of the program and for export. .txt files and .csv
files are for viewing convenience.
- OSX users should select the option to view .txt files to prevent logs from opening in the console.
- Known Bug: Progress bar disappears during multiple uploads if one completes first. This is cosmetic. The process
still continues in the background until completed or until an error is received. This will be corrected in an update.

### 2012/09/04 - Version 0.62

- Select upload vault from existing. Selecting a server location populates a dropdown with existing vaults on that
server.

### 2012/09/02 - Version 0.61

- Ability to export logs.
- Improved file selection for download mode in OSX.
- Choose log output (.log, .txt, .csv)
- Save log output preferences and reload on restart.

### 2012/08/31 - Version 0.6

- UI redesign.
- Download support (Currently Experimental - Unthreaded)
- Create vault.
- Updated AWS libraries.

### 2012/08/29 - Version 0.52

- Bug-Fixes.

### 2012/08/28 - Version 0.51

- Better multi-file support.
- Better error handling.

### 2012/08/27 - Version 0.5

- Added multiple file upload with drag and drop support. Be careful with this. Glacier is not a second hard drive,
it works by uploading single files and downloading or deleting by ArchiveID. Amazon does not provide a method to
retrieve ArchiveID's right now, so your log files are your access to your files. I would recommend zipping large
numbers of files together and uploading them as a single file.
- Even better logging! Switched from DataOutput UTF to Writer. Logs should now have standardized layouts and no machine
characters. (I recommend saving your old log elsewhere and letting the program generate a new one. Your old log is fine,
but some text viewers get confused by data output.)
- Added simple notification bar that appears when program is uploading. Progress bar and threading planned in near
future.

### 2012/08/26 - Version 0.4

- Added delete archive function. Set AWS Keys and Location/Vault, then insert Archive ID to delete.
- Saves preferences to "SAGU.properties" in launch folder and reloads settings on launch.
- Built with Java 1.6 libraries. Java 7 no longer required.
- Set executable bit for Linux/OSX.
- Maintenance fix to logging. ArchiveID's logged under v0.2-0.3 contain an extra character at the beginning which
was a rogue Java artifact. (SAGU will automatically handle this if entered, but users should remove the antecedent
extra character in early logs if using the archiveID with other utilities.)

### 2012/08/25 - Version 0.3

- Direct links to AWS Key Page, Glacier Vault Creation, Log file, SAGU Update site.
- Enable right-click to copy/paste.

### 2012/08/24 - Version 0.2

- Upload Logging!
- Logging is enabled by default. Saves to Glacier.log in program directory.
- Log contains Archive ID, Filename, Vault Name, Upload Date
- ArchiveID will be useful for deleting or retrieving individual archives.

### 2012/08/23 - Version 0.1

- Ability to upload a single file at a time to the Glacier service.
- Requires Security Credentials from Amazon AWS Console.
https://portal.aws.amazon.com/gp/aws/securityCredentials
- Requires you to create a vault at the Glacier Console.
https://console.aws.amazon.com/glacier/home
- Select hosting location via dropdown.
- Select single file.
- Upload file to selected vault/server.
- (Amazon Glacier inventory is updated approximately once per day. It may take 24 hours for your files to appear.)

### 2012/08/21 - Amazon Glacier Announced
https://forums.aws.amazon.com/ann.jspa?annID=1599
