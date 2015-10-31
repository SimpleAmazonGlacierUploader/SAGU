*Nota Bene:* As of 10/13/2015 this app is in maintenance mode.
I'm pulling in useful commits from the community but make no guarantees about long-term support of this project.
The unlimited storage plan with [Amazon's Cloud Drive](https://www.amazon.com/clouddrive/) has solved my need for cheap
large-scale file storage.

# Simple Amazon Glacier Uploader

Multiplatform (Win/Mac/Linux) GUI Client for _Amazon Glacier_.

_Amazon Glacier_ is a long-term persistent file-storage system for cold data storage.
The _Simple Amazon Glacier Uploader_ aims to be an upload and download solution that is as durable as your data.

SAGU is a single `.jar` file Glacier interface written in Java for cross-platform usability.
The use of Java assures that you will have access to your files regardless of your operating system when it is time
to retrieve your data.

For latest binary download see [project's web page](http://simpleglacieruploader.brianmcmichael.com).

### Contributors:

	https://github.com/brianmcmichael
	https://github.com/liry
	https://github.com/greenwoodma
	https://github.com/Fensterbank
	https://github.com/richardneish
	https://github.com/philosophicles

### License
GNU General Public License Version 3. See [LICENSE](LICENSE)

### Additional License Information
Contains images from Silk Icon Set v.1.3 (http://www.famfamfam.com/lab/icons/silk/)

### How-tos
##### Build runnable JAR including all dependencies
```
mvn clean package assembly:single
```

Resulting archive: `target/GlacierUploader-*-jar-with-dependencies.jar`
