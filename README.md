# Simple Amazon Glacier Uploader

Multiplatform (Win/Mac/Linux) GUI Client for _Amazon Glacier_.

_Amazon Glacier_ is a long-term persistent file-storage system for cold data storage.
The _Simple Amazon Glacier Uploader_ aims to be an upload and download solution that is as durable as your data.

SAGU is a single `.jar` file Glacier interface written in Java for cross-platform usability.
The use of Java assures that you will have access to your files regardless of your operating system when it is time
to retrieve your data.

##### Download latest binary: [GlacierUploader-0.76.0.jar](https://github.com/SimpleAmazonGlacierUploader/SAGU/releases/download/GlacierUploader-0.76.0/GlacierUploader-0.76.0.jar)

## Contributors:

	https://github.com/brianmcmichael
	https://github.com/liry
	https://github.com/greenwoodma
	https://github.com/Fensterbank
	https://github.com/richardneish
	https://github.com/philosophicles

*Note from Brian L. McMichael:* As of 10/13/2015 this app is in maintenance mode.
I'm pulling in useful commits from the community but make no guarantees about long-term support of this project.
The unlimited storage plan with [Amazon's Cloud Drive](https://www.amazon.com/clouddrive/) has solved my need for cheap
large-scale file storage.

## License
GNU General Public License Version 3. See [LICENSE](LICENSE)

### Additional License Information
Contains images from Silk Icon Set v.1.3 (http://www.famfamfam.com/lab/icons/silk/)

## Development

One needs JDK 11+ for building (requirement of some dependencies). Target release is still set to Java 8 (so resulting
jar should be runnable under JRE 8).

#### Build runnable JAR including all dependencies
```
mvn clean package assembly:single
```

Resulting archive: `target/GlacierUploader-*-jar-with-dependencies.jar`

#### Perform release of new version
Double check you are in the master branch with the latest changes pulled. Also check that `CHANGELOG.md` contains info
about the release to be done.

This will do some checks, change the version in the POM to a new version (you will be prompted for the versions to use),
commit POM, tag the code in the SCM, bump the version in the POM and commit+push this POM.
One can first simulate the release (with no changes in SCM) using parameter `-DdryRun=true`.
```
mvn release:prepare
```

Then, switch to just created release tag
```
git checkout GlacierUploader-X.Y.Z
```
and build the runnable JAR (see above) from there. Create [the GitHub Release](
https://github.com/SimpleAmazonGlacierUploader/SAGU/releases) and attach given JAR.
Then, edit [the "next" Milestone](https://github.com/SimpleAmazonGlacierUploader/SAGU/milestones?state=open) to have
the released version in the Title and close it. Next create the new "next" Milestone.

Also do not forget to update [the Website](
https://github.com/SimpleAmazonGlacierUploader/SimpleAmazonGlacierUploader.github.io).

#### Bulk bump of dependency versions
```
mvn versions:use-latest-versions
```
