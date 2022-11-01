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

#### Perform release in SCM (GIT)
```
mvn release:prepare
```

This will do some checks, change the version in the POM to a new version (you will be prompted for the versions to use),
commit POM, tag the code in the SCM, bump the version in the POM and commit this POM.

One can first simulate the release (with no changes in SCM) using parameter `-DdryRun=true`.

Also do not forget to update `CHANGELOG.md`, add [release](https://github.com/SimpleAmazonGlacierUploader/SAGU/releases)
and [update website](https://github.com/SimpleAmazonGlacierUploader/SimpleAmazonGlacierUploader.github.io).
