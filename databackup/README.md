# Data backup
_V1.0: 2022-04-07_

A small application to save a folder recursively into a zip file.
It reads the input folder's and the output file's path from the command line argument.
You can add relative or absolute paths. It saves the zip into files named with DateTime postfix, to use it as a
cron job.

It filters files in the folder:
- didn't save hidden files
- only saves files with specific file name content (.gpkg)

The application could be also run in a docker container, with a predefined crontab. Dockerfile and
docker-compose.yml created.

You can find the source code on [GitHub](https://github.com/ferenc-attila/BNPI/tree/master/databackup),
and the docker image on [Docker Hub](https://hub.docker.com/r/ferencattila/databackup).

## TODO

- [ ] implementation of more test cases
- [ ] read paths from a properties file
- [x] logback implementation
- [ ] documentation
- [ ] dynamic filters